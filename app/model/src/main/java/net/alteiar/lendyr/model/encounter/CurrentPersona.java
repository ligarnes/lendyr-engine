package net.alteiar.lendyr.model.encounter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPersona {
  private int initiativeIdx;
  private boolean majorActionUsed;
  private boolean minorActionUsed;
}
