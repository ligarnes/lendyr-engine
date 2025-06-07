package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.*;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;
import net.alteiar.lendyr.model.map.Bridge;
import net.alteiar.lendyr.model.map.LayeredMap;
import net.alteiar.lendyr.model.map.StaticMapLayer;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(
    uses = {GenericMapper.class, GeometryMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface WorldMapMapper {
  WorldMapMapper INSTANCE = Mappers.getMapper(WorldMapMapper.class);

  default LendyrMap mapToDto(GameMap gameMap, LayeredMap layeredMap) {
    return LendyrMap.newBuilder()
        .setPath(gameMap.getPath())
        .setMap(layeredMapToDto(layeredMap))
        .build();
  }

  default LendyrLayeredMap layeredMapToDto(LayeredMap map) {
    if (map == null) {
      return null;
    }
    List<Integer> layersIdx = map.getLayers();
    Map<Integer, LendyrStaticLayer> layers = new HashMap<>(layersIdx.size());
    for (Integer layerIdx : layersIdx) {
      layers.put(layerIdx, layerToDto(map.getLayer(layerIdx)));
    }

    return LendyrLayeredMap.newBuilder()
        .setWidth(map.getWidth())
        .setHeight(map.getHeight())
        .addAllBridges(bridgeListToDto(map.getBridges()))
        .putAllLayers(layers)
        .build();
  }

  List<LendyrMapBridge> bridgeListToDto(List<Bridge> bridges);
  
  default LendyrStaticLayer layerToDto(StaticMapLayer map) {
    if (map == null) {
      return null;
    }

    LendyrStaticLayer.Builder staticLayer = LendyrStaticLayer.newBuilder()
        .setWidth(map.getWidth())
        .setHeight(map.getHeight());

    staticLayer.setShape(GeometryMapper.INSTANCE.shapeToDto(map.getShape()));

    staticLayer.addAllActivations(GeometryMapper.INSTANCE.mapElementListToDto(map.getActivations()));
    staticLayer.addAllMapElements(GeometryMapper.INSTANCE.mapElementListToDto(map.getMapElements()));

    return staticLayer.build();
  }

  @Mapping(source = "entities", target = "entityList")
  LendyrWorld worldMapToDto(LocalMap localMap);
}
