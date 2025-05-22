package net.alteiar.lendyr.engine.entity.status;

import lombok.Builder;
import lombok.Getter;

public class DurableStatusEntity implements StatusEntity {
  @Getter
  private String name;
  private final DurationType durationType;
  private int remainingTurn;
  private boolean isActive;

  @Getter
  private final StatusEffect statusEffect;

  @Getter
  private final int bonus;

  @Builder
  protected DurableStatusEntity(String name, DurationType durationType, int totalTurn, StatusEffect statusEffect, int bonus) {
    this.name = name;
    this.durationType = durationType;
    this.remainingTurn = totalTurn;
    this.statusEffect = statusEffect;
    this.bonus = bonus;
    isActive = true;
  }

  @Override
  public boolean isActive() {
    return isActive;
  }

  @Override
  public void use() {
    if (durationType == DurationType.ONCE) {
      this.isActive = false;
    }
  }


  @Override
  public void newTurn() {
    if (durationType == DurationType.SCENE) {
      // Nothing to do it applies to the whole scene
      return;
    }

    remainingTurn--;
    if (remainingTurn <= 0) {
      this.isActive = false;
    }
  }

  @Override
  public void newScene() {
    switch (durationType) {
      case SCENE:
      case TURN:
        this.isActive = false;
    }
  }
}
