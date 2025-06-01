package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

  public void addObstacle(float x, float y) {
    this.tiles.stream().filter(t -> t.getX() == x && t.getY() == y).forEach(t -> t.setValid(false));
  }

  public void reset() {
    tiles.forEach(Tile::reset);
  }
}
