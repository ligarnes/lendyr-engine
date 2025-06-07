package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class LayeredMap {
  @Getter
  private final int width;
  @Getter
  private final int height;
  private final Map<Integer, StaticMapLayer> layers;
  @Getter
  private final List<Bridge> bridges;

  public LayeredMap(int width, int height, Map<Integer, StaticMapLayer> layers, List<Bridge> bridges) {
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
    Rectangle square = createCollisionRectangle(x, y, 1, 1);
    layers.forEach((layer, map) -> {
      if (map.isInLayer(square) && !map.checkCollision(square)) {
        inLayer.add(layer);
      }
    });
    return inLayer;
  }

  public boolean checkCollision(int layer, Rectangle rect) {
    return layers.get(layer).checkCollision(createCollisionRectangle(rect));
  }

  public boolean isInLayer(int layer, Rectangle rect) {
    return layers.get(layer).isInLayer(createCollisionRectangle(rect));
  }

  public List<Integer> getLayers() {
    return layers.keySet().stream().toList();
  }

  /// This method should be avoided.
  /// Use directly the layered map.
  /// It is used for copy/DTO mapping
  public StaticMapLayer getLayer(int layer) {
    return layers.get(layer);
  }

  public int getLayerWidth(int layer) {
    return layers.get(layer).getWidth();
  }

  public int getLayerHeight(int layer) {
    return layers.get(layer).getHeight();
  }

  ///  Create the collision rectangle.
  /// Since touching rectangle generates collision, we want to reduce the collision rectangle by minimal margin to avoid limit/marginal collision.
  private Rectangle createCollisionRectangle(Rectangle rectangle) {
    return createCollisionRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
  }

  ///  Create the collision rectangle.
  /// Since touching rectangle generates collision, we want to reduce the collision rectangle by minimal margin to avoid limit/marginal collision.
  private Rectangle createCollisionRectangle(float x, float y, float width, float height) {
    return new Rectangle(x + 0.01f, y + 0.01f, width - 0.02f, height - 0.02f);
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
        if (isInLayer(layer, square)) {
          Optional<Bridge> bridge = getBridge(square);
          if (checkCollision(layer, square)) {
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

  public void debug(int layer) {
    debugPathLayer(layer, 1, Collections.emptyMap());
  }
}
