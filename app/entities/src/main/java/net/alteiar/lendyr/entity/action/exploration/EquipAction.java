package net.alteiar.lendyr.entity.action.exploration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaChangedEvent;
import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.items.Weapon;
import net.alteiar.lendyr.model.persona.Ability;
import net.alteiar.lendyr.model.persona.EquippedLocation;
import net.alteiar.lendyr.model.persona.PersonaItem;

import java.util.List;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class EquipAction implements GameAction {
  @Getter
  private final UUID characterId;
  @Getter
  private final EquippedLocation location;
  @Getter
  private final UUID itemId;

  // Stateful variables
  PersonaEntity persona;
  PersonaItem personaItem;
  Item item;

  @Builder
  public EquipAction(@NonNull UUID characterId, EquippedLocation location, UUID itemId) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.location = location;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    personaItem = persona.getInventory().getBackpack().stream().filter(p -> p.getItemId().equals(itemId)).findFirst()
        .orElseThrow(() -> new NotAllowedException("Cannot equip something the character does not possess"));

    Item item = context.getItem(itemId)
        .orElseThrow(() -> new NotFoundException(String.format("the item with id [%s] does not exists", itemId)));

    switch (item.getItemType()) {
      case Item.ARMOR:
        if (location != EquippedLocation.ARMOR) {
          throw new NotAllowedException("Cannot equip something that is not the armor");
        }
        break;
      case Item.NECKLESS:
        if (location != EquippedLocation.NECKLESS) {
          throw new NotAllowedException("Cannot equip something that is not the neckless equip");
        }
        break;
      case Item.BOOTS:
        if (location != EquippedLocation.BOOTS) {
          throw new NotAllowedException("Cannot equip something that is not the boots equip");
        }
        break;
      case Item.CLOAK:
        if (location != EquippedLocation.CLOAK) {
          throw new NotAllowedException("Cannot equip something that is not the cloak equip");
        }
        break;
      case Item.GLOVES:
        if (location != EquippedLocation.GLOVES) {
          throw new NotAllowedException("Cannot equip something that is not the gloves equip");
        }
        break;
      case Item.RING:
        if (location != EquippedLocation.RING_1 && location != EquippedLocation.RING_2) {
          throw new NotAllowedException("Cannot equip something that is not the ring equip");
        }
        break;
      case Item.AMMO:
        if (location != EquippedLocation.AMMO) {
          throw new NotAllowedException("Cannot equip something that is not the ammo equip");
        }
        break;
      case Item.WEAPON:
        if (location != EquippedLocation.HAND_1 && location != EquippedLocation.HAND_2) {
          throw new NotAllowedException("Cannot equip something that is not the hand equip");
        }
        break;
      default:
        throw new NotAllowedException("Cannot equip items");
    }

    if (item instanceof Weapon weapon) {
      if (persona.getAbility(Ability.STRENGTH).getValue() < weapon.getMinStr()) {
        throw new NotAllowedException("Not enough strength");
      }
    }
  }

  @Override
  public List<GameEvent> apply(GameEntity gameEntity, DiceEngine diceEngine) {
    PersonaItem found = persona.getEquipped().get(location);
    persona.getEquipped().equip(location, personaItem);
    persona.getInventory().removeItem(itemId);
    if (found != null) {
      persona.getInventory().addToBackpack(found);
    }
    return List.of(PersonaChangedEvent.builder().persona(persona).build());
  }
}
