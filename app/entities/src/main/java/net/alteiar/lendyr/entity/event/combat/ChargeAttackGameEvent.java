package net.alteiar.lendyr.entity.event.combat;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class ChargeAttackGameEvent implements GameEvent {
  @NonNull
  UUID sourceId;
  @NonNull
  UUID targetId;
  @NonNull
  List<Position> path;
  @NonNull
  SkillResult attackResult;
  int mitigatedDamage;
  int rawDamage;
  boolean hit;
  int targetRemainingHp;

  boolean minorActionUsed;
  boolean majorActionUsed;

  @Override
  public boolean hasWorldChanged() {
    return mitigatedDamage != 0 || !path.isEmpty();
  }
}
