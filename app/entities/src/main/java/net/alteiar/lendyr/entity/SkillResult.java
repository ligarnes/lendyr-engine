package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResult {
  private int die1;
  private int die2;
  private int stunDie;
  private int bonus;

  public boolean hasStun() {
    return die1 == die2 || die1 == stunDie || die2 == stunDie;
  }

  public int computeTotal() {
    return die1 + die2 + stunDie + bonus;
  }

  public int computeStun() {
    return hasStun() ? stunDie : 0;
  }
}
