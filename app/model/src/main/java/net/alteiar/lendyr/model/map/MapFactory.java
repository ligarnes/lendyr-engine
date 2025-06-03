package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.map.element.MapElement;
import net.alteiar.lendyr.model.map.element.PolygonMapElement;
import net.alteiar.lendyr.model.map.element.RectangleMapElement;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import net.alteiar.lendyr.model.map.tiled.TiledObjectGroup;
import net.alteiar.lendyr.model.map.tiled.object.TiledObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class MapFactory {

  private final TiledMap map;
  private final int worldWidth;
  private final int worldHeight;
  private final float scaleX;
  private final float scaleY;

  public MapFactory(TiledMap map) {
    this.map = map;
    worldWidth = map.getWidth();
    worldHeight = map.getHeight();
    scaleX = map.getScaleX();
    scaleY = map.getScaleY();
  }

  private List<MapElement> accumulateElement(List<MapElement> mapElements, TiledObject o) {
    List<MapElement> elements = mapElements;
    if (elements == null) {
      elements = new ArrayList<>();
    }
    MapElement element = createMapElement(o);
    if (element != null) {
      elements.add(element);
    } else {
      log.warn("Element {} of type {} is not supported", o.getName(), o.getType());
    }
    return elements;
  }

  /**
   * Load a tiled map as a layered map.
   *
   * @return the layeredMap
   */
  public LayeredMap load() {
    Map<Integer, List<MapElement>> obstaclesPerLayer = new HashMap<>();
    Map<Integer, List<MapElement>> activationsPerLayer = new HashMap<>();
    Map<Integer, StaticMapLayer> layers = new HashMap<>();
    List<Bridge> bridges = new ArrayList<>();

    map.getByName("obstacles")
        .map(TiledObjectGroup::getObject)
        .stream()
        .flatMap(List::stream)
        .forEach(o -> {
          int layer = o.getIntProperty("layer");
          obstaclesPerLayer.compute(layer, (idx, mapElements) -> accumulateElement(mapElements, o));
        });

    map.getByName("activation")
        .map(TiledObjectGroup::getObject)
        .stream()
        .flatMap(List::stream)
        .forEach(o -> {
          int layer = o.getIntProperty("layer");
          activationsPerLayer.compute(layer, (idx, mapElements) -> accumulateElement(mapElements, o));
        });

    map.getByName("bridges")
        .map(TiledObjectGroup::getObject)
        .stream()
        .flatMap(List::stream)
        .forEach(o -> {
          int lower = o.getIntProperty("lower");
          int upper = o.getIntProperty("upper");
          MapElement element = createMapElement(o);
          bridges.add(Bridge.builder().lower(lower).upper(upper).region(element).build());
        });

    map.getByName("layers")
        .map(TiledObjectGroup::getObject)
        .stream()
        .flatMap(List::stream)
        .forEach(o -> {
          int layerId = o.getIntProperty("layer");
          Shape2D layerShape = convertToWorldCoordinate(o.getShape());
          List<MapElement> obstacles = obstaclesPerLayer.getOrDefault(layerId, new ArrayList<>());
          List<MapElement> activations = activationsPerLayer.getOrDefault(layerId, new ArrayList<>());
          layers.put(layerId, new StaticMapLayer(worldWidth, worldHeight, layerShape, obstacles, activations));
        });

    return new LayeredMap(map.getWidth(), map.getHeight(), layers, bridges);
  }

  private MapElement createMapElement(TiledObject object) {
    return switch (object.getType()) {
      case POLYGON -> PolygonMapElement.builder().name(object.getName()).polygon(convertToWorldCoordinate(object.getPolygon())).build();
      case RECTANGLE -> RectangleMapElement.builder().name(object.getName()).rectangle(convertToWorldCoordinate(object.getRectangle())).build();
      default -> null;
    };
  }

  private Shape2D convertToWorldCoordinate(Shape2D shape2D) {
    return switch (shape2D) {
      case Polygon poly -> convertToWorldCoordinate(poly);
      case Rectangle rectangle -> convertToWorldCoordinate(rectangle);
      default -> throw new UnsupportedOperationException("Shape not supported yet %s".formatted(shape2D.getClass()));
    };
  }

  private Polygon convertToWorldCoordinate(Polygon polygon) {
    Polygon newPolygon = new Polygon(polygon.getTransformedVertices());
    for (int i = 0; i < polygon.getVertexCount(); i++) {
      Vector2 newVector = convertToWorldCoordinate(newPolygon.getVertex(i, new Vector2()));
      newPolygon.setVertex(i, newVector.x, newVector.y);
    }
    return newPolygon;
  }

  private Vector2 convertToWorldCoordinate(Vector2 vector2) {
    return vector2.set(vector2.x * scaleX, worldHeight - (vector2.y * scaleY));
  }

  private Rectangle convertToWorldCoordinate(Rectangle rectangle) {
    float newHeight = rectangle.height * scaleY;

    return new Rectangle(
        rectangle.x * scaleX,
        worldHeight - (rectangle.y * scaleY + newHeight),
        rectangle.width * scaleX,
        newHeight);
  }
}
