package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.persona.LendyrAbilities;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrAbilityValue;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrPersona;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrPersonaInventory;
import net.alteiar.lendyr.model.persona.Abilities;
import net.alteiar.lendyr.model.persona.AbilityStat;
import net.alteiar.lendyr.model.persona.Inventory;
import net.alteiar.lendyr.model.persona.Persona;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {GenericMapper.class, ItemMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PersonaMapper {
  PersonaMapper INSTANCE = Mappers.getMapper(PersonaMapper.class);

  LendyrPersona personaToDto(Persona character);

  @Mapping(source = "backpack", target = "backpackList")
  LendyrPersonaInventory inventoryToDto(Inventory inventory);

  LendyrAbilities abilitiesToDto(Abilities abilities);

  @Mapping(source = "focuses", target = "focusesList")
  LendyrAbilityValue abilityValueToDto(AbilityStat ability);
}
