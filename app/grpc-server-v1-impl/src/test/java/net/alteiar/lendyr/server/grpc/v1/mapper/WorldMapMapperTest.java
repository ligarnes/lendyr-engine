package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.LendyrLayeredMap;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrStaticLayer;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrWorld;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;
import net.alteiar.lendyr.model.map.Bridge;
import net.alteiar.lendyr.model.map.LayeredMap;
import net.alteiar.lendyr.model.map.StaticMapLayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class WorldMapMapperTest {

  @Test
  void mapToDto() {
    LayeredMap layeredMap = newLayeredMap();
    GameMap map = RandomProvider.INSTANCE.nextObject(GameMap.class);

    LendyrMap dto = WorldMapMapper.INSTANCE.mapToDto(map, layeredMap);

    Assertions.assertEquals(map.getPath(), dto.getPath());
    Assertions.assertEquals(WorldMapMapper.INSTANCE.layeredMapToDto(layeredMap), dto.getMap());
  }

  @Test
  void layeredMapToDto() {
    LayeredMap layeredMap = newLayeredMap();

    // When
    LendyrLayeredMap dto = WorldMapMapper.INSTANCE.layeredMapToDto(layeredMap);

    // Then
    Assertions.assertEquals(layeredMap.getWidth(), dto.getWidth());
    Assertions.assertEquals(layeredMap.getHeight(), dto.getHeight());
    Assertions.assertEquals(layeredMap.getBridges().size(), dto.getBridgesCount());
    for (int i = 0; i < layeredMap.getBridges().size(); i++) {
      Assertions.assertEquals(layeredMap.getBridges().get(i).getLower(), dto.getBridges(i).getLower());
      Assertions.assertEquals(layeredMap.getBridges().get(i).getUpper(), dto.getBridges(i).getUpper());
      Assertions.assertEquals(GeometryMapper.INSTANCE.mapElementToDto(layeredMap.getBridges().get(i).getRegion()), dto.getBridges(i).getRegion());
    }
    Assertions.assertEquals(layeredMap.getLayers().size(), dto.getLayersMap().size());
    for (Integer layer : layeredMap.getLayers()) {
      Assertions.assertEquals(WorldMapMapper.INSTANCE.layerToDto(layeredMap.getLayer(layer)), dto.getLayersMap().get(layer));
    }
  }

  @Test
  void layerToDto() {
    StaticMapLayer layer = RandomProvider.INSTANCE.nextObject(StaticMapLayer.class);

    LendyrStaticLayer layerDto = WorldMapMapper.INSTANCE.layerToDto(layer);

    Assertions.assertEquals(layer.getWidth(), layerDto.getWidth());
    Assertions.assertEquals(layer.getHeight(), layerDto.getHeight());
    Assertions.assertEquals(GeometryMapper.INSTANCE.shapeToDto(layer.getShape()), layerDto.getShape());
    Assertions.assertEquals(layer.getActivations().size(), layerDto.getActivationsCount());
    for (int i = 0; i < layer.getActivations().size(); i++) {
      Assertions.assertEquals(GeometryMapper.INSTANCE.mapElementToDto(layer.getActivations().get(i)), layerDto.getActivations(i));
    }
    Assertions.assertEquals(layer.getMapElements().size(), layerDto.getMapElementsCount());
    for (int i = 0; i < layer.getMapElements().size(); i++) {
      Assertions.assertEquals(GeometryMapper.INSTANCE.mapElementToDto(layer.getMapElements().get(i)), layerDto.getMapElements(i));
    }
  }

  @Test
  void mapWorldToDto() {
    LocalMap gameMap = RandomProvider.INSTANCE.nextObject(LocalMap.class);

    LendyrWorld map = WorldMapMapper.INSTANCE.worldMapToDto(gameMap);

    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(gameMap.getMapId()), map.getMapId());
    Assertions.assertEquals(gameMap.getName(), map.getName());

    Assertions.assertEquals(gameMap.getEntities().size(), map.getEntityList().size());
    for (int i = 0; i < gameMap.getEntities().size(); i++) {
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(gameMap.getEntities().get(i)), map.getEntity(i));
    }
  }

  LayeredMap newLayeredMap() {
    Map<Integer, StaticMapLayer> layers = new HashMap<>();
    layers.put(1, RandomProvider.INSTANCE.nextObject(StaticMapLayer.class));
    layers.put(2, RandomProvider.INSTANCE.nextObject(StaticMapLayer.class));
    layers.put(3, RandomProvider.INSTANCE.nextObject(StaticMapLayer.class));
    List<Bridge> bridges = new ArrayList<>();
    bridges.add(RandomProvider.INSTANCE.nextObject(Bridge.class));
    bridges.add(RandomProvider.INSTANCE.nextObject(Bridge.class));
    bridges.add(RandomProvider.INSTANCE.nextObject(Bridge.class));

    return new LayeredMap(20, 30, layers, bridges);
  }
}