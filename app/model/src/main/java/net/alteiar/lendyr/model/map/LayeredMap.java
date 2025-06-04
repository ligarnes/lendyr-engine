package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.*;

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

  public boolean moveTo(DynamicBlockingObject initial, DynamicBlockingObject target) {
    if (!isAdjacent(initial.getRectangle(), target.getRectangle())) {
      return false;
    }

    // Find layer to check
    int layer = initial.getLayer();
    boolean sameLayer = Objects.equals(initial.getLayer(), target.getLayer());
    if (!sameLayer) {
      Optional<Bridge> bridgeInitial = getBridge(initial.getRectangle());
      if (bridgeInitial.isPresent() && (bridgeInitial.get().getLower() == target.getLayer() || bridgeInitial.get().getUpper() == target.getLayer())) {
        layer = target.getLayer();
      } else if (bridgeInitial.isEmpty()) {
        Optional<Bridge> bridgeTarget = getBridge(target.getRectangle());
        if (bridgeTarget.isEmpty() || (bridgeTarget.get().getLower() != target.getLayer() && bridgeTarget.get().getUpper() != target.getLayer())) {
          return true;
        }
      } else {
        return true;
      }
    }

    return checkCollision(layer, target.getRectangle());
  }

  private boolean isAdjacent(Rectangle initial, Rectangle target) {
    // Both positions need to overlap with a 0.1f margin.
    return initial.overlaps(new Rectangle(target.getX() - 0.1f, target.getY() - 0.1f, target.getWidth() + 0.2f, target.getHeight() + 0.2f));
  }

  public List<Integer> getLayersAt(float x, float y) {
    List<Integer> inLayer = new ArrayList<>();
    layers.forEach((layer, map) -> {
      if (map.isInLayer(x, y)) {
        inLayer.add(layer);
      }
    });
    return inLayer;
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
