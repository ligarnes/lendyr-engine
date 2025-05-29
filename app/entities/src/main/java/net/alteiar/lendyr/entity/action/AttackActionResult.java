package net.alteiar.lendyr.entity.action;

import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.entity.SkillResult;

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
