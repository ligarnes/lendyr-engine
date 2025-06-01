package net.alteiar.lendyr.algorithm.representation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;
import net.alteiar.lendyr.model.map.LayeredMap;
import net.alteiar.lendyr.model.map.StaticMapLayer;

import java.util.ArrayList;
import java.util.Map;

@UtilityClass
public class MultiLayerNetworkFactory {

  public static MultiLayerNetwork generate(LayeredMap mapEntity) {
    ArrayList<Tile> tiles = new ArrayList<>();

    mapEntity.getLayers().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
      int layer = entry.getKey();
      StaticMapLayer map = entry.getValue();
      for (float x = 0; x < map.getWidth(); x++) {
        for (float y = 0; y < map.getHeight(); y++) {
          Rectangle square = new Rectangle(x, y, 1, 1);
          if (map.isInLayer(square)) {
            boolean collision = mapEntity.checkCollision(layer, square);
            if (!collision) {
              int[] layers = mapEntity.getBridge(square).map(b -> new int[]{b.getLower(), b.getUpper()}).orElse(new int[]{layer});
              Tile t = new Tile(new Vector2(x, y), layers, true);
              tiles.add(t);
            }
          }
        }
      }
    });

    MultiLayerNetwork multiLayerNetwork = MultiLayerNetwork.builder().tiles(tiles).build();
    multiLayerNetwork.getTiles().forEach(t -> t.calculateNeighbours(multiLayerNetwork));
    return multiLayerNetwork;
  }

}
