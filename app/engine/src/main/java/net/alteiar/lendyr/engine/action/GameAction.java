package net.alteiar.lendyr.engine.action;

import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.entity.exception.NotSupportedException;

/**
 * Actions are not thread safe.
 */
public interface GameAction {

  /**
   * Validate if the action is permitted.
   */
  void ensureAllowed(GameContext context);

  /**
   * Apply the action.
   *
   * @param context the game context
   * @return the result of the action
   */
  default ActionResult apply(GameContext context) {
    throw new NotSupportedException("Action is not supported yet");
  }
}
