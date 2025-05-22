package net.alteiar.lendyr.engine.action.major;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.action.GameAction;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.action.result.AttackActionResult;
import net.alteiar.lendyr.engine.entity.PersonaEntity;
import net.alteiar.lendyr.engine.entity.exception.NotAllowedException;
import net.alteiar.lendyr.engine.entity.exception.NotFoundException;
import net.alteiar.lendyr.engine.random.SkillResult;
import net.alteiar.lendyr.model.persona.Attack;
import net.alteiar.lendyr.model.persona.AttackType;

import java.util.UUID;

@Log4j2
public class AttackAction implements GameAction {
  @NonNull
  private final UUID sourceId;
  @NonNull
  private final UUID targetId;

  // Stateful variables
  private PersonaEntity personaSource;
  private PersonaEntity personaTarget;

  @Builder
  public AttackAction(@NonNull UUID sourceId, @NonNull UUID targetId) {
    this.sourceId = sourceId;
    this.targetId = targetId;
  }

  @Override
  public void ensureAllowed(GameContext context) {
    personaSource = context.getGame().findById(sourceId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", sourceId)));
    personaTarget = context.getGame().findById(targetId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", targetId)));

    // Check if they are in range
    Rectangle attackRange = personaSource.getAttackLongBoundingBox();
    Rectangle target = personaTarget.getDefenceBoundingBox();

    if (!attackRange.overlaps(target)) {
      throw new NotAllowedException("Target is out of range");
    }
  }

  @Override
  public ActionResult apply(GameContext context) {
    log.info("{} attack {}", personaSource.getName(), personaTarget.getName());

    Attack attack = personaSource.getAttack();
    Rectangle target = personaTarget.getDefenceBoundingBox();
    int rangeMalus = 0;
    if (AttackType.RANGE.equals(attack.getAttackType())) {
      rangeMalus = -2;
      Rectangle shortRange = personaSource.getAttackNormalBoundingBox();
      if (shortRange.overlaps(target)) {
        rangeMalus = 0;
      }
    }

    // Make the attack roll
    SkillResult result = personaSource.skillTest(attack.getAttack(), attack.getAttackFocus(), rangeMalus);
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
}
