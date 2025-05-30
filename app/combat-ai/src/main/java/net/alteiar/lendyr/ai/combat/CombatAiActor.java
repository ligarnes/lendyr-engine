package net.alteiar.lendyr.ai.combat;

import net.alteiar.lendyr.entity.PersonaEntity;

public interface CombatAiActor {

  /**
   * Play the agent.
   *
   * @param persona the character
   */
  TurnAction combatTurn(PersonaEntity persona);
}
