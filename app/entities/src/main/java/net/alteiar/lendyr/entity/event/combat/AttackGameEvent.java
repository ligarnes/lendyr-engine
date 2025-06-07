package net.alteiar.lendyr.entity.event.combat;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.event.GameEvent;

import java.util.UUID;

@Value
@Builder
public class AttackGameEvent implements GameEvent {
  @NonNull
  UUID sourceId;
  @NonNull
  UUID targetId;
  @NonNull
  SkillResult attackResult;
  int mitigatedDamage;
  int rawDamage;
  boolean hit;
  int targetRemainingHp;

  @Override
  public boolean hasWorldChanged() {
    return mitigatedDamage != 0;
  }
}
