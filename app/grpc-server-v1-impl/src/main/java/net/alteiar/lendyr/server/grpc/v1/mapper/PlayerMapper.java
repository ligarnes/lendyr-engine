package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.player.LendyrPlayer;
import net.alteiar.lendyr.model.Player;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = GenericMapper.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PlayerMapper {
  PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

  @Mapping(source = "controlledPersonaIds", target = "controlledPersonaIdsList")
  LendyrPlayer businessToDto(Player player);
}
