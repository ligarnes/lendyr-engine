package net.alteiar.lendyr.engine;

import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.representation.DynamicPathfinding;
import net.alteiar.lendyr.engine.exploration.NpcExploration;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.event.exploration.PersonaPositionChanged;
import net.alteiar.lendyr.entity.event.exploration.RealtimeUpdateEvent;
import net.alteiar.lendyr.entity.npc.NpcEntity;
import net.alteiar.lendyr.model.persona.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
public class RealTimeEngine {
  private final GameContext gameContext;
  private final DynamicPathfinding pathfinding;
  private final NpcExploration npcExploration;

  public RealTimeEngine(GameContext gameContext) {
    this.gameContext = gameContext;
    this.pathfinding = new DynamicPathfinding(gameContext.getGame().getMap());
    npcExploration = new NpcExploration();
  }

  public void update(float delta) {
    pathfinding.update();

    // NPC play
    npcExploration.npcPlay(gameContext);

    // Update the world
    List<PersonaPositionChanged> positions = new ArrayList<>();
    positions.addAll(updatePlayerControlled(delta));
    positions.addAll(updateNpc(delta));

    RealtimeUpdateEvent.RealtimeUpdateEventBuilder eventBuilder = RealtimeUpdateEvent.builder();
    eventBuilder.positions(positions);
    gameContext.notifyEvent(eventBuilder.build());
  }

  private List<PersonaPositionChanged> updatePlayerControlled(float delta) {
    List<PersonaEntity> personaEntities = gameContext.getGame().getMap().getPcEntities();
    return personaEntities.stream().map(personaEntity -> movePersona(delta, personaEntity))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private List<PersonaPositionChanged> updateNpc(float delta) {
    List<NpcEntity> personaEntities = gameContext.getGame().getMap().getNpcEntities();
    return personaEntities.stream().map(npc -> movePersona(delta, npc.getPersona()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private PersonaPositionChanged convertPositionChanged(PersonaEntity persona) {
    return PersonaPositionChanged.builder().sourceId(persona.getId()).position(persona.getPosition()).nextPosition(persona.getNextPosition()).build();
  }

  public Optional<PersonaPositionChanged> movePersona(float delta, PersonaEntity personaEntity) {
    if (personaEntity.getNextPosition() != null || personaEntity.getTargetPosition() != null) {

      if (personaEntity.getNextPosition() == null) {
        computeNextPosition(personaEntity);
      }

      if (personaEntity.getNextPosition() != null) {
        float totalDistance = (personaEntity.getSpeed() / 3f) * delta;
        updatePosition(personaEntity, totalDistance);
        return Optional.of(convertPositionChanged(personaEntity));
      } else {
        log.info("No next position");
      }
    }
    return Optional.empty();
  }

  private void computeNextPosition(PersonaEntity personaEntity) {
    List<Position> positions = pathfinding.pathTo(personaEntity.getPosition(), personaEntity.getTargetPosition(), personaEntity.getSize(), 3);
    if (!positions.isEmpty()) {
      personaEntity.setNextPosition(positions.getFirst());
    } else {
      personaEntity.setNextPosition(null);
      personaEntity.setTargetPosition(null);
    }
  }

  private void updatePosition(PersonaEntity personaEntity, float distance) {
    if (personaEntity.getNextPosition() == null) {
      return;
    }

    int layer = personaEntity.getLayer();
    float nextX;
    float nextY;

    float distanceToNext = personaEntity.getNextPosition().dst(personaEntity.getPosition());

    if (distanceToNext <= distance) {
      // Reach target
      layer = personaEntity.getNextPosition().getLayer();
      nextX = personaEntity.getNextPosition().getX();
      nextY = personaEntity.getNextPosition().getY();
      personaEntity.setNextPosition(null);
    } else {
      float targetX = personaEntity.getNextPosition().getX();
      float targetY = personaEntity.getNextPosition().getY();

      // Keep the speed homogeneous whatever it's diagonal or straight
      float t = distance / personaEntity.getPosition().dst(personaEntity.getNextPosition());
      nextX = (1 - t) * personaEntity.getPosition().getX() + t * targetX;
      nextY = (1 - t) * personaEntity.getPosition().getY() + t * targetY;
    }

    personaEntity.setPosition(nextX, nextY, layer);
    if (Objects.equals(personaEntity.getTargetPosition(), personaEntity.getPosition())) {
      personaEntity.setTargetPosition(null);
    } else if (personaEntity.getNextPosition() == null && personaEntity.getTargetPosition() != null) {
      // use remaining of the move to continue in target destination if any
      computeNextPosition(personaEntity);
      updatePosition(personaEntity, distance - distanceToNext);
    }
  }
}
