package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

public class Geometry {

  @Test
  public void test() {
    Vector2 source = new Vector2(0, 0);
    Vector2 target = new Vector2(0, 19);
    float maxDistance = 8f;
    float range = 12f;

    // When
    Vector2 dest = findClosestAtRange(source, target, maxDistance, range);
    //Vector2 dest = findClosestAt1Range(source, target, maxDistance);

    // Then
    System.out.println("Move dist: " + source.dst(dest));
    System.out.println("original dist: " + source.dst(target));
    System.out.println("Destination: " + dest);

  }

  private Vector2 findClosestAt1Range(Vector2 source, Vector2 target, float maxDistance) {
    Vector2 realTarget = new Vector2();
    if (Math.abs(target.x - source.x) < Math.abs(target.y - source.y)) {
      float direction = target.y - source.y > 0 ? 1 : -1;
      realTarget.set(target.x, target.y - direction);
    } else {
      float direction = target.x - source.x > 0 ? 1 : -1;
      realTarget.set(target.x - direction, target.y);
    }

    return findClosestAtRange(source, realTarget, maxDistance, 0f);
  }

  private Vector2 findClosestAtRange(Vector2 source, Vector2 target, float maxDistance, float range) {
    float dist = source.dst(target);
    float moveDist = Math.min(dist - range, maxDistance);
    Vector2 direction = target.cpy().sub(source).nor();

    float newX = source.x + direction.x * moveDist;
    float newY = source.y + direction.y * moveDist;

    return new Vector2().set(roundPosition(newX), roundPosition(newY));
  }

  private float roundPosition(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
