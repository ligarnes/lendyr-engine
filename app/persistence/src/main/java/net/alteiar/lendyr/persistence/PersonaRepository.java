package net.alteiar.lendyr.persistence;

import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.persona.Persona;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to retrieve the persona in the game.
 */
@Log4j2
public class PersonaRepository {

  private final JsonMapper jsonMapper;

  PersonaRepository(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public Optional<Persona> findById(UUID id) {
    try {
      return Optional.of(jsonMapper.load("./persona/persona-%s.json".formatted(id), Persona.class));
    } catch (RuntimeException e) {
      log.warn("Failed to load persona", e);
      return Optional.empty();
    }
  }
}
