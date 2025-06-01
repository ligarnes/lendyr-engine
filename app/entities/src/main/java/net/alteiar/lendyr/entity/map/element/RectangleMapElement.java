package net.alteiar.lendyr.entity.map.element;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
public class RectangleMapElement implements MapElement {
  @Getter
  private final String name;
  private final Rectangle rectangle;

  @Override
  public boolean checkCollision(Rectangle rect) {
    return rectangle.overlaps(rect)
        || rect.contains(rectangle)
        || rectangle.contains(rect);
  }
}
