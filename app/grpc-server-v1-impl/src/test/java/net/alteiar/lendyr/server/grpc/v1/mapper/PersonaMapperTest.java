package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.grpc.model.v1.persona.*;
import net.alteiar.lendyr.model.persona.*;
import net.alteiar.lendyr.model.persona.Persona;
import net.alteiar.lendyr.persistence.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PersonaMapperTest {

  @Test
  void personaToDto() {
    Persona persona = RandomProvider.INSTANCE.nextObject(Persona.class);
    persona.setPosition(new Position(4.2f, 5.3f, 1));

    for (EquippedLocation location : EquippedLocation.values()) {
      persona.getEquipped().equip(location, RandomProvider.INSTANCE.nextObject(PersonaItem.class));
    }

    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    PersonaEntity personaEntity = PersonaEntity.builder().persona(persona).itemRepository(itemRepository).build();

    LendyrPersona dto = PersonaMapper.INSTANCE.personaToDto(personaEntity);

    Assertions.assertEquals(personaEntity.getName(), dto.getName());
    Assertions.assertEquals(personaEntity.getPortraitPath(), dto.getPortraitPath());
    Assertions.assertEquals(personaEntity.getTokenPath(), dto.getTokenPath());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(persona.getId()), dto.getId());
    Assertions.assertEquals(personaEntity.getCurrentHealthPoint(), dto.getCurrentHealthPoint());
    Assertions.assertEquals(personaEntity.getHealthPoint(), dto.getHealthPoint());
    Assertions.assertEquals(personaEntity.getDefense(), dto.getDefense());
    Assertions.assertEquals(personaEntity.getArmorRating(), dto.getArmorRating());
    Assertions.assertEquals(personaEntity.getArmorPenalty(), dto.getArmorPenalty());
    Assertions.assertEquals(personaEntity.getSpeed(), dto.getSpeed());

    Assertions.assertEquals(personaEntity.getSize().getWidth(), dto.getSize().getWidth());
    Assertions.assertEquals(personaEntity.getSize().getHeight(), dto.getSize().getHeight());

    Assertions.assertEquals(personaEntity.getPosition().getX(), dto.getPosition().getX());
    Assertions.assertEquals(personaEntity.getPosition().getY(), dto.getPosition().getY());
    Assertions.assertEquals(personaEntity.getPosition().getLayer(), dto.getPosition().getLayer());

    assertEquals(personaEntity.getAbility(Ability.ACCURACY), dto.getAbilities().getAccuracy());
    assertEquals(personaEntity.getAbility(Ability.COMMUNICATION), dto.getAbilities().getCommunication());
    assertEquals(personaEntity.getAbility(Ability.CONSTITUTION), dto.getAbilities().getConstitution());
    assertEquals(personaEntity.getAbility(Ability.DEXTERITY), dto.getAbilities().getDexterity());
    assertEquals(personaEntity.getAbility(Ability.FIGHTING), dto.getAbilities().getFighting());
    assertEquals(personaEntity.getAbility(Ability.INTELLIGENCE), dto.getAbilities().getIntelligence());
    assertEquals(personaEntity.getAbility(Ability.PERCEPTION), dto.getAbilities().getPerception());
    assertEquals(personaEntity.getAbility(Ability.STRENGTH), dto.getAbilities().getStrength());
    assertEquals(personaEntity.getAbility(Ability.WILLPOWER), dto.getAbilities().getWillpower());

    Assertions.assertFalse(personaEntity.getEquipped().toList().isEmpty());
    assertEquipped(personaEntity.getEquipped(), dto.getEquipped());

    Assertions.assertFalse(personaEntity.getInventory().getBackpack().isEmpty());
    assertInventory(personaEntity.getInventory(), dto.getInventory());
  }

  private void assertEquipped(PersonaEquipped expected, LendyrPersonaEquipped actual) {
    assertPersonaEquipped(expected.get(EquippedLocation.HAND_1), actual.getRightHand());
    assertPersonaEquipped(expected.get(EquippedLocation.HAND_2), actual.getLeftHand());
    assertPersonaEquipped(expected.get(EquippedLocation.ARMOR), actual.getArmor());
    assertPersonaEquipped(expected.get(EquippedLocation.BELT), actual.getBelt());
    assertPersonaEquipped(expected.get(EquippedLocation.GLOVES), actual.getGloves());
    assertPersonaEquipped(expected.get(EquippedLocation.NECKLESS), actual.getNeckless());
    assertPersonaEquipped(expected.get(EquippedLocation.RING_1), actual.getRing1());
    assertPersonaEquipped(expected.get(EquippedLocation.RING_2), actual.getRing2());
    assertPersonaEquipped(expected.get(EquippedLocation.AMMO), actual.getAmmo());
    assertPersonaEquipped(expected.get(EquippedLocation.BOOTS), actual.getBoots());
    assertPersonaEquipped(expected.get(EquippedLocation.CLOAK), actual.getCloak());
    assertPersonaEquipped(expected.get(EquippedLocation.PANTS), actual.getPants());
  }

  private void assertPersonaEquipped(PersonaItem expected, LendyrPersonaEquipment actual) {
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(expected.getItemId()), actual.getItemId());
    Assertions.assertEquals(expected.getQuantity(), actual.getQuantity());
    Assertions.assertEquals(expected.getStatus().name(), actual.getStatus().name());
  }

  private void assertInventory(Inventory expected, LendyrPersonaInventory actual) {
    Assertions.assertEquals(expected.getBackpack().size(), actual.getBackpackCount());
    for (int i = 0; i < expected.getBackpack().size(); i++) {
      assertPersonaEquipped(expected.getBackpack().get(i), actual.getBackpack(i));
    }
  }

  private void assertEquals(AbilityStat expected, LendyrAbilityValue actual) {
    Assertions.assertEquals(expected.getValue(), actual.getValue());
    Assertions.assertEquals(expected.isPrimary(), actual.getIsPrimary());
    Assertions.assertEquals(expected.getFocuses().size(), actual.getFocusesCount());

    for (int i = 0; i < expected.getFocuses().size(); i++) {
      Assertions.assertEquals(expected.getFocuses().get(i).name(), actual.getFocuses(i).name());
    }
  }
}