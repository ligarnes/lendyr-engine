package net.alteiar.lendyr.model.map.tiled.object;

import com.badlogic.gdx.math.Polygon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledPolygon {
  private String points;

  public Polygon getPolygon() {
    return new Polygon(PropertyUtils.readPoints(points));
  }

}
