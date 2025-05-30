package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResult {
  private int dice1;
  private int dice2;
  private int stunDie;
  private int bonus;

  public boolean hasStun() {
    return dice1 == dice2 || dice1 == stunDie || dice2 == stunDie;
  }

  public int computeTotal() {
    return dice1 + dice2 + stunDie + bonus;
  }

  public int computeStun() {
    return hasStun() ? stunDie : 0;
  }
}
