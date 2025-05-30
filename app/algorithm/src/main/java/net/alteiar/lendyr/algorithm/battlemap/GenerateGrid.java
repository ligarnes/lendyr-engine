package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.entity.LocalMapEntity;

import java.util.ArrayList;

public class GenerateGrid {

  public static GridNetwork generate(LocalMapEntity mapEntity) {
    ArrayList<Tile> tiles = new ArrayList<>();

    int width = mapEntity.getWidth();
    int height = mapEntity.getHeight();

    for (float i = 0; i < mapEntity.getWidth(); i++) {
      for (float j = 0; j < mapEntity.getHeight(); j++) {
        boolean collision = mapEntity.checkCollision(new Rectangle(i, j, 1, 1));
        Tile t = new Tile(new Vector2(i, j), !collision);
        tiles.add(t);
      }
    }

    GridNetwork gridNetwork = GridNetwork.builder().width(width).height(height).tiles(tiles).build();
    for (Tile t : gridNetwork.getTiles()) {
      t.calculateNeighbours(gridNetwork);
    }

    return gridNetwork;
  }

}
