package net.alteiar.lendyr.ai.combat;

import lombok.Builder;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;

public class CombatAiSelector implements CombatAiActor {

  private final MeleeCombatStrategy meleeStrategy;
  private final RangeCombatStrategy rangeStrategy;

  @Builder
  CombatAiSelector(GameEntity game) {
    meleeStrategy = new MeleeCombatStrategy(game);
    rangeStrategy = new RangeCombatStrategy(game);
  }

  @Override
  public TurnAction combatTurn(PersonaEntity persona) {
    return switch (persona.getAttack().getAttackType()) {
      case MELEE -> meleeStrategy.combatTurn(persona);
      case RANGE, MAGIC -> rangeStrategy.combatTurn(persona);
    };
  }
}
