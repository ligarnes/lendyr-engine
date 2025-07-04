package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.PlayState;
import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.persona.Persona;
import net.alteiar.lendyr.persistence.ItemRepository;
import net.alteiar.lendyr.persistence.MapRepository;
import net.alteiar.lendyr.persistence.PersonaRepository;
import net.alteiar.lendyr.persistence.RepositoryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameEntityImpl implements GameEntity {
  @Getter
  private final ItemRepository itemRepository;
  @Getter
  private final MapRepository mapRepository;
  @Getter
  private final PersonaRepository personaRepository;


  @Getter
  private PlayerEntity player;
  @Setter
  @Getter
  private PlayState playState;

  // Cached
  private final Map<UUID, PersonaEntity> personas;

  @Getter
  private final EncounterEntity encounter;
  @Getter
  private final LocalMapEntity map;

  @Builder
  public GameEntityImpl(@NonNull RepositoryFactory repositoryFactory) {
    this.personas = new HashMap<>();
    encounter = new EncounterEntityImpl(this);

    itemRepository = repositoryFactory.getItemRepository();
    mapRepository = repositoryFactory.getMapRepository();
    personaRepository = repositoryFactory.getPersonaRepository();
    map = LocalMapEntity.builder().gameEntity(this).build();
  }

  public boolean isGameOver() {
    return player.getControlledPersonas().stream().allMatch(PersonaEntity::isDefeated);
  }

  public void pause() {
    this.playState = PlayState.PAUSE;
  }

  public void resume() {
    if (!encounter.isEncounterComplete()) {
      this.playState = PlayState.COMBAT;
    } else {
      this.playState = PlayState.REAL_TIME;
    }
  }

  public void load(@NonNull Game game) {
    player = new PlayerEntity(this, game.getPlayer());
    encounter.setEncounter(game.getEncounter());
    map.load(game.getLocalMap(), mapRepository.findMapById(game.getLocalMap().getMapId()));
    playState = PlayState.PAUSE;
  }

  public Optional<PersonaEntity> findById(UUID personaId) {
    // Load entity on demand
    personas.compute(personaId, (id, entity) -> {
      if (entity == null) {
        Optional<Persona> found = personaRepository.findById(id);
        if (found.isPresent()) {
          entity = new PersonaEntity(itemRepository, found.get());
        }
      }
      return entity;
    });

    return Optional.ofNullable(personas.get(personaId));
  }

  @Override
  public Optional<Item> getItem(UUID id) {
    return itemRepository.findById(id);
  }

  public Game toModel() {
    return Game.builder()
        .encounter(encounter.toModel())
        .localMap(map.toModel())
        .player(player.toModel())
        .playState(playState)
        .build();
  }
}
