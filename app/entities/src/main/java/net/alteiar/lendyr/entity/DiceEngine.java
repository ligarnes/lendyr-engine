package net.alteiar.lendyr.entity;

public interface DiceEngine {
  SkillResult rollSkill(int bonus);

  int rollDamage(int diceCount);
}
