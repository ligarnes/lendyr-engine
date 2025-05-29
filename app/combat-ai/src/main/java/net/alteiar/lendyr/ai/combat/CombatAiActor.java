package net.alteiar.lendyr.ai.combat;

import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;

public interface CombatAiActor {

  /**
   * Play the agent.
   *
   * @param persona    the character
   * @param gameEntity the game entity
   */
  TurnAction combatTurn(PersonaEntity persona, GameEntity gameEntity);
}
