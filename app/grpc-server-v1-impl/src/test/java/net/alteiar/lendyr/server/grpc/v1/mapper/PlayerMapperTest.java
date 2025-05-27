package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.player.LendyrPlayer;
import net.alteiar.lendyr.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlayerMapperTest {

  @Test
  void businessToDto() {
    Player player = RandomProvider.INSTANCE.nextObject(Player.class);

    LendyrPlayer dto = PlayerMapper.INSTANCE.businessToDto(player);

    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(player.getId()), dto.getId());
    Assertions.assertEquals(player.getName(), dto.getName());
    Assertions.assertEquals(player.getControlledPersonaIds().size(), dto.getControlledPersonaIdsCount());
    for (int i = 0; i < player.getControlledPersonaIds().size(); i++) {
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(player.getControlledPersonaIds().get(i)), dto.getControlledPersonaIds(i));
    }
  }
}