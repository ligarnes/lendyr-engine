package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
@Log4j2
public class LayeredMap {
  float width;
  float height;
  Map<Integer, StaticMapLayer> layers;
  List<Bridge> bridges;

  public LayeredMap(float width, float height, Map<Integer, StaticMapLayer> layers, List<Bridge> bridges) {
    this.width = width;
    this.height = height;
    this.bridges = Collections.unmodifiableList(bridges);
    this.layers = Collections.unmodifiableMap(layers);
  }

  public boolean checkCollision(int layer, Rectangle rect) {
    return layers.get(layer).checkCollision(rect);
  }

  public Optional<Bridge> getBridge(Rectangle position) {
    return bridges.stream().filter(b -> b.getRegion().checkCollision(position)).findFirst();
  }

  public void debugPath(Map<Vector2, String> path) {
    debugPath(1, path);
  }

  public void debugPath(float squareSize, Map<Vector2, String> path) {
    layers.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
      debugPathLayer(entry.getKey(), squareSize, path);
    });
  }

  private void debugPathLayer(int layer, float squareSize, Map<Vector2, String> path) {
    StaticMapLayer currentLayer = layers.get(layer);

    Rectangle rect = currentLayer.getBounds();

    log.info("Layer: {}", layer);
    for (int y = (int) (rect.getY() + rect.getHeight()); y >= rect.getY() - 1; y--) {
      StringBuilder stringBuilder = new StringBuilder();
      for (int x = (int) rect.getX() - 1; x < rect.getX() + rect.getWidth() + 1; x++) {
        Rectangle square = new Rectangle(x, y, squareSize, squareSize);
        if (currentLayer.isInLayer(square)) {
          Optional<Bridge> bridge = getBridge(square);
          if (currentLayer.checkCollision(square)) {
            stringBuilder.append("x");
          } else if (path.containsKey(new Vector2(x, y))) {
            stringBuilder.append(path.get(new Vector2(x, y)));
          } else if (bridge.isPresent()) {
            if (bridge.get().getLower() == layer) {
              stringBuilder.append("^");
            } else {
              stringBuilder.append("!");
            }
          } else {
            stringBuilder.append("_");
          }
        } else {
          stringBuilder.append("=");
        }
      }
      log.info(stringBuilder.toString());
    }
  }

  public void debug() {
    debugPath(Collections.emptyMap());
  }
}
