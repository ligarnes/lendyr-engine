package net.alteiar.lendyr.entity.action;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.action.exception.NotSupportedException;

/**
 * Actions are not thread safe.
 */
public interface GameAction {

  /**
   * Validate if the action is permitted.
   */
  void ensureAllowed(GameEntity gameEntity);

  /**
   * Apply the action.
   *
   * @param gameEntity the game entity
   * @return the result of the action
   */
  default ActionResult apply(GameEntity gameEntity, DiceEngine diceEngine) {
    throw new NotSupportedException("Action is not supported yet");
  }
}
