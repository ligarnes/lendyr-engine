package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrWorld;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;
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
public interface WorldMapMapper {
  WorldMapMapper INSTANCE = Mappers.getMapper(WorldMapMapper.class);

  LendyrMap mapToDto(GameMap gameMap);

  @Mapping(source = "entities", target = "entityList")
  LendyrWorld worldMapToDto(LocalMap localMap);
}
