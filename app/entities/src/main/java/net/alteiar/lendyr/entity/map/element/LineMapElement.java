package net.alteiar.lendyr.entity.map.element;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
public class LineMapElement implements MapElement {
  private final List<Vector2> points;

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
