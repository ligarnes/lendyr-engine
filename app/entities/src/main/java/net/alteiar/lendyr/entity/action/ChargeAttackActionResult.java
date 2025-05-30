package net.alteiar.lendyr.entity.action;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.entity.SkillResult;

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
  List<Vector2> path;
  @NonNull
  SkillResult attackResult;
  int mitigatedDamage;
  int rawDamage;

  @Override
  public boolean hasWorldChanged() {
    return mitigatedDamage != 0 || !path.isEmpty();
  }
}
