package net.alteiar.lendyr.entity.action.combat;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.combat.NextCombatPersonaGameEvent;

import java.util.List;

public class EndTurnAction implements GameAction {

  @Override
  public void ensureAllowed(GameEntity context) {
    // Nothing to validate
  }

  @Override
  public List<GameEvent> apply(GameEntity gameEntity, DiceEngine diceEngine) {
    EncounterEntity encounter = gameEntity.getEncounter();
    encounter.endPlayerTurn();
    return List.of(new NextCombatPersonaGameEvent(encounter.getTurn(), encounter.getCurrentPersona().getPersonaId()));
  }
}
