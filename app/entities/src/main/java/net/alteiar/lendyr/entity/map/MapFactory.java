package net.alteiar.lendyr.entity.map;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import net.alteiar.lendyr.entity.map.element.LineMapElement;
import net.alteiar.lendyr.entity.map.element.MapElement;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.MapLayer;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;
import net.alteiar.lendyr.persistence.dao.TiledMap;
import net.alteiar.lendyr.persistence.dao.TiledObjectGroup;
import net.alteiar.lendyr.persistence.dao.tiled.object.TiledObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class MapFactory {

  LocalMapDao map;

  /**
   * Load a tiled map as a layered map.
   *
   * @param map the game map
   * @return the layeredMap
   */
  public LayeredMap load() {
    TiledMap tiledMap = map.getTiledMap();
    GameMap game = map.getMap();
    LayeredMap world = new LayeredMap(tiledMap.getWidth(), tiledMap.getHeight());
    tiledMap.getObjectgroup().forEach(g -> {
      int layerId = game.getLayers().stream().filter(p -> g.getName().equals(p.getLayerName())).map(MapLayer::getLayerId).findFirst().orElse(-1);
      if (layerId != -1) {
        StaticMapLayer layer = processLayer(tiledMap, g);
        world.getLayers().put(layerId, layer);
      }
    });

    return world;
  }

  private StaticMapLayer processLayer(TiledMap map, TiledObjectGroup group) {
    StaticMapLayer layer = new StaticMapLayer(map.getWidth(), map.getHeight());
    group.getObject().forEach(o -> {
      MapElement mapElement = processMapElement(o);
      if (Objects.nonNull(mapElement)) {
        layer.getMapElements().add(mapElement);
      }
    });
    return layer;
  }

  private MapElement processMapElement(TiledObject object) {
    if (object.getPolyline() != null) {
      float x = object.getX();
      float y = object.getY();
      List<Vector2> points = new ArrayList<>();
      String[] pointsStr = object.getPolyline().getPoints().split(" ");
      for (String s : pointsStr) {
        String[] values = s.split(",");
        float worldX = (x + Float.parseFloat(values[0])) / 32f;
        float worldY = map.getMap().getWorldHeight() - (y + Float.parseFloat(values[1])) / 32f;
        points.add(new Vector2(worldX, worldY));
      }
      return LineMapElement.builder().points(points).build();
    }
    return null;
  }
}
