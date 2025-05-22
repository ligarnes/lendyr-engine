package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.model.encounter.EncounterMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncounterMapMapperTest {

  @Test
  void mapToDto() {
    EncounterMap encounter = RandomProvider.INSTANCE.nextObject(EncounterMap.class);

    LendyrMap map = EncounterMapMapper.INSTANCE.mapToDto(encounter);

    Assertions.assertEquals(encounter.getMapPath(), map.getMapPath());
    Assertions.assertEquals(encounter.getHeightInPixel(), map.getHeightInPixel());
    Assertions.assertEquals(encounter.getWidthInPixel(), map.getWidthInPixel());
    Assertions.assertEquals(encounter.getPixelPerMeter(), map.getPixelPerMeter());
    Assertions.assertEquals(encounter.getWalls().size(), map.getWallsCount());

    for (int i = 0; i < encounter.getWalls().size(); i++) {
      Assertions.assertEquals(encounter.getWalls().get(i).x, map.getWalls(i).getX());
      Assertions.assertEquals(encounter.getWalls().get(i).y, map.getWalls(i).getY());
      Assertions.assertEquals(encounter.getWalls().get(i).width, map.getWalls(i).getWidth());
      Assertions.assertEquals(encounter.getWalls().get(i).height, map.getWalls(i).getHeight());
    }
  }
}