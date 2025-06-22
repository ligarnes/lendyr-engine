package net.alteiar.lendyr.algorithm.representation;

import net.alteiar.lendyr.model.map.layered.LayeredMap;
import net.alteiar.lendyr.model.map.layered.MapFactory;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class MultiLayerNetworkTest {
  LayeredMap layeredMap;
  MultiLayerNetwork multiLayerNetwork;

  @BeforeEach
  void beforeEach() {
    TiledMap tiledMap = TiledMap.load(new File("../../assembly/data/tiled/sample.tmx"));

    layeredMap = new MapFactory(tiledMap).load();
    multiLayerNetwork = MultiLayerNetworkFactory.generate(layeredMap);
  }

  @Test
  void find() {
    Assertions.assertNotNull(multiLayerNetwork.find(0, 0, 1));
    Assertions.assertTrue(multiLayerNetwork.find(0, 0, 1).isValid());
    Assertions.assertNotNull(multiLayerNetwork.find(0, 0, 1));
    Assertions.assertNotNull(multiLayerNetwork.find(0, 0, 1));
  }

  @Test
  void debug() {
    layeredMap.debug(3);
    multiLayerNetwork.debugLayer(3);
  }
}