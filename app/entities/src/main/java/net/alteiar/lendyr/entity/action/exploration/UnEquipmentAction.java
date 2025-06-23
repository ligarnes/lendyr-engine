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
import net.alteiar.lendyr.model.persona.EquippedLocation;
import net.alteiar.lendyr.model.persona.PersonaItem;

import java.util.List;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
@ToString
public class UnEquipmentAction implements GameAction {
  @Getter
  private final UUID characterId;
  private final EquippedLocation location;

  // Stateful variables
  PersonaEntity persona;

  @Builder
  public UnEquipmentAction(@NonNull UUID characterId, EquippedLocation location) {
    this.characterId = characterId;
    this.location = location;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));


    PersonaItem item = persona.getEquipped().get(location);
    if (item == null) {
      throw new NotAllowedException("No item equipped to remove");
    }
  }

  @Override
  public List<GameEvent> apply(GameEntity gameEntity, DiceEngine diceEngine) {

    PersonaItem personaItem = persona.getEquipped().removeFrom(location);
    persona.getInventory().addToBackpack(personaItem);

    return List.of(PersonaChangedEvent.builder().persona(persona.toModel()).build());
  }

}
