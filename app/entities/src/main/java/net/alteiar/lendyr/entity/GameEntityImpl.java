package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.PlayState;
import net.alteiar.lendyr.model.Player;
import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.persistence.ItemRepository;
import net.alteiar.lendyr.persistence.MapRepository;
import net.alteiar.lendyr.persistence.RepositoryFactory;

import java.util.*;

public class GameEntityImpl implements GameEntity {
  private final Map<UUID, PersonaEntity> personas;

  @Getter
  private final ItemRepository itemRepository;
  @Getter
  private final MapRepository mapRepository;
  @Getter
  private final EncounterEntity encounter;
  @Getter
  private final LocalMapEntity map;

  @Getter
  private Player player;
  @Setter
  @Getter
  private PlayState playState;

  @Builder
  public GameEntityImpl(@NonNull RepositoryFactory repositoryFactory) {
    this.personas = new HashMap<>();
    encounter = new EncounterEntity(this);

    itemRepository = repositoryFactory.getItemRepository();
    mapRepository = repositoryFactory.getMapRepository();
    map = LocalMapEntity.builder().gameEntity(this).build();
  }

  public boolean isGameOver() {
    return player.getControlledPersonaIds().stream().map(personas::get).filter(Objects::nonNull).allMatch(PersonaEntity::isDefeated);
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
    game.getPersonas().forEach(p -> personas.put(p.getId(), PersonaEntity.builder().itemRepository(itemRepository).persona(p).build()));
    encounter.setEncounter(game.getEncounter());
    map.load(game.getLocalMap(), mapRepository.findMapById(game.getLocalMap().getMapId()));
    player = game.getPlayer();
    playState = PlayState.PAUSE;
  }

  public Optional<PersonaEntity> findById(UUID personaId) {
    return Optional.ofNullable(personas.get(personaId));
  }

  @Override
  public Optional<Item> getItem(UUID id) {
    return itemRepository.findById(id);
  }

  public Game toModel() {
    return Game.builder()
        .personas(personas.values().stream().map(PersonaEntity::toModel).toList())
        .encounter(encounter.toModel())
        .localMap(map.toModel())
        .player(player)
        .playState(playState)
        .build();
  }
}
