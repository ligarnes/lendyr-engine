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
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaChangedEvent;
import net.alteiar.lendyr.model.persona.EquipmentStatus;
import net.alteiar.lendyr.model.persona.PersonaItem;

import java.util.List;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class DropItemAction implements GameAction {
  @Getter
  private final UUID characterId;
  @Getter
  private UUID targetId;
  @Getter
  private List<UUID> items;


  // Stateful variables
  PersonaEntity persona;

  @Builder
  public DropItemAction(@NonNull UUID characterId, @NonNull UUID sourceId, @NonNull List<UUID> items) {
    this.characterId = characterId;
    this.targetId = sourceId;
    this.items = items;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    // TODO verify persona is in range to the source
  }

  @Override
  public GameEvent apply(GameEntity gameEntity, DiceEngine diceEngine) {
    // Add all the items
    for (UUID itemId : items) {
      PersonaItem personaItem = PersonaItem.builder().itemId(itemId).quantity(1).status(EquipmentStatus.READY).build();
      persona.getInventory().addToBackpack(personaItem);
    }
    return PersonaChangedEvent.builder().persona(persona.toModel()).build();
  }

}
