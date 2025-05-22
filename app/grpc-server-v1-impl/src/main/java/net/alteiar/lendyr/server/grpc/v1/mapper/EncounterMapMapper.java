package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.model.encounter.EncounterMap;
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
public interface EncounterMapMapper {
  EncounterMapMapper INSTANCE = Mappers.getMapper(EncounterMapMapper.class);

  /**
   * Convert the map to dto.
   *
   * @param map the map
   * @return the dto
   */
  @Mapping(source = "walls", target = "wallsList")
  LendyrMap mapToDto(EncounterMap map);
}
