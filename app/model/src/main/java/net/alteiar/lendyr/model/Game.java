package net.alteiar.lendyr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.encounter.Encounter;
import net.alteiar.lendyr.model.map.LocalMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
  private Encounter encounter;
  private LocalMap localMap;
  private Player player;
  private PlayState playState;
}
