package net.alteiar.lendyr.ai.combat;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.entity.action.combat.major.MajorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MinorAction;

@Data
@Builder
public class TurnAction {
  private MajorAction majorAction;
  private MinorAction minorAction;
  private ActionOrder actionOrder;

  public static enum ActionOrder {
    MAJOR_FIRST, MINOR_FIRST
  }
}
