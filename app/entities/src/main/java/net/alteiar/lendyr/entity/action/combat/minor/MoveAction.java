package net.alteiar.lendyr.entity.action.combat.minor;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotEnoughActionException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.combat.MoveGameEvent;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class MoveAction implements MinorAction {
  @Getter
  private final UUID characterId;
  @Getter
  private final List<Position> positions;

  // Stateful variables
  PersonaEntity persona;

  @Builder
  public MoveAction(@NonNull UUID characterId, @NonNull List<Position> positions) {
    this.characterId = characterId;
    this.positions = positions;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    if (positions.isEmpty()) {
      throw new NotAllowedException("Move action requires at least one position");
    }

    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    if (context.getEncounter().isMinorActionUsed()) {
      throw new NotEnoughActionException(String.format("the persona with id [%s] has already consumed the minor action", characterId));
    }
    UUID currentPersonaId = context.getEncounter().getCurrentPersona().getPersonaId();
    if (!Objects.equals(currentPersonaId, characterId)) {
      throw new NotAllowedException(String.format("the persona with id [%s] is not the current ", characterId));
    }

    // Validate distance
    float totalDest = 0;
    Position current = persona.getPosition();
    for (Position target : positions) {
      totalDest += roundToQuarter(current.dst(target));
      // Validate no collision.
      if (context.getMap().checkCollision(characterId, target)) {
        log.warn("Collision of {} at {} from {}", persona.getName(), current, persona.getPosition());
        throw new NotAllowedException("the move is illegal because of obstacle");
      }
      current = target;
    }

    if (totalDest > persona.getMoveDistance()) {
      log.info("Invalid distance: {} < {} for {}", persona.getMoveDistance(), totalDest, positions);
      throw new NotAllowedException(
          String.format("the persona with id [%s] cannot move this far; distance: %.1f ; max distance: %.1f ", characterId, totalDest, persona.getMoveDistance()));
    }
  }

  @Override
  public GameEvent apply(GameEntity gameEntity, DiceEngine diceEngine) {
    // Move to last position
    log.info("Move {} to {}", characterId, positions);
    persona.setPosition(positions.getLast());
    gameEntity.getEncounter().useMinorAction();

    return MoveGameEvent.builder()
        .sourceId(characterId)
        .path(positions)
        .minorActionUsed(gameEntity.getEncounter().isMinorActionUsed())
        .majorActionUsed(gameEntity.getEncounter().isMajorActionUsed())
        .build();
  }

  private float roundToQuarter(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
