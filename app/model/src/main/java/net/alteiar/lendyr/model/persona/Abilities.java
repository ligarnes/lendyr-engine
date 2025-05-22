package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describe the abilities of a character, NPC or Monster.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Abilities {
  private AbilityStat accuracy;
  private AbilityStat communication;
  private AbilityStat constitution;
  private AbilityStat dexterity;
  private AbilityStat fighting;
  private AbilityStat intelligence;
  private AbilityStat perception;
  private AbilityStat strength;
  private AbilityStat willpower;
}
