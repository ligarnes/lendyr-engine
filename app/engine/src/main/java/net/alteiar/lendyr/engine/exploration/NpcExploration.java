package net.alteiar.lendyr.engine.exploration;

import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.npc.NpcEntity;
import net.alteiar.lendyr.model.npc.behavior.Patrol;
import net.alteiar.lendyr.model.npc.behavior.Static;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;

@Log4j2
public class NpcExploration {

  public void npcTakeDecision(NpcEntity npcEntity) {
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

  public void patrol(NpcEntity npcEntity, Patrol patrol) {
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
