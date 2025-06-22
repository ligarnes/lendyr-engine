package net.alteiar.lendyr.model.map.layered.element;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;


@Getter
public class LineMapElement implements MapElement {
  private final String name;
  private final List<Vector2> points;
  private final Rectangle boundingBox;

  @Builder
  public LineMapElement(@NonNull String name, @NonNull List<Vector2> points) {
    this.name = name;
    this.points = points;

    // Compute bounding box
    float minX = Integer.MAX_VALUE;
    float maxX = Integer.MIN_VALUE;
    float minY = Integer.MAX_VALUE;
    float maxY = Integer.MIN_VALUE;
    for (Vector2 point : points) {
      if (point.x < minX) {
        minX = point.x;
      }
      if (point.x > maxX) {
        maxX = point.x;
      }
      if (point.y < minY) {
        minY = point.y;
      }
      if (point.y > maxY) {
        maxY = point.y;
      }
    }
    boundingBox = new Rectangle(minX, minY, maxX - minX, maxY - minY);
  }

  @Override
  public boolean checkCollision(Rectangle rect) {
    for (int i = 0; i < points.size() - 1; i++) {
      Vector2 start = points.get(i);
      Vector2 end = points.get(i + 1);
      if (Intersector.intersectSegmentRectangle(start, end, rect)) {
        return true;
      }
    }
    return false;
  }
}
