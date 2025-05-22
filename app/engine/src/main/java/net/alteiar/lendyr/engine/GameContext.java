package net.alteiar.lendyr.engine;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.engine.action.GameAction;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.entity.GameEntity;
import net.alteiar.lendyr.engine.entity.PersonaEntity;
import net.alteiar.lendyr.engine.entity.exception.ActionException;
import net.alteiar.lendyr.engine.entity.exception.ProcessingException;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.persona.Persona;
import net.alteiar.lendyr.persistence.ItemRepository;
import net.alteiar.lendyr.persistence.MapRepository;
import net.alteiar.lendyr.persistence.RepositoryFactory;
import net.alteiar.lendyr.persistence.SaveRepository;

import java.util.Optional;
import java.util.UUID;

public class GameContext {
  @Getter
  private GameEntity game;

  @Setter
  private GameContextListener listener;

  @Getter
  private final ItemRepository itemRepository;
  @Getter
  private final MapRepository mapRepository;
  private final SaveRepository saveRepository;

  @Builder
  GameContext(@NonNull RepositoryFactory repositoryFactory) {
    game = null;
    this.itemRepository = repositoryFactory.getItemRepository();
    this.mapRepository = repositoryFactory.getMapRepository();
    this.saveRepository = repositoryFactory.getSaveRepository();
  }

  public void load(String saveName) {
    Game loadedGame = saveRepository.load(saveName);
    this.game = GameEntity.builder().gameContext(this).build();
    this.game.load(loadedGame);
  }

  public void save(String saveName) {
    saveRepository.save(saveName, game.toModel());
  }

  public Optional<Persona> findById(UUID personaId) {
    return getGame().findById(personaId).map(PersonaEntity::toModel);
  }

  public ActionResult act(GameAction action) {
    try {
      action.ensureAllowed(this);
      ActionResult result = action.apply(this);
      if (result.hasWorldChanged()) {
        listener.gameChanged();
      }
      return result;
    } catch (ActionException e) {
      throw e;
    } catch (RuntimeException e) {
      throw new ProcessingException("Unexpected exception", e);
    }
  }
}
