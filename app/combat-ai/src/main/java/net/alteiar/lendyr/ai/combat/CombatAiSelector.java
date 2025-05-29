package net.alteiar.lendyr.ai.combat;

import lombok.Builder;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;

public class CombatAiSelector implements CombatAiActor {

  private final MeleeCombatStrategy meleeStrategy;
  private final RangeCombatStrategy rangeStrategy;

  @Builder
  CombatAiSelector() {
    meleeStrategy = new MeleeCombatStrategy();
    rangeStrategy = new RangeCombatStrategy();
  }

  @Override
  public TurnAction combatTurn(PersonaEntity persona, GameEntity gameEntity) {
    return switch (persona.getAttack().getAttackType()) {
      case MELEE -> meleeStrategy.combatTurn(persona, gameEntity);
      case RANGE, MAGIC -> rangeStrategy.combatTurn(persona, gameEntity);
    };
  }
}
