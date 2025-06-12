package net.alteiar.lendyr.algorithm.representation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.movement.Pathfinding;
import net.alteiar.lendyr.model.map.LayeredMapWithMovable;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class DynamicPathfinding {
  private final LayeredMapWithMovable map;
  private Pathfinding pathfinding;

  public DynamicPathfinding(LayeredMapWithMovable map) {
    this.map = map;
  }

  private void load() {
    if (pathfinding != null) {
      return;
    }
    if (map.getLayeredMap() != null) {
      MultiLayerNetwork multiLayerNetwork = MultiLayerNetworkFactory.generate(map.getLayeredMap());
      pathfinding = Pathfinding.builder().layeredMap(multiLayerNetwork).build();
    }
  }

  public void update() {
    load();
    if (pathfinding != null) {
      pathfinding.getMultiLayerNetwork().reset();
      this.map.getMovableObjects().forEach(rectangle -> {
        int startX = MathUtils.floor(rectangle.getRectangle().getX());
        int startY = MathUtils.floor(rectangle.getRectangle().getY());
        int width = MathUtils.ceil(rectangle.getRectangle().getX() + rectangle.getRectangle().getWidth());
        int height = MathUtils.ceil(rectangle.getRectangle().getY() + rectangle.getRectangle().getHeight());
        for (int x = startX; x < width; x++) {
          for (int y = startY; y < height; y++) {
            pathfinding.getMultiLayerNetwork().addObstacle(x, y, rectangle.getLayer());
          }
        }
      });
    }
  }

  public List<Position> fleeFrom(Position position, Position target, float maxDistance) {
    return pathfinding.fleePath(position, target, maxDistance);
  }

  public List<Position> pathTo(Position source, Position end, Size size, float distance) {
    clampToWorld(end, size);

    log.info("Move from {} to {}", source, end);
    return pathfinding.computePath(source, end, distance);
  }

  private Position clampToWorld(Position target, Size size) {
    float personaWidth = size.getWidth();
    float personaHeigth = size.getHeight();
    target.setX(MathUtils.round(MathUtils.clamp(target.getX(), 0, map.getLayeredMap().getWidth() - personaWidth)));
    target.setY(MathUtils.round(MathUtils.clamp(target.getY(), 0, map.getLayeredMap().getHeight() - personaHeigth)));
    return target;
  }


  public void debug(List<Position> path) {
    Map<Vector2, String> mapDebug = new HashMap<>();

    map.getMovableObjects().forEach(obstacle -> {
      int startX = MathUtils.floor(obstacle.getRectangle().getX());
      int startY = MathUtils.floor(obstacle.getRectangle().getY());
      int width = MathUtils.ceil(obstacle.getRectangle().getX() + obstacle.getRectangle().getWidth());
      int height = MathUtils.ceil(obstacle.getRectangle().getY() + obstacle.getRectangle().getHeight());
      for (int x = startX; x < width; x++) {
        for (int y = startY; y < height; y++) {
          mapDebug.put(new Vector2(x, y), "a");
        }
      }
    });
    path.forEach(p -> mapDebug.put(p.toVector(), "o"));
    map.getLayeredMap().debugPath(mapDebug);
  }
}
