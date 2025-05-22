package net.alteiar.lendyr.engine.entity.status;


import net.alteiar.lendyr.engine.entity.exception.NotAllowedException;

public class StatusFactory {

  public static StatusEntity boost(int amount) {
    if (amount < 1 || amount > 3) {
      throw new NotAllowedException("The boost bonus must be between 1 and 3");
    }
    return DurableStatusEntity.builder().name("boost").durationType(DurationType.TURN).totalTurn(1).statusEffect(StatusEffect.SKILL).bonus(amount).build();
  }

  public static StatusEntity defensiveStance() {
    return DurableStatusEntity.builder().name("defensive stance").durationType(DurationType.TURN).totalTurn(1).statusEffect(StatusEffect.DEFENSE).bonus(4).build();
  }

  public static StatusEntity crushingBlow() {
    return DurableStatusEntity.builder().name("crushing blow").durationType(DurationType.SCENE).statusEffect(StatusEffect.ARMOR).bonus(-2).build();
  }
}
