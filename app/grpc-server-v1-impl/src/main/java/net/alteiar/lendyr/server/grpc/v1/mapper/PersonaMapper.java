package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.grpc.model.v1.persona.*;
import net.alteiar.lendyr.model.persona.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(
    uses = {GenericMapper.class, ItemMapper.class},
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PersonaMapper {
  PersonaMapper INSTANCE = Mappers.getMapper(PersonaMapper.class);

  @Mapping(source = "healthPoint", target = "healthPoint")
  LendyrPersona personaToDto(PersonaEntity character);

  default LendyrPersonaEquipped equippedToDto(PersonaEquipped equipped) {
    LendyrPersonaEquipped.Builder builder = LendyrPersonaEquipped.newBuilder();

    Optional.ofNullable(equipped.get(EquippedLocation.ARMOR)).map(this::equipmentToDto).ifPresent(builder::setArmor);
    Optional.ofNullable(equipped.get(EquippedLocation.AMMO)).map(this::equipmentToDto).ifPresent(builder::setAmmo);
    Optional.ofNullable(equipped.get(EquippedLocation.CLOAK)).map(this::equipmentToDto).ifPresent(builder::setCloak);
    Optional.ofNullable(equipped.get(EquippedLocation.BOOTS)).map(this::equipmentToDto).ifPresent(builder::setBoots);
    Optional.ofNullable(equipped.get(EquippedLocation.GLOVES)).map(this::equipmentToDto).ifPresent(builder::setGloves);
    Optional.ofNullable(equipped.get(EquippedLocation.RING_1)).map(this::equipmentToDto).ifPresent(builder::setRing1);
    Optional.ofNullable(equipped.get(EquippedLocation.RING_2)).map(this::equipmentToDto).ifPresent(builder::setRing2);
    Optional.ofNullable(equipped.get(EquippedLocation.NECKLESS)).map(this::equipmentToDto).ifPresent(builder::setNeckless);
    Optional.ofNullable(equipped.get(EquippedLocation.PANTS)).map(this::equipmentToDto).ifPresent(builder::setPants);
    Optional.ofNullable(equipped.get(EquippedLocation.HAND_1)).map(this::equipmentToDto).ifPresent(builder::setRightHand);
    Optional.ofNullable(equipped.get(EquippedLocation.HAND_2)).map(this::equipmentToDto).ifPresent(builder::setLeftHand);
    Optional.ofNullable(equipped.get(EquippedLocation.BELT)).map(this::equipmentToDto).ifPresent(builder::setBelt);

    return builder.build();
  }

  LendyrPersonaEquipment equipmentToDto(PersonaItem item);

  @Mapping(source = "backpack", target = "backpackList")
  LendyrPersonaInventory inventoryToDto(Inventory inventory);

  LendyrAbilities abilitiesToDto(Abilities abilities);

  @Mapping(source = "primary", target = "isPrimary")
  @Mapping(source = "focuses", target = "focusesList")
  LendyrAbilityValue abilityValueToDto(AbilityStat ability);
}
