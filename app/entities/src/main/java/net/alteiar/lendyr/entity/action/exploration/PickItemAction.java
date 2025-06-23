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
import net.alteiar.lendyr.entity.event.exploration.ItemContainerChangedEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaChangedEvent;
import net.alteiar.lendyr.model.map.ItemContainer;
import net.alteiar.lendyr.model.map.layered.DynamicBlockingObject;
import net.alteiar.lendyr.model.persona.EquipmentStatus;
import net.alteiar.lendyr.model.persona.PersonaItem;

import java.util.List;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class PickItemAction implements GameAction {
  @Getter
  private final UUID characterId;
  @Getter
  private UUID sourceId;
  @Getter
  private List<UUID> items;

  // Stateful variables
  PersonaEntity persona;
  ItemContainer container;

  @Builder
  public PickItemAction(@NonNull UUID characterId, @NonNull UUID sourceId, @NonNull List<UUID> items) {
    this.characterId = characterId;
    this.sourceId = sourceId;
    this.items = items;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    container = context.getMap().getItemContainerById(sourceId)
        .orElseThrow(() -> new NotFoundException(String.format("the container with id [%s] does not exists", characterId)));

    DynamicBlockingObject reach = persona.getPickReach();
    DynamicBlockingObject containerSize = container.getBoundingBox();
    if (!reach.overlap(containerSize)) {
      throw new NotAllowedException("The container is out of reach");
    }

    for (UUID itemId : items) {
      if (!container.getItems().contains(itemId)) {
        throw new NotFoundException(String.format("the item with id [%s] is not in the container", itemId));
      }
    }
  }

  @Override
  public List<GameEvent> apply(GameEntity gameEntity, DiceEngine diceEngine) {
    // Add all the items
    for (UUID itemId : items) {
      PersonaItem personaItem = PersonaItem.builder().itemId(itemId).quantity(1).status(EquipmentStatus.READY).build();
      persona.getInventory().addToBackpack(personaItem);
      container.getItems().remove(itemId);
    }

    return List.of(
        PersonaChangedEvent.builder().persona(persona).build(),
        ItemContainerChangedEvent.builder().itemContainer(container).build()
    );
  }

}
