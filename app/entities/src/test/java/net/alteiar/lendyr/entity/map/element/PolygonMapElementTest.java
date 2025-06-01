package net.alteiar.lendyr.entity.map.element;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PolygonMapElementTest {

  @Test
  void checkCollision_convex() {
    Polygon polygon = new Polygon(new float[]{1, 0, 3, 0, 4, 2, 2, 3, 0, 2});
    polygon.setPosition(1, 1);
    PolygonMapElement polygonMapElement = PolygonMapElement.builder().polygon(polygon).build();

    // Contain other rectangles
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(1.5f, 2, 1, 1)));
    // Contained by other rectangles
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(0, 0, 8, 8)));
    // Intersect
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(0, 0, 2, 2)));
    // No collision
    Assertions.assertFalse(polygonMapElement.checkCollision(new Rectangle(6, 6, 2, 2)));
  }

  @Test
  void checkCollision_concave() {
    Polygon polygon = new Polygon(new float[]{0, 0, 1, 2, 3, 0, 3, 5, 0, 5});
    polygon.setPosition(1, 1);
    PolygonMapElement polygonMapElement = PolygonMapElement.builder().polygon(polygon).build();

    // Contain other rectangles
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(2f, 4, 1, 1)));
    // Contained by other rectangles
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(0, 0, 8, 8)));
    // Intersect
    Assertions.assertTrue(polygonMapElement.checkCollision(new Rectangle(2, 0, 3, 2)));
    // No collision
    Assertions.assertFalse(polygonMapElement.checkCollision(new Rectangle(6, 6, 2, 2)));
    Assertions.assertFalse(polygonMapElement.checkCollision(new Rectangle(2, 0, 0.5f, 2)));
  }
}