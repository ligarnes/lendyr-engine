package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrCurrentInitiative;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrEncounter;
import net.alteiar.lendyr.model.encounter.CurrentPersona;
import net.alteiar.lendyr.model.encounter.Encounter;
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
public interface EncounterMapper {
  EncounterMapper INSTANCE = Mappers.getMapper(EncounterMapper.class);

  @Mapping(source = "turn", target = "currentTurn")
  @Mapping(source = "currentPersona", target = "active")
  @Mapping(source = "initiative", target = "initiativeOrderList")
  LendyrEncounter businessToDto(Encounter encounter);

  @Mapping(source = "initiativeIdx", target = "activePersonaIdx")
  LendyrCurrentInitiative businessToDto(CurrentPersona currentPersona);
}
