package net.alteiar.lendyr.model.map.element;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
public class RectangleMapElement implements MapElement {
  private final String name;
  private final Rectangle rectangle;

  public Rectangle getBoundingBox() {
    return rectangle;
  }

  @Override
  public boolean checkCollision(Rectangle rect) {
    return rectangle.overlaps(rect);
  }
}
