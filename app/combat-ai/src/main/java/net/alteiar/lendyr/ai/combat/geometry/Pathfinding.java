package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.MathUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.astar.AStarAlgorithm;
import net.alteiar.lendyr.algorithm.astar.FleeAlgorithm;
import net.alteiar.lendyr.algorithm.battlemap.MultiLayerNetwork;
import net.alteiar.lendyr.algorithm.battlemap.Tile;
import net.alteiar.lendyr.model.persona.Position;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Pathfinding {
  @Getter
  private final MultiLayerNetwork multiLayerNetwork;
  private final AStarAlgorithm<Tile> astar;
  private final FleeAlgorithm<Tile> flee;

  @Builder
  Pathfinding(MultiLayerNetwork layeredMap) {
    this.multiLayerNetwork = layeredMap;
    this.astar = new AStarAlgorithm<>();
    this.flee = new FleeAlgorithm<>();
  }

  public List<Position> fleePath(Position start, Position danger, float maxDistance) {
    Tile startTile = getTile(start);
    Tile endTile = getTile(danger);

    List<Tile> foundPath = flee.solve(startTile, endTile, maxDistance);

    return resolveRealPath(start, foundPath, maxDistance);
  }

  public List<Position> computePath(Position start, Position end, float maxDistance) {
    Tile startTile = getTile(start);
    Tile endTile = getTile(end);
    boolean isValid = endTile.isValid();
    if (!isValid) {
      endTile.setValid(true);
    }
    try {
      List<Tile> foundPath = astar.solve(startTile, endTile);
      if (!isValid && !foundPath.isEmpty()) {
        foundPath.removeLast();
      }
      return resolveRealPath(start, foundPath, maxDistance);
    } finally {
      endTile.setValid(isValid);
    }
  }

  private Tile getTile(Position start) {
    Tile startTile = multiLayerNetwork.find(MathUtils.ceil(start.getX()), MathUtils.ceil(start.getY()), start.getLayer());
    if (startTile == null) {
      throw new IllegalArgumentException("Position %s is not a valid position".formatted(start));
    }
    return startTile;
  }

  private List<Position> resolveRealPath(Position start, List<Tile> foundPath, float maxDistance) {
    float distance = 0;
    Position currentPosition = start;
    List<Position> path = new ArrayList<>();
    for (int i = 0; i < foundPath.size(); i++) {
      Position newPosition = foundPath.get(i).toPosition();
      distance += currentPosition.dst(newPosition);
      if (distance > maxDistance) {
        log.info("Move too far ({}) at {} ", distance, newPosition);
        break;
      }
      path.add(newPosition);
      currentPosition = newPosition;
    }

    return path;
  }
}
