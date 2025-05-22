package net.alteiar.lendyr.algorithm.battlemap;

import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.algorithm.astar.Network;

import java.util.ArrayList;

@Getter
public class GridNetwork extends Network<Tile> {

  private final int width;
  private final int height;
  private final ArrayList<Tile> tiles;

  @Builder
  public GridNetwork(int width, int height, ArrayList<Tile> tiles) {
    this.width = width;
    this.height = height;
    this.tiles = tiles;
  }

  public Tile find(float x, float y) {
    for (Tile t : tiles) {
      if (t.getX() == x && t.getY() == y)
        return t;
    }
    return null;
  }

  @Override
  public Iterable<Tile> getNodes() {
    return tiles;
  }
}
