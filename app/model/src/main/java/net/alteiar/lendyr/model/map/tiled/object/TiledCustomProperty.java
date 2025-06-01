package net.alteiar.lendyr.model.map.tiled.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledCustomProperty {
  private String name;
  private String type;
  private String value;
}
