package net.alteiar.lendyr.server.grpc.v1.processor;

import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.GameContextListener;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.server.grpc.v1.mapper.GameStateMapper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class CurrentStateProcessor implements GameContextListener {
  private final Object waitToken;
  private final GameContext gameContext;
  private final AtomicBoolean hasChanged;

  @Builder
  CurrentStateProcessor(@NonNull GameContext gameContext) {
    waitToken = new Object();
    this.gameContext = gameContext;
    this.hasChanged = new AtomicBoolean(true);
  }

  @Override
  public void gameChanged() {
    hasChanged.set(true);
    synchronized (waitToken) {
      waitToken.notifyAll();
    }
  }

  public boolean isCompleted() {
    return false;
  }

  public Optional<LendyrGameState> awaitNewState(long timeoutMillis) throws InterruptedException {
    if (!hasChanged.get()) {
      synchronized (waitToken) {
        waitToken.wait(timeoutMillis);
      }
    }
    if (hasChanged.get()) {
      hasChanged.set(false);
      return Optional.of(currentGameState());
    }
    return Optional.empty();
  }

  public LendyrGameState currentGameState() {
    return GameStateMapper.INSTANCE.businessToDto(gameContext.getGame().toModel());
  }
}
