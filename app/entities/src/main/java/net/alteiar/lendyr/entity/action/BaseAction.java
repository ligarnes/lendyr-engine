package net.alteiar.lendyr.entity.action;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.model.persona.Ability;
import net.alteiar.lendyr.model.persona.AbilityFocus;
import net.alteiar.lendyr.model.persona.AbilityStat;
import net.alteiar.lendyr.model.persona.Attack;

import java.util.Objects;

public abstract class BaseAction implements GameAction {

  protected int rollDamage(DiceEngine diceEngine, PersonaEntity persona) {
    Attack attack = persona.getAttack();
    int totalDamage = diceEngine.rollDamage(attack.getDiceCount()) + attack.getFixedDamage();
    totalDamage += persona.getAbility(attack.getDamageBonus()).getValue();
    return totalDamage;
  }

  protected SkillResult skillTest(DiceEngine diceEngine, PersonaEntity persona, Ability ability, AbilityFocus focus, int otherBonus) {
    if (!ability.getFocusList().contains(focus)) {
      throw new NotAllowedException(String.format("The focus [%s] is not part of the ability [%s]", ability.name(), focus.name()));
    }
    return rollAbility(diceEngine, persona.getAbility(ability), focus, otherBonus);
  }

  private SkillResult rollAbility(DiceEngine diceEngine, AbilityStat abilityStat, AbilityFocus focus, int otherBonus) {
    int focusBonus = getFocus(abilityStat, focus);

    int totalBonus = abilityStat.getValue() + focusBonus + otherBonus;
    return diceEngine.rollSkill(totalBonus);
  }

  private int getFocus(AbilityStat abilityStat, AbilityFocus focus) {
    if (Objects.isNull(abilityStat.getFocuses())) {
      return 0;
    }
    return abilityStat.getFocuses().contains(focus) ? 2 : 0;
  }
}
