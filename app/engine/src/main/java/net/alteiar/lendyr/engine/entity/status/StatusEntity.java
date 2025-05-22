package net.alteiar.lendyr.engine.entity.status;


public interface StatusEntity {

  String getName();

  boolean isActive();

  StatusEffect getStatusEffect();

  int getBonus();

  void use();

  void newScene();

  void newTurn();
}
