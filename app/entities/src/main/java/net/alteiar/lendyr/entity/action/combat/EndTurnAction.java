package net.alteiar.lendyr.entity.action.combat;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.action.ActionResult;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.GenericActionResult;

public class EndTurnAction implements GameAction {

  @Override
  public void ensureAllowed(GameEntity context) {
    // Nothing to validate
  }

  @Override
  public ActionResult apply(GameEntity gameEntity, DiceEngine diceEngine) {
    gameEntity.getEncounter().endPlayerTurn();
    return GenericActionResult.builder().build();
  }
}
