package net.alteiar.lendyr.model.map.layered.element;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PolygonMapElement implements MapElement {
  private final String name;
  private final Polygon polygon;

  @Override
  public Rectangle getBoundingBox() {
    return polygon.getBoundingRectangle();
  }

  /// Four cases:
  ///
  /// - Rect is outside of Poly
  /// - Rect intersects Poly
  /// - Rect is inside of Poly
  /// - Poly is inside of Rect
  @Override
  public boolean checkCollision(Rectangle rect) {
    return IntersectUtils.intersect(polygon, rect);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Vertex: ").append("\n");
    for (int i = 0; i < polygon.getVertexCount(); i++) {
      sb.append(polygon.getVertex(i, new Vector2())).append(" -> ");
    }
    sb.append("---------");
    return sb.toString();
  }
}
