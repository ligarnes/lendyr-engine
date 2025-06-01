package net.alteiar.lendyr.model.map.element;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PolygonMapElement implements MapElement {
  @Getter
  private final String name;

  private final Polygon polygon;

  /// Four cases:
  ///
  /// - Rect is outside of Poly
  /// - Rect intersects Poly
  /// - Rect is inside of Poly
  /// - Poly is inside of Rect
  @Override
  public boolean checkCollision(Rectangle rect) {
    if (!polygon.getBoundingRectangle().overlaps(rect)) {
      return false;
    }

    // any polygon point is in rect -> Poly is inside of Rect or Rect intersects Poly
    if (rect.contains(polygon.getTransformedVertices()[0], polygon.getTransformedVertices()[1])) {
      return true;
    }

    // any rectangle point is in the polygon -> rect is inside of poly
    if (polygon.contains(rect.x, rect.y)) {
      return true;
    }

    // Check each vertex collision with rectangle
    for (int i = 0; i < polygon.getVertexCount(); i++) {
      if (Intersector.intersectSegmentRectangle(
          polygon.getTransformedVertices()[i], polygon.getTransformedVertices()[i + 1],
          polygon.getTransformedVertices()[i + 2], polygon.getTransformedVertices()[i + 2 + 1],
          rect)) {
        return true;
      }
    }

    return false;
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
