package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbilityStat {
  private boolean isPrimary;
  private int value;
  private List<AbilityFocus> focuses;

  public int getValue(AbilityFocus focus) {
    if (focuses != null && focuses.contains(focus)) {
      return value + 2;
    }
    return value;
  }
}
