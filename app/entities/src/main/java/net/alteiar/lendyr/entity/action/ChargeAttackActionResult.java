package net.alteiar.lendyr.entity.action;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class ChargeAttackActionResult implements ActionResult {
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

  @Override
  public boolean hasWorldChanged() {
    return mitigatedDamage != 0 || !path.isEmpty();
  }
}
