package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.battlemap.MultiLayerNetwork;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.map.WorldMap;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class PersonaWorldRepresentation {
  private final WorldMap map;
  private Pathfinding pathfinding;

  public PersonaWorldRepresentation(WorldMap map) {
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
    if (map.getLayeredMap() != null) {
      pathfinding.getMultiLayerNetwork().reset();
      this.map.getMovableObjects().forEach(rectangle -> {
        int startX = MathUtils.floor(rectangle.getX());
        int startY = MathUtils.floor(rectangle.getY());
        int width = MathUtils.ceil(rectangle.getX() + rectangle.getWidth());
        int height = MathUtils.ceil(rectangle.getY() + rectangle.getHeight());
        for (int x = startX; x < width; x++) {
          for (int y = startY; y < height; y++) {
            pathfinding.getMultiLayerNetwork().addObstacle(x, y);
          }
        }
      });
    }
  }

  public List<Position> fleeFrom(PersonaEntity entity, Position target) {
    return pathfinding.fleePath(entity.getPosition(), target, entity.getMoveDistance());
  }

  public List<Position> pathTo(PersonaEntity entity, Position end) {
    clampToWorld(end, entity.getSize());

    debug(List.of());
    List<Position> path = pathfinding.computePath(entity.getPosition(), end, entity.getMoveDistance());

    debug(path);

    return path;
  }

  private Position clampToWorld(Position target, Size size) {
    float personaWidth = size.getWidth();
    float personaHeigth = size.getHeight();
    target.setX(MathUtils.round(MathUtils.clamp(target.getX(), personaWidth, map.getLayeredMap().getWidth() - personaWidth)));
    target.setY(MathUtils.round(MathUtils.clamp(target.getY(), personaHeigth, map.getLayeredMap().getHeight() - personaHeigth)));
    return target;
  }


  public void debug(List<Position> path) {
    Map<Vector2, String> mapDebug = new HashMap<>();

    map.getMovableObjects().forEach(obstacle -> {
      int startX = MathUtils.floor(obstacle.getX());
      int startY = MathUtils.floor(obstacle.getY());
      int width = MathUtils.ceil(obstacle.getX() + obstacle.getWidth());
      int height = MathUtils.ceil(obstacle.getY() + obstacle.getHeight());
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
