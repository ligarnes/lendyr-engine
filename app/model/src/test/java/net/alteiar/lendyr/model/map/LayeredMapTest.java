package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class LayeredMapTest {

  LayeredMap layeredMap;

  @BeforeEach
  void beforeEach() {
    TiledMap tiledMap = TiledMap.load(new File("../../assembly/data/tiled/sample.tmx"));

    layeredMap = new MapFactory(tiledMap).load();
  }

  @Test
  void getLayersAt() {
    Assertions.assertEquals(List.of(3), layeredMap.getLayersAt(12, 14));
    Assertions.assertEquals(List.of(3), layeredMap.getLayersAt(13, 14));
    Assertions.assertEquals(List.of(3), layeredMap.getLayersAt(14, 14));
    Assertions.assertEquals(List.of(4), layeredMap.getLayersAt(15, 14));
    Assertions.assertEquals(List.of(4), layeredMap.getLayersAt(16, 14));
    Assertions.assertEquals(List.of(3), layeredMap.getLayersAt(17, 14));
  }

  @Test
  void checkCollision() {
    Assertions.assertFalse(layeredMap.checkCollision(3, new Rectangle(12, 14, 1, 1)));
    Assertions.assertFalse(layeredMap.checkCollision(3, new Rectangle(13, 14, 1, 1)));
    Assertions.assertFalse(layeredMap.checkCollision(3, new Rectangle(14, 14, 1, 1)));
    Assertions.assertTrue(layeredMap.checkCollision(3, new Rectangle(14, 15, 1, 1)));
    Assertions.assertTrue(layeredMap.checkCollision(3, new Rectangle(14, 16, 1, 1)));
    Assertions.assertFalse(layeredMap.checkCollision(3, new Rectangle(17, 14, 1, 1)));
  }

  @Test
  void debug() {
    layeredMap.debug();
  }

}