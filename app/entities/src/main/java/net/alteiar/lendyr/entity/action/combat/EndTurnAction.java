package net.alteiar.lendyr.entity.action.combat;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.combat.NextCombatPersonaGameEvent;

public class EndTurnAction implements GameAction {

  @Override
  public void ensureAllowed(GameEntity context) {
    // Nothing to validate
  }

  @Override
  public GameEvent apply(GameEntity gameEntity, DiceEngine diceEngine) {
    EncounterEntity encounter = gameEntity.getEncounter();
    encounter.endPlayerTurn();
    return new NextCombatPersonaGameEvent(encounter.getTurn(), encounter.getCurrentPersona().getPersonaId());
  }
}
