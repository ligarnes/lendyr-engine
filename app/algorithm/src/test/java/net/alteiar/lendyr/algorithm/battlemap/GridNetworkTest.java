package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.algorithm.astar.AStarAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GridNetworkTest {

  private static GridNetwork generateGrid(int width, int height) {
    ArrayList<Tile> tiles = new ArrayList<>();

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Tile t = Tile.builder().valid(true).position(new Vector2(i, j)).build();
        tiles.add(t);
      }
    }

    GridNetwork gridNetwork = GridNetwork.builder()
        .width(width)
        .height(height)
        .tiles(tiles)
        .build();

    for (Tile t : gridNetwork.getTiles()) {
      t.calculateNeighbours(gridNetwork);
    }

    return gridNetwork;
  }

  @Test
  void test() {
    GridNetwork gridNetwork = generateGrid(100, 100);

    AStarAlgorithm<GridNetwork, Tile> astar = new AStarAlgorithm<>(gridNetwork);

    List<Tile> path = astar.solve(gridNetwork.find(10, 10), gridNetwork.find(30, 30));

    for (Tile t : path.reversed()) {
      System.out.println(t.getX() + "," + t.getY());
    }
  }
}