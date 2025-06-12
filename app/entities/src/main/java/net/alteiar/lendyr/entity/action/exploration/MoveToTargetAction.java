package net.alteiar.lendyr.entity.action.exploration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.model.persona.Position;

import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class MoveToTargetAction implements GameAction {
  @Getter
  private final UUID characterId;
  @Getter
  private final Position targetPosition;

  // Stateful variables
  PersonaEntity persona;

  @Builder
  public MoveToTargetAction(@NonNull UUID characterId, Position targetPosition) {
    this.characterId = characterId;
    this.targetPosition = targetPosition;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    if (targetPosition == null) {
      throw new NotAllowedException("Move action requires a target position");
    }

    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));
  }

  @Override
  public GameEvent apply(GameEntity gameEntity, DiceEngine diceEngine) {
    // Move to last position
    log.info("Set target position to {}", targetPosition);
    persona.setTargetPosition(targetPosition);
    return null;
  }
}
