package net.alteiar.lendyr.algorithm.representation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;
import net.alteiar.lendyr.model.map.layered.LayeredMap;

import java.util.ArrayList;

@UtilityClass
public class MultiLayerNetworkFactory {

  public static MultiLayerNetwork generate(LayeredMap layeredMap) {
    ArrayList<Tile> tiles = new ArrayList<>();

    layeredMap.getLayers().stream().sorted().forEach(layer -> {
      for (int x = 0; x < layeredMap.getWidth(); x++) {
        for (int y = 0; y < layeredMap.getHeight(); y++) {
          Rectangle square = new Rectangle(x, y, 1, 1);
          if (layeredMap.isInLayer(layer, square) && !layeredMap.checkCollision(layer, square)) {
            int[] layers = layeredMap.getBridge(square).map(b -> new int[]{b.getLower(), b.getUpper()}).orElse(new int[]{layer});
            Tile t = new Tile(new Vector2(x, y), layers, true);
            tiles.add(t);
          }
        }
      }
    });

    MultiLayerNetwork multiLayerNetwork = MultiLayerNetwork.builder().tiles(tiles).build();
    multiLayerNetwork.getTiles().forEach(t -> t.calculateNeighbours(multiLayerNetwork));
    return multiLayerNetwork;
  }
}
