package net.alteiar.lendyr.model.map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class LayeredMapTest {

  @Test
  void getLayersAt() throws IOException {
    XmlMapper mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    TiledMap tiledMap = mapper.readValue(MapFactoryTest.class.getResourceAsStream("/sample.tmx"), TiledMap.class);
    MapFactory mapFactory = new MapFactory(tiledMap);

    LayeredMap layeredMap = mapFactory.load();

    layeredMap.debug();

    Assertions.assertEquals(List.of(3), layeredMap.getLayersAt(15, 13));
    Assertions.assertEquals(List.of(4), layeredMap.getLayersAt(15, 14));
    Assertions.assertEquals(List.of(4), layeredMap.getLayersAt(15, 15));
  }
}