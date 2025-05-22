package net.alteiar.lendyr.model.items;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.persona.Ability;
import net.alteiar.lendyr.model.persona.AbilityFocus;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weapon implements Item {
  private UUID id;
  private String itemType;
  private String name;
  private String description;
  private String icon;
  private String weaponGroup;
  private int damageDice;
  private int damageFixed;
  private int minStr;
  private int cost;
  private Ability ability;
  private AbilityFocus focus;
  private float normalRange;
  private float longRange;
  private boolean penetrating;
  private Ability damageAbility;

  private int encumbrance;
  private WeaponType attackType;
}
