package net.alteiar.lendyr.engine;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.ai.combat.CombatAiSelector;
import net.alteiar.lendyr.ai.combat.TurnAction;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.combat.EndTurnAction;

@Log4j2
public class PersonaEngine {
  private final CombatAiSelector combatAiSelector;
  private final GameContext gameContext;

  @Builder
  PersonaEngine(@NonNull GameContext gameContext) {
    this.gameContext = gameContext;
    combatAiSelector = CombatAiSelector.builder().build();
  }

  public void playTurn(PersonaEntity persona) {
    log.info("Playing turn for {}", persona.getName());
    TurnAction turnAction = combatAiSelector.combatTurn(persona, gameContext.getGame());

    if (turnAction.getActionOrder() == TurnAction.ActionOrder.MAJOR_FIRST) {
      act(turnAction.getMajorAction());
      act(turnAction.getMinorAction());
    } else {
      act(turnAction.getMinorAction());
      act(turnAction.getMajorAction());
    }
    act(new EndTurnAction());
  }

  private void act(GameAction action) {
    if (action != null) {
      try {
        gameContext.act(action);
      } catch (RuntimeException e) {
        log.warn("AI try unauthorized action: {}", action, e);
      }
    }
  }
}
