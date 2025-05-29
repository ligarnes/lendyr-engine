package net.alteiar.lendyr.engine.random;

import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.SkillResult;

import java.util.Random;

public class DiceEngineImpl implements DiceEngine {
  private final Random random = new Random();

  @Override
  public SkillResult rollSkill(int bonus) {
    return SkillResult.builder().die1(rollD6()).die2(rollD6()).stunDie(rollD6()).bonus(bonus).build();
  }

  @Override
  public int rollDamage(int diceCount) {
    int total = 0;
    for (int i = 0; i < diceCount; i++) {
      total += rollD6();
    }
    return total;
  }

  private int rollD6() {
    return random.nextInt(1, 7);
  }
}
