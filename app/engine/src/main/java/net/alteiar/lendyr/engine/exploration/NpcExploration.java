package net.alteiar.lendyr.engine.exploration;

import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.npc.NpcEntity;
import net.alteiar.lendyr.model.npc.behavior.Patrol;
import net.alteiar.lendyr.model.npc.behavior.Static;
import net.alteiar.lendyr.model.persona.Position;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class NpcExploration {

  public void npcPlay(GameContext gameContext) {
    List<NpcEntity> npcs = gameContext.getGame().getMap().getNpcEntities().stream().filter(NpcEntity::isAlive).toList();

    List<NpcEntity> inCombat = triggerCombat(gameContext, npcs.stream().filter(NpcEntity::isEnemy).toList());
    if (!inCombat.isEmpty()) {
      // Trigger combat
      gameContext.startEncounter(inCombat);
      // Do not continue the processing
      return;
    }

    npcs.forEach(this::npcTakeDecision);
  }

  private List<NpcEntity> triggerCombat(GameContext gameContext, List<NpcEntity> enemies) {
    List<NpcEntity> inCombat = new ArrayList<>();
    // Find all enemy NPCs that are in range with the players
    for (NpcEntity npc : enemies) {
      if (triggerCombat(gameContext, npc)) {
        inCombat.add(npc);
      }
    }

    // Fight contagion (add enemy close to an enemy in combat)
    for (NpcEntity npc : enemies) {
      if (inCombat.contains(npc)) {
        continue;
      }
      if (combatContagion(inCombat, npc)) {
        inCombat.add(npc);
      }
    }

    return inCombat;
  }

  private boolean combatContagion(List<NpcEntity> inCombat, NpcEntity npcEntity) {
    Position currentPosition = npcEntity.getPersona().getPosition();
    for (NpcEntity personaEntity : inCombat) {
      Position targetPosition = personaEntity.getPersona().getPosition();
      if (currentPosition.isSameLayer(targetPosition)
          && currentPosition.dst(targetPosition) < 5f) {
        return true;
      }
    }

    return false;
  }

  private boolean triggerCombat(GameContext gameContext, NpcEntity npcEntity) {
    List<PersonaEntity> personaEntities = gameContext.getGame().getMap().getPcEntities();

    Position currentPosition = npcEntity.getPersona().getPosition();
    for (PersonaEntity personaEntity : personaEntities) {
      Position targetPosition = personaEntity.getPosition();
      if (currentPosition.isSameLayer(targetPosition)
          && currentPosition.dst(targetPosition) < 5f) {
        return true;
      }
    }

    return false;
  }

  private void npcTakeDecision(NpcEntity npcEntity) {
    move(npcEntity);
  }


  private void move(NpcEntity npcEntity) {
    switch (npcEntity.getBehavior()) {
      case Patrol patrol:
        patrol(npcEntity, patrol);
        break;
      case Static staticPosition:

        break;
      default:
        //log.info("Do nothing");
        break;
    }
  }

  private void patrol(NpcEntity npcEntity, Patrol patrol) {
    List<Position> path = patrol.getPositions();

    Position currentPosition = npcEntity.getPersona().getPosition();
    Position targetPosition = npcEntity.getPersona().getTargetPosition();

    if (targetPosition == null) {
      int patrolIdx = patrol.getPositions().indexOf(currentPosition);

      if (patrolIdx >= 0) {
        targetPosition = patrol.getPositions().get(patrolIdx);
      } else {
        // Default go to position 0
        targetPosition = patrol.getPositions().get(0);
      }
    }

    if (currentPosition.dst(targetPosition) < 0.5f) {
      int idx = path.indexOf(targetPosition);
      int nextIdx = (idx + 1) % path.size();
      npcEntity.getPersona().setTargetPosition(path.get(nextIdx));
      log.info("Patrol move to next position: {}", targetPosition);
    }
  }
}
