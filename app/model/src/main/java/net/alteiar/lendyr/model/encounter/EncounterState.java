package net.alteiar.lendyr.model.encounter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncounterState {
  private int turn;
  private List<UUID> initiative;
  private CurrentPersona currentPersona;
}
