package net.alteiar.lendyr.ai.combat;

import lombok.Builder;
import net.alteiar.lendyr.ai.combat.geometry.PersonaWorldRepresentation;
import net.alteiar.lendyr.entity.GameEntityImpl;
import net.alteiar.lendyr.entity.PersonaEntity;

public class CombatAiSelector implements CombatAiActor {

  private final PersonaWorldRepresentation personaWorldRepresentation;
  private final MeleeCombatStrategy meleeStrategy;
  private final RangeCombatStrategy rangeStrategy;

  @Builder
  CombatAiSelector(GameEntityImpl game) {
    personaWorldRepresentation = new PersonaWorldRepresentation(game.getMap());
    meleeStrategy = new MeleeCombatStrategy(game, personaWorldRepresentation);
    rangeStrategy = new RangeCombatStrategy(game, personaWorldRepresentation);
  }

  @Override
  public TurnAction combatTurn(PersonaEntity persona) {
    personaWorldRepresentation.update();
    return switch (persona.getAttack().getAttackType()) {
      case MELEE -> meleeStrategy.combatTurn(persona);
      case RANGE, MAGIC -> rangeStrategy.combatTurn(persona);
    };
  }
}
