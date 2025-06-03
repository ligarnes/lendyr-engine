package net.alteiar.lendyr.entity;

import java.util.Optional;
import java.util.UUID;

public interface GameEntity {
  
  LocalMapEntity getMap();

  EncounterEntity getEncounter();

  Optional<PersonaEntity> findById(UUID personaId);
}
