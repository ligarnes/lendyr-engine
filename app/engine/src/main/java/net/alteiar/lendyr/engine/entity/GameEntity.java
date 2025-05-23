package net.alteiar.lendyr.engine.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.model.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameEntity {
  private final Map<UUID, PersonaEntity> personas;

  @Getter
  private final EncounterEntity encounter;

  @Getter
  private final GameContext gameContext;

  @Getter
  private final MapEntity map;
  
  @Builder
  public GameEntity(@NonNull GameContext gameContext) {
    this.gameContext = gameContext;
    this.personas = new HashMap<>();
    encounter = EncounterEntity.builder().gameContext(gameContext).build();
    map = MapEntity.builder().gameContext(gameContext).build();
  }

  public void load(@NonNull Game game) {
    game.getPersonas().forEach(p -> personas.put(p.getId(), PersonaEntity.builder().itemRepository(gameContext.getItemRepository()).persona(p).build()));
    encounter.load(game.getEncounter());
    map.load(game.getEncounter());
  }

  public Optional<PersonaEntity> findById(UUID personaId) {
    return Optional.ofNullable(personas.get(personaId));
  }

  public Game toModel() {
    return Game.builder()
        .personas(personas.values().stream().map(PersonaEntity::toModel).toList())
        .encounter(encounter.toModel())
        .build();
  }
}
