package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.algorithm.astar.AStarAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GridNetworkTest {

  private static GridNetwork generateGrid(int width, int height) {
    ArrayList<Tile> tiles = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Tile t = Tile.builder().valid(true).position(new Vector2(i, j)).build();
        tiles.add(t);
      }
    }

    return GridNetwork.builder()
        .width(width)
        .height(height)
        .tiles(tiles)
        .build();
  }

  @Test
  void test() {
    GridNetwork gridNetwork = generateGrid(100, 100);
    for (Tile t : gridNetwork.getTiles()) {
      t.calculateNeighbours(gridNetwork);
    }

    AStarAlgorithm<GridNetwork, Tile> astar = new AStarAlgorithm<>(gridNetwork);

    astar.solve(gridNetwork.find(10, 10), gridNetwork.find(30, 30));
    ArrayList<Tile> path = astar.getPath();

    for (Tile t : path) {
      System.out.println(t.getX() + "," + t.getY());
    }
  }
}