package net.alteiar.lendyr.engine.action.major;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.action.GameAction;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.action.result.AttackActionResult;
import net.alteiar.lendyr.engine.entity.PersonaEntity;
import net.alteiar.lendyr.engine.entity.exception.NotAllowedException;
import net.alteiar.lendyr.engine.entity.exception.NotEnoughActionException;
import net.alteiar.lendyr.engine.entity.exception.NotFoundException;
import net.alteiar.lendyr.engine.random.SkillResult;
import net.alteiar.lendyr.model.encounter.CurrentPersona;
import net.alteiar.lendyr.model.persona.Attack;
import net.alteiar.lendyr.model.persona.AttackType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Log4j2
public class ChargeAttackAction implements GameAction {
  private final UUID sourceId;
  private final UUID targetId;
  private final List<Vector2> positions;

  // Stateful variables
  private PersonaEntity personaSource;
  private PersonaEntity personaTarget;

  @Builder
  public ChargeAttackAction(@NonNull UUID sourceId, @NonNull UUID targetId, @NonNull List<Vector2> positions) {
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.positions = positions;
  }

  @Override
  public void ensureAllowed(GameContext context) {
    personaSource = context.getGame().findById(sourceId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", sourceId)));
    personaTarget = context.getGame().findById(targetId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", targetId)));

    CurrentPersona currentPersona = context.getGame().getEncounter().getEncounter().getCurrentState().getCurrentPersona();
    if (currentPersona.isMajorActionUsed()) {
      throw new NotEnoughActionException(String.format("the persona with id [%s] has already consumed the major action", personaSource.getId()));
    }
    UUID currentPersonaId = context.getGame().getEncounter().getEncounter().getCurrentState().getInitiative().get(currentPersona.getInitiativeIdx());
    if (!Objects.equals(currentPersonaId, personaSource.getId())) {
      throw new NotAllowedException(String.format("the persona with id [%s] is not the current ", personaSource.getId()));
    }

    // Validate distance
    float totalDest = 0;
    Vector2 current = personaSource.getPosition();
    for (Vector2 target : positions) {
      totalDest += roundToQuarter(Vector2.dst(current.x, current.y, target.x, target.y));
      current = target;

      // Validate no collision.
      if (context.getGame().getMap().checkCollision(personaSource.getId(), target)) {
        throw new NotAllowedException("the move is illegal because of obstacle");
      }
    }

    double halfSpeed = Math.ceil(personaSource.getSpeed() / 2d);
    if (totalDest > halfSpeed) {
      throw new NotAllowedException(
          String.format("the persona with id [%s] cannot move this far; distance: %.0f ; max distance: %.0f ", personaSource.getId(), halfSpeed, totalDest));
    }

    if (!AttackType.MELEE.equals(personaSource.getAttack().getAttackType())) {
      throw new NotAllowedException("The charge can only be done with a melee attack");
    }

    // Check if they are in range
    Rectangle attackRange = personaSource.getAttackLongBoundingBox();
    attackRange.setPosition(positions.getLast());
    Rectangle target = personaTarget.getDefenceBoundingBox();

    if (!attackRange.overlaps(target)) {
      throw new NotAllowedException("Target is out of range");
    }
  }

  @Override
  public ActionResult apply(GameContext context) {
    log.info("{} attack {}", personaSource.getName(), personaTarget.getName());

    personaSource.setPosition(positions.getLast());

    Attack attack = personaSource.getAttack();

    // Make the attack roll
    SkillResult result = personaSource.skillTest(attack.getAttack(), attack.getAttackFocus(), 1);
    boolean attackHit = result.computeTotal() >= personaTarget.getDefense();
    log.info("Attack hit: {} ({} >= {})", attackHit, result.computeTotal(), personaTarget.getDefense());

    // Do damage if attack succeeded
    int mitigatedDamage = 0;
    int totalDamage = 0;
    if (attackHit) {
      totalDamage = personaSource.rollDamage();
      log.info("Attack does {} raw damages", totalDamage);
      if (personaSource.getAttack().isPenetrating()) {
        mitigatedDamage = personaTarget.takePenetratingDamage(totalDamage);
      } else {
        mitigatedDamage = personaTarget.takeDamage(totalDamage);
      }
      log.info("Attack does {} mitigated damages", mitigatedDamage);
    }
    context.getGame().getEncounter().useMajorAction();

    return AttackActionResult.builder()
        .attackResult(result)
        .rawDamage(totalDamage)
        .mitigatedDamage(mitigatedDamage)
        .build();
  }

  private float roundToQuarter(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
