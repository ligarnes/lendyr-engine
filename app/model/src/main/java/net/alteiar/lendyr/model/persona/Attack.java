package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attack {
  private AttackType attackType;
  private float longRange;
  private float normalRange;
  private Ability attack;
  private AbilityFocus attackFocus;
  private int diceCount;
  private int fixedDamage;
  private Ability damageBonus;
  private boolean penetrating;
}
