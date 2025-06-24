package net.alteiar.lendyr.engine;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.engine.random.DiceEngineImpl;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntityImpl;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.exception.ActionException;
import net.alteiar.lendyr.entity.action.exception.ProcessingException;
import net.alteiar.lendyr.entity.event.CombatStartedEvent;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.GameModeChanged;
import net.alteiar.lendyr.entity.event.GameSaved;
import net.alteiar.lendyr.entity.npc.NpcEntity;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.PlayState;
import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.persona.Ability;
import net.alteiar.lendyr.model.persona.AbilityFocus;
import net.alteiar.lendyr.persistence.RepositoryFactory;
import net.alteiar.lendyr.persistence.SaveRepository;

import java.util.ArrayList;
import java.util.List;

public class GameContext {
  @Getter
  private final GameEntityImpl game;

  @Setter
  private GameContextListener listener;
  private final DiceEngine diceEngine;
  private final SaveRepository saveRepository;
  private final GameEngine gameEngine;

  @Builder
  GameContext(@NonNull RepositoryFactory repositoryFactory) {
    this.saveRepository = repositoryFactory.getSaveRepository();
    this.game = GameEntityImpl.builder().repositoryFactory(repositoryFactory).build();
    this.diceEngine = new DiceEngineImpl();
    this.gameEngine = GameEngine.builder().gameContext(this).build();
  }

  public void load(String saveName) {
    Game loadedGame = saveRepository.load(saveName);
    this.game.load(loadedGame);
    this.gameEngine.start();
  }

  public void stop() {
    this.gameEngine.stop();
    // auto-save on stop
    this.save("auto-save");
  }

  public void save(String saveName) {
    saveRepository.save(saveName, game.toModel());
    listener.newAction(new GameSaved(saveName));
  }

  public void resume() {
    this.game.resume();
    listener.newAction(new GameModeChanged(this.game.getPlayState()));
  }

  public void pause() {
    this.game.pause();
    listener.newAction(new GameModeChanged(this.game.getPlayState()));
  }

  public void notifyGameOver() {
    this.game.setPlayState(PlayState.GAME_OVER);
    listener.newAction(new GameModeChanged(this.game.getPlayState()));
  }

  public void changeGameStateChanged(PlayState playState) {
    this.game.setPlayState(playState);
    listener.newAction(new GameModeChanged(this.game.getPlayState()));
  }

  public void act(GameAction action) {
    try {
      action.ensureAllowed(this.game);
      List<GameEvent> result = action.apply(this.game, diceEngine);
      if (result != null) {
        result.forEach(listener::newAction);
      }
    } catch (ActionException e) {
      throw e;
    } catch (RuntimeException e) {
      throw new ProcessingException("Unexpected exception", e);
    }
  }

  public void notifyEvent(GameEvent event) {
    listener.newAction(event);
  }

  public void startEncounter(List<NpcEntity> enemies) {
    List<PersonaEntity> allies = getGame().getPlayer().getControlledPersonas();

    List<CombatActor> actors = new ArrayList<>();
    for (PersonaEntity persona : allies) {
      persona.setNextPosition(null);
      persona.setTargetPosition(null);
      int initScore = persona.getAbility(Ability.DEXTERITY).getValue(AbilityFocus.INITIATIVE);
      actors.add(new CombatActor(persona.getId(), initScore, 1));
    }

    for (NpcEntity persona : enemies) {
      persona.getPersona().setNextPosition(null);
      persona.getPersona().setTargetPosition(null);
      int initScore = persona.getPersona().getAbility(Ability.DEXTERITY).getValue(AbilityFocus.INITIATIVE);
      actors.add(new CombatActor(persona.getPersona().getId(), initScore, 2));
    }

    this.getGame().getEncounter().newEncounter(actors);

    this.notifyEvent(new CombatStartedEvent(this.getGame().getEncounter()));
    this.changeGameStateChanged(PlayState.COMBAT);
  }

  public Game toModel() {
    return this.game.toModel();
  }
}
