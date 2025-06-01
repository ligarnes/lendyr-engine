package net.alteiar.lendyr.model.map.tiled.object;

import com.badlogic.gdx.math.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledObject {

  public static enum TileObjectType {
    RECTANGLE, POLYGON, ELLIPSE, POLYLINE, OTHER
  }

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

  public TileObjectType getType() {
    if (polygon != null) {
      return TileObjectType.POLYGON;
    }
    if (ellipse != null) {
      return TileObjectType.ELLIPSE;
    }
    if (polyline != null) {
      return TileObjectType.POLYLINE;
    }
    if (width > 0 && height > 0) {
      return TileObjectType.RECTANGLE;
    }
    return TileObjectType.OTHER;
  }

  public Shape2D getShape() {
    if (polygon != null) {
      return getPolygon();
    }
    if (ellipse != null) {
      return getEllipse();
    }

    return getRectangle();
  }

  public Rectangle getRectangle() {
    return new Rectangle(x, y, width, height);
  }

  public Polygon getPolygon() {
    Polygon shape = polygon.getPolygon();
    shape.setPosition(x, y);
    return shape;
  }

  public Ellipse getEllipse() {
    return new Ellipse(x, y, width, height);
  }

  public List<Vector2> getPolyline() {
    return polyline.getPolyline().stream().map(poly -> poly.add(x, y)).toList();
  }

  public int getIntProperty(String property) {
    return properties.stream()
        .filter(p -> "int".equals(p.getType()))
        .filter(p -> p.getName().equals(property))
        .map(TiledCustomProperty::getValue)
        .map(Integer::valueOf)
        .findFirst().orElse(-1);
  }

  public boolean getBoolProperty(String property) {
    return properties.stream()
        .filter(p -> "bool".equals(p.getType()))
        .filter(p -> p.getName().equals(property))
        .map(TiledCustomProperty::getValue)
        .map(Boolean::valueOf)
        .findFirst().orElse(false);
  }
}
