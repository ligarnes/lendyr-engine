package net.alteiar.lendyr.model.map.element;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntersectUtils {

  public static boolean intersect(Polygon polygon, Rectangle rect) {
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
}
