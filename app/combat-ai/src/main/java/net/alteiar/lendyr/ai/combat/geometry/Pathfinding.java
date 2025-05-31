package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.astar.AStarAlgorithm;
import net.alteiar.lendyr.algorithm.battlemap.GridNetwork;
import net.alteiar.lendyr.algorithm.battlemap.Tile;
import net.alteiar.lendyr.entity.PersonaEntity;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Pathfinding {
  @Getter
  private final GridNetwork gridNetwork;
  private final AStarAlgorithm<GridNetwork, Tile> astar;

  @Builder
  Pathfinding(GridNetwork gridNetwork) {
    this.gridNetwork = gridNetwork;
    this.astar = new AStarAlgorithm<>(gridNetwork);
  }

  public List<Vector2> computePath(PersonaEntity entity, Vector2 end) {
    clampToWorld(entity, end);
    int currentX = Math.round(entity.getPosition().x);
    int currentY = Math.round(entity.getPosition().y);
    log.info("Compute path from ({},{}) to ({},{})", currentX, currentY, end.x, end.y);
    List<Tile> foundPath = astar.solve(gridNetwork.find(currentX, currentY), gridNetwork.find(Math.round(end.x), Math.round(end.y)));

    if (foundPath.size() < 2) {
      return List.of();
    }

    float distance = 0;
    Vector2 currentPosition = entity.getPosition();
    List<Vector2> path = new ArrayList<>();
    for (int i = 1; i < foundPath.size(); i++) {
      Vector2 newPosition = foundPath.get(i).getVector2().cpy();
      distance += currentPosition.dst(newPosition);
      if (distance > entity.getMoveDistance()) {
        log.info("Move too far ({}) at {} ", distance, newPosition);
        break;
      }
      path.add(newPosition);
      currentPosition = newPosition;
    }

    return path;
  }

  private Vector2 clampToWorld(PersonaEntity persona, Vector2 target) {
    float personaWidth = persona.getSize().getWidth();
    float personaHeigth = persona.getSize().getHeight();
    target.x = MathUtils.clamp(target.x, personaWidth, gridNetwork.getWidth() - personaWidth);
    target.y = MathUtils.clamp(target.y, personaHeigth, gridNetwork.getHeight() - personaHeigth);
    return target;
  }

  public void debug() {
    log.info("layer {}: ", 1);
    for (int y = gridNetwork.getHeight() - 1; y >= 0; y--) {
      StringBuilder builder = new StringBuilder();
      for (int x = 0; x < gridNetwork.getWidth(); x++) {
        Tile tile = gridNetwork.find(x, y);
        String symbol = "_";
        if (tile.isMovableObject()) {
          symbol = "o";
        } else if (!tile.isValid()) {
          symbol = "x";
        }
        builder.append(symbol).append(" ");
      }
      log.info(builder.toString());
    }
  }
}
