package net.alteiar.lendyr.entity;

import net.alteiar.lendyr.model.items.Item;

import java.util.Optional;
import java.util.UUID;

public interface GameEntity {

  LocalMapEntity getMap();

  EncounterEntity getEncounter();

  Optional<PersonaEntity> findById(UUID personaId);

  Optional<Item> getItem(UUID id);

  PlayerEntity getPlayer();
}
