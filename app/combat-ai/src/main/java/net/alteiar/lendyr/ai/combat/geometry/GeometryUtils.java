package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GeometryUtils {

  /**
   * Find the closest position to join at 1 distance
   *
   * @param source      the source
   * @param target      the target
   * @param maxDistance the maximal distance allowed
   * @return the destination
   */
  public static Vector2 findClosestAt1Range(Vector2 source, Vector2 target, float maxDistance) {
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

  /**
   * Find the closest position at the desired range.
   *
   * @param source      the source position
   * @param target      the target position
   * @param maxDistance the maximal move distance allowed
   * @param range       the desired range
   * @return the target position
   */
  public static Vector2 findClosestAtRange(Vector2 source, Vector2 target, float maxDistance, float range) {
    float dist = source.dst(target);
    float moveDist = Math.min(dist - range, maxDistance);
    Vector2 direction = target.cpy().sub(source).nor();

    float newX = source.x + direction.x * moveDist;
    float newY = source.y + direction.y * moveDist;

    return new Vector2().set(roundPosition(newX), roundPosition(newY));
  }

  /**
   * Find the closest position at the desired range.
   *
   * @param source      the source position
   * @param target      the target position
   * @param maxDistance the maximal move distance allowed
   * @param range       the desired range
   * @return the target position
   */
  public static Vector2 findFarthestAtRange(Vector2 source, Vector2 target, float maxDistance, float range) {
    float dist = source.dst(target);
    float moveDist = Math.max(Math.min(maxDistance, range - dist), -maxDistance);
    Vector2 direction = source.cpy().sub(target).nor();

    float newX = source.x + direction.x * moveDist;
    float newY = source.y + direction.y * moveDist;

    return new Vector2().set(roundPosition(newX), roundPosition(newY));
  }

  public static float roundPosition(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
