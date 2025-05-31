package net.alteiar.lendyr.persistence.dao.tiled.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledObject {
  int id;
  String name;
  float x;
  float y;
  float width;
  float height;
  private List<TiledCustomProperty> properties;
  private TiledPolyline polyline;
  private TiledPolygon polygon;
  private TiledEllipse ellipse;
}
