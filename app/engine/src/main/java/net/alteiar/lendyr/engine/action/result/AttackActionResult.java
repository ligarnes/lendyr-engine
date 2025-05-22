package net.alteiar.lendyr.engine.action.result;

import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.engine.random.SkillResult;

@Getter
@Builder
public class AttackActionResult implements ActionResult {
  private SkillResult attackResult;
  private int mitigatedDamage;
  private int rawDamage;

  @Override
  public boolean hasWorldChanged() {
    return mitigatedDamage != 0;
  }
}
