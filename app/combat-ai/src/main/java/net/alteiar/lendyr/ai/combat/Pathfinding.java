package net.alteiar.lendyr.ai.combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.astar.AStarAlgorithm;
import net.alteiar.lendyr.algorithm.battlemap.GenerateGrid;
import net.alteiar.lendyr.algorithm.battlemap.GridNetwork;
import net.alteiar.lendyr.algorithm.battlemap.Tile;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@UtilityClass
public class Pathfinding {

  public static Vector2 clampToWorld(GameEntity game, PersonaEntity persona, Vector2 target) {
    float personaWidth = persona.getSize().getWidth();
    float personaHeigth = persona.getSize().getHeight();
    target.x = MathUtils.clamp(target.x, personaWidth, game.getMap().getWidth() - personaWidth);
    target.y = MathUtils.clamp(target.y, personaHeigth, game.getMap().getHeight() - personaHeigth);
    return target;
  }

  /**
   * Compute the path from a point to another.
   * The path is straight and won't consider obstacles.
   *
   * @param game   the game entity
   * @param entity the entity moving
   * @param end    end position
   * @return the path
   */
  public static List<Vector2> computePath(GameEntity game, PersonaEntity entity, Vector2 end) {
    clampToWorld(game, entity, end);
    GridNetwork gridNetwork = GenerateGrid.generate(game.getMap());
    AStarAlgorithm<GridNetwork, Tile> astar = new AStarAlgorithm<>(gridNetwork);

    log.info("Starting path finding");
    astar.solve(gridNetwork.find(Math.round(entity.getPosition().x), Math.round(entity.getPosition().y)), gridNetwork.find(Math.round(end.x), Math.round(end.y)));


    float distance = 0;
    Vector2 currentPosition = entity.getPosition();
    List<Vector2> path = new ArrayList<>();
    for (int i = astar.getPath().size() - 2; i >= 0; i--) {

      Vector2 newPosition = new Vector2(astar.getPath().get(i).getX(), astar.getPath().get(i).getY());
      distance += currentPosition.dst(newPosition);
      if (distance > entity.getMoveDistance()) {
        log.info("Move too far ({}) at {} ", distance, newPosition);
        break;
      }
      log.info("Add position to path: {}", newPosition);
      path.add(newPosition);
      currentPosition = newPosition;
    }

    return path;
  }

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
