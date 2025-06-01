package net.alteiar.lendyr.model.map.element;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RectangleMapElementTest {

  @Test
  void checkCollision() {
    RectangleMapElement rectangleMapElement = RectangleMapElement.builder().rectangle(new Rectangle(1, 1, 4, 4)).build();

    // Contain other rectangles
    Assertions.assertTrue(rectangleMapElement.checkCollision(new Rectangle(2, 2, 1, 1)));
    // Contained by other rectangles
    Assertions.assertTrue(rectangleMapElement.checkCollision(new Rectangle(0, 0, 8, 8)));
    // Intersect
    Assertions.assertTrue(rectangleMapElement.checkCollision(new Rectangle(0, 0, 2, 2)));
    // No collision
    Assertions.assertFalse(rectangleMapElement.checkCollision(new Rectangle(5, 5, 2, 2)));
  }
}