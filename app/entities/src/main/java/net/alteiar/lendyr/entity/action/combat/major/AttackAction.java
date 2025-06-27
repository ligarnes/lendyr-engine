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
import net.alteiar.lendyr.entity.action.BaseAction;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.combat.AttackGameEvent;
import net.alteiar.lendyr.model.persona.Attack;
import net.alteiar.lendyr.model.persona.AttackType;

import java.util.List;
import java.util.UUID;

@Log4j2
@ToString
public class AttackAction extends BaseAction implements MajorAction {
  @Getter
  private final UUID sourceId;
  @Getter
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
  public void ensureAllowed(GameEntity gameEntity) {
    personaSource = gameEntity.findById(sourceId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", sourceId)));
    personaTarget = gameEntity.findById(targetId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", targetId)));

    // Check if they are in range
    if (!personaSource.canAttack(personaTarget)) {
      throw new NotAllowedException("Target is out of range");
    }
  }

  @Override
  public List<GameEvent> apply(GameEntity gameEntity, DiceEngine diceEngine) {
    log.info("{} attack {}", personaSource.getName(), personaTarget.getName());

    Attack attack = personaSource.getAttack();
    Rectangle target = personaTarget.getDefenceBoundingBox().getRectangle();
    int rangeMalus = 0;
    if (AttackType.RANGE.equals(attack.getAttackType())) {
      rangeMalus = -2;
      Rectangle shortRange = personaSource.getAttackNormalBoundingBox().getRectangle();
      if (shortRange.overlaps(target)) {
        rangeMalus = 0;
      }
    }

    // Make the attack roll
    SkillResult result = skillTest(diceEngine, personaSource, attack.getAttack(), attack.getAttackFocus(), rangeMalus);
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

    return List.of(
        AttackGameEvent.builder()
            .sourceId(sourceId)
            .targetId(targetId)
            .attackResult(result)
            .rawDamage(totalDamage)
            .mitigatedDamage(mitigatedDamage)
            .hit(attackHit)
            .targetRemainingHp(personaTarget.getCurrentHealthPoint())
            .minorActionUsed(gameEntity.getEncounter().isMinorActionUsed())
            .majorActionUsed(gameEntity.getEncounter().isMajorActionUsed())
            .build()
    );
  }
}
