package net.alteiar.lendyr.engine.action;

import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.action.result.GenericActionResult;

public class EndTurnAction implements GameAction {

  @Override
  public void ensureAllowed(GameContext context) {
    // Nothing to validate
  }

  public ActionResult apply(GameContext context) {
    context.getGame().getEncounter().endPlayerTurn();
    return GenericActionResult.builder().build();
  }
}
