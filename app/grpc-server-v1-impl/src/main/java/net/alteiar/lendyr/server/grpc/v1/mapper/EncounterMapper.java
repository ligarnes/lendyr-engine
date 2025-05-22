package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrCombatState;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrCurrentInitiative;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrEncounter;
import net.alteiar.lendyr.model.encounter.CurrentPersona;
import net.alteiar.lendyr.model.encounter.Encounter;
import net.alteiar.lendyr.model.encounter.EncounterState;
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

  LendyrEncounter businessToDto(Encounter encounter);

  @Mapping(source = "turn", target = "currentTurn")
  @Mapping(source = "currentPersona", target = "active")
  @Mapping(source = "initiative", target = "initiativeOrderList")
  LendyrCombatState stateToDto(EncounterState encouterState);

  @Mapping(source = "initiativeIdx", target = "activePersonaIdx")
  LendyrCurrentInitiative businessToDto(CurrentPersona currentPersona);
}
