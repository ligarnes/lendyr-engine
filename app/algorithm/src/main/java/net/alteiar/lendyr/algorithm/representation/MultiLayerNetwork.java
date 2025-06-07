package net.alteiar.lendyr.algorithm.representation;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Getter
public class MultiLayerNetwork {
  private final List<Tile> tiles;

  @Builder
  public MultiLayerNetwork(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public Tile find(Vector2 position, int layer) {
    return find(position.x, position.y, layer);
  }

  public Tile find(float x, float y, int layer) {
    return this.tiles.stream().filter(t -> t.getX() == x && t.getY() == y && t.isLayer(layer)).findFirst().orElse(null);
  }

  public void addObstacle(float x, float y, int layer) {
    this.tiles.stream().filter(t -> t.getX() == x && t.getY() == y && t.isLayer(layer)).forEach(t -> t.setValid(false));
  }

  public void reset() {
    tiles.forEach(Tile::reset);
  }

  public void debug() {
    Set<Integer> layers = tiles.stream().flatMap(t -> {
      int[] l = t.getLayers();
      List<Integer> list = new ArrayList<>();
      for (int j : l) {
        list.add(j);
      }
      return list.stream();
    }).collect(Collectors.toSet());

    int maxX = tiles.stream().map(Tile::getX).mapToInt(Float::intValue).max().orElse(0);
    int maxY = tiles.stream().map(Tile::getY).mapToInt(Float::intValue).max().orElse(0);

    log.info("Map size: {}x{}", maxX + 1, maxY + 1);
    for (Integer layer : layers) {
      debugLayer(layer);
    }
  }

  public void debugLayer(int layer) {
    int maxX = tiles.stream().map(Tile::getX).mapToInt(Float::intValue).max().orElse(0);
    int maxY = tiles.stream().map(Tile::getY).mapToInt(Float::intValue).max().orElse(0);

    log.info("Layer {}:", layer);
    for (int y = maxY + 1; y >= -1; y--) {
      StringBuilder stringBuilder = new StringBuilder();
      for (int x = -1; x < maxX + 2; x++) {
        Tile tile = find(x, y, layer);
        if (tile != null) {
          if (tile.isValid()) {
            stringBuilder.append("_");
          } else {
            stringBuilder.append("x");
          }
        } else {
          stringBuilder.append("=");
        }
      }
      log.info(stringBuilder.toString());
    }
  }
}
