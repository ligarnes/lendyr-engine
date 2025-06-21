package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.alteiar.lendyr.grpc.model.v1.persona.*;
import net.alteiar.lendyr.model.persona.*;
import net.alteiar.lendyr.model.persona.Persona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PersonaMapperTest {

  @Test
  void personaToDto() throws JsonProcessingException {
    Persona persona = RandomProvider.INSTANCE.nextObject(Persona.class);
    persona.setPosition(new Position(4.2f, 5.3f, 1));

    for (EquippedLocation location : EquippedLocation.values()) {
      persona.getEquipped().equip(location, RandomProvider.INSTANCE.nextObject(PersonaItem.class));
    }

    LendyrPersona dto = PersonaMapper.INSTANCE.personaToDto(persona);

    Assertions.assertEquals(persona.getName(), dto.getName());
    Assertions.assertEquals(persona.getPortraitPath(), dto.getPortraitPath());
    Assertions.assertEquals(persona.getTokenPath(), dto.getTokenPath());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(persona.getId()), dto.getId());
    Assertions.assertEquals(persona.getCurrentHealthPoint(), dto.getCurrentHealthPoint());
    Assertions.assertEquals(persona.getHealthPoint(), dto.getHealthPoint());
    Assertions.assertEquals(persona.getDefense(), dto.getDefense());
    Assertions.assertEquals(persona.getArmorRating(), dto.getArmorRating());
    Assertions.assertEquals(persona.getArmorPenalty(), dto.getArmorPenalty());
    Assertions.assertEquals(persona.getSpeed(), dto.getSpeed());

    Assertions.assertEquals(persona.getSize().getWidth(), dto.getSize().getWidth());
    Assertions.assertEquals(persona.getSize().getHeight(), dto.getSize().getHeight());

    Assertions.assertEquals(persona.getPosition().getX(), dto.getPosition().getX());
    Assertions.assertEquals(persona.getPosition().getY(), dto.getPosition().getY());
    Assertions.assertEquals(persona.getPosition().getLayer(), dto.getPosition().getLayer());

    assertEquals(persona.getAbilities().getAccuracy(), dto.getAbilities().getAccuracy());
    assertEquals(persona.getAbilities().getCommunication(), dto.getAbilities().getCommunication());
    assertEquals(persona.getAbilities().getConstitution(), dto.getAbilities().getConstitution());
    assertEquals(persona.getAbilities().getDexterity(), dto.getAbilities().getDexterity());
    assertEquals(persona.getAbilities().getFighting(), dto.getAbilities().getFighting());
    assertEquals(persona.getAbilities().getIntelligence(), dto.getAbilities().getIntelligence());
    assertEquals(persona.getAbilities().getPerception(), dto.getAbilities().getPerception());
    assertEquals(persona.getAbilities().getStrength(), dto.getAbilities().getStrength());
    assertEquals(persona.getAbilities().getWillpower(), dto.getAbilities().getWillpower());

    assertEquipped(persona.getEquipped(), dto.getEquipped());
    assertInventory(persona.getInventory(), dto.getInventory());
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