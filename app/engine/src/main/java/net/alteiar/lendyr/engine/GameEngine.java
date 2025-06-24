package net.alteiar.lendyr.engine;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.PlayState;
import net.alteiar.lendyr.model.encounter.CombatActor;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class GameEngine {
  private final GameContext gameContext;
  private final long maxThinkingTimeInMs;
  private final AtomicBoolean isRunning;
  private final CombatPersonaEngine combatPersonaEngine;
  private Thread gameEngineThread;

  private final RealTimeEngine realTimeEngine;

  @Builder
  GameEngine(@NonNull GameContext gameContext) {
    maxThinkingTimeInMs = 33L;
    this.gameContext = gameContext;
    this.isRunning = new AtomicBoolean(false);
    this.combatPersonaEngine = CombatPersonaEngine.builder().gameContext(gameContext).build();
    this.realTimeEngine = new RealTimeEngine(gameContext);
  }

  public void start() {
    this.isRunning.set(true);
    this.gameEngineThread = Thread.ofPlatform().name("game-engine").start(this::run);
  }

  public void stop() {
    this.isRunning.set(false);
    gameEngineThread.interrupt();
    try {
      gameEngineThread.join(Duration.ofSeconds(5));
    } catch (InterruptedException e) {
      log.warn(e);
    }
  }

  private void run() {
    long lastTimestamp = 0;
    while (isRunning.get()) {
      long before = System.currentTimeMillis();
      update((System.currentTimeMillis() - lastTimestamp) / 1000f);
      lastTimestamp = before;
      long duration = System.currentTimeMillis() - before;
      if (duration > maxThinkingTimeInMs) {
        log.warn("Update was too long; took {}ms", duration);
      } else {
        long remaining = maxThinkingTimeInMs - duration;
        try {
          Thread.sleep(remaining);
        } catch (InterruptedException e) {
          // Ignore
          log.debug("Interrupted while waiting for next computation", e);
        }
      }
    }
  }

  private void update(float delta) {
    if (gameContext.getGame().isGameOver()) {
      gameContext.notifyGameOver();
    }

    PlayState playState = gameContext.getGame().getPlayState();
    switch (playState) {
      case COMBAT -> updateCombat();
      case REAL_TIME -> realTimeEngine.update(delta);
      case GAME_OVER, PAUSE -> {
        // Nothing to do.
      }
      default -> throw new IllegalStateException("Play state %s is not supported yet".formatted(playState));
    }
  }

  private void updateCombat() {
    if (gameContext.getGame().getEncounter().isEncounterComplete()) {
      gameContext.changeGameStateChanged(PlayState.REAL_TIME);
      return;
    }

    CombatActor actor = gameContext.getGame().getEncounter().getCurrentPersona();
    if (!gameContext.getGame().getPlayer().isPlayerControlled(actor.getPersonaId())) {
      // Play only entities that player does not control
      gameContext.getGame().findById(actor.getPersonaId()).ifPresent(combatPersonaEngine::playTurn);
    }
  }
}
