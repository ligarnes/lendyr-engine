package net.alteiar.lendyr.entity.action.combat.major;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.action.ActionResult;
import net.alteiar.lendyr.entity.action.BaseAction;
import net.alteiar.lendyr.entity.action.ChargeAttackActionResult;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotEnoughActionException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.model.persona.Attack;
import net.alteiar.lendyr.model.persona.AttackType;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Log4j2
@ToString
public class ChargeAttackAction extends BaseAction implements MajorAction {
  @Getter
  private final UUID sourceId;
  @Getter
  private final UUID targetId;
  @Getter
  private final List<Position> positions;

  // Stateful variables
  private PersonaEntity personaSource;
  private PersonaEntity personaTarget;

  @Builder
  public ChargeAttackAction(@NonNull UUID sourceId, @NonNull UUID targetId, @NonNull List<Position> positions) {
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.positions = positions;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    personaSource = context.findById(sourceId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", sourceId)));
    personaTarget = context.findById(targetId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", targetId)));

    if (context.getEncounter().isMajorActionUsed()) {
      throw new NotEnoughActionException(String.format("the persona with id [%s] has already consumed the major action", personaSource.getId()));
    }
    UUID currentPersonaId = context.getEncounter().getCurrentPersona().getPersonaId();
    if (!Objects.equals(currentPersonaId, personaSource.getId())) {
      throw new NotAllowedException(String.format("the persona with id [%s] is not the current ", personaSource.getId()));
    }

    // Validate distance
    float totalDest = 0;
    Position current = personaSource.getPosition();
    for (Position target : positions) {
      totalDest += roundToQuarter(current.dst(target));
      current = target;

      // Validate no collision.
      if (context.getMap().checkCollision(personaSource.getId(), target)) {
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
    attackRange.setPosition(positions.getLast().getX(), positions.getLast().getY());
    Rectangle target = personaTarget.getDefenceBoundingBox();

    if (!attackRange.overlaps(target)) {
      throw new NotAllowedException("Target is out of range");
    }
  }

  @Override
  public ActionResult apply(GameEntity gameEntity, DiceEngine diceEngine) {
    log.info("{} attack {}", personaSource.getName(), personaTarget.getName());

    personaSource.setPosition(positions.getLast());

    Attack attack = personaSource.getAttack();

    // Make the attack roll
    SkillResult result = skillTest(diceEngine, personaSource, attack.getAttack(), attack.getAttackFocus(), 1);
    boolean attackHit = result.computeTotal() >= personaTarget.getDefense();
    log.info("Attack hit: {} ({} >= {})", attackHit, result.computeTotal(), personaTarget.getDefense());

    // Do damage if attack succeeded
    int mitigatedDamage = 0;
    int totalDamage = 0;
    if (attackHit) {
      totalDamage = rollDamage(diceEngine, personaSource);
      log.info("Attack does {} raw damages", totalDamage);
      if (personaSource.getAttack().isPenetrating()) {
        mitigatedDamage = personaTarget.takePenetratingDamage(totalDamage);
      } else {
        mitigatedDamage = personaTarget.takeDamage(totalDamage);
      }
      log.info("Attack does {} mitigated damages", mitigatedDamage);
    }
    gameEntity.getEncounter().useMajorAction();

    return ChargeAttackActionResult.builder()
        .sourceId(sourceId)
        .targetId(targetId)
        .path(positions)
        .attackResult(result)
        .rawDamage(totalDamage)
        .mitigatedDamage(mitigatedDamage)
        .build();
  }

  private float roundToQuarter(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
