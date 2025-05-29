package net.alteiar.lendyr.ai.combat;

import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class Pathfinding {

  /**
   * Compute the path from a point to another.
   * The path is straight and won't consider obstacles.
   *
   * @param start start position
   * @param end   end position
   */
  public static void computePath(Vector2 start, Vector2 end, List<Vector2> path) {
    Vector2 currentPoint = start;

    while (!Objects.equals(currentPoint, end)) {
      currentPoint = nextPosition(currentPoint, end);
      path.add(currentPoint);
    }
  }

  private static Vector2 nextPosition(Vector2 current, Vector2 target) {
    if (current.x > target.x) {
      if (current.y > target.y) {
        float nextX = current.x - Math.min(1, current.x - target.x);
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2().set(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x - Math.min(1, current.x - target.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2().set(nextX, nextY);
      } else {
        float nextX = current.x - Math.min(1, current.x - target.x);
        return new Vector2().set(nextX, current.y);
      }
    } else if (current.x < target.x) {
      if (current.y > target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2().set(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2().set(nextX, nextY);
      } else {
        float nextX = current.x + Math.min(1, target.x - current.x);
        return new Vector2().set(nextX, current.y);
      }
    } else {
      if (current.y > target.y) {
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2().set(current.x, nextY);
      } else if (current.y < target.y) {
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2().set(current.x, nextY);
      }
    }

    return new Vector2().set(target.x, target.y);
  }
}
