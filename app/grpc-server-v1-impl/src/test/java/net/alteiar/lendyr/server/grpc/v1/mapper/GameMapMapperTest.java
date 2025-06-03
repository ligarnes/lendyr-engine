package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrWorld;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameMapMapperTest {

  @Test
  void mapToDto() {
    GameMap gameMap = RandomProvider.INSTANCE.nextObject(GameMap.class);

    LendyrMap map = WorldMapMapper.INSTANCE.mapToDto(gameMap);

    Assertions.assertEquals(gameMap.getPath(), map.getPath());
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
}