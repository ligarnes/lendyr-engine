package net.alteiar.lendyr.model.encounter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {
  private int turn;
  private List<CombatActor> initiative;
  private CurrentPersona currentPersona;
}
