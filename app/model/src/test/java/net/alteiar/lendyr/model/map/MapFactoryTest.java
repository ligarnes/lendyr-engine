package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Polygon;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.alteiar.lendyr.model.map.layered.LayeredMap;
import net.alteiar.lendyr.model.map.layered.MapFactory;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class MapFactoryTest {

  @Test
  void singleLayer() throws IOException {
    XmlMapper mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    TiledMap tiledMap = mapper.readValue(MapFactoryTest.class.getResourceAsStream("/simple-layer-1.tmx"), TiledMap.class);
    MapFactory mapFactory = new MapFactory(tiledMap);

    LayeredMap layeredMap = mapFactory.load();

    layeredMap.debug();
  }

  @Test
  void multi_layer() throws IOException {
    XmlMapper mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    TiledMap tiledMap = mapper.readValue(MapFactoryTest.class.getResourceAsStream("/sample.tmx"), TiledMap.class);
    MapFactory mapFactory = new MapFactory(tiledMap);

    LayeredMap layeredMap = mapFactory.load();

    System.out.println(Arrays.toString(((Polygon) layeredMap.getLayer(1).getShape()).getTransformedVertices()));
    System.out.println(layeredMap.getLayer(2).getShape());

    layeredMap.debug();
  }
}