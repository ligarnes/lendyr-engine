package net.alteiar.lendyr.server.grpc.v1.processor;

import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.GameContextListener;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameEvent;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.server.grpc.v1.mapper.ActionMapper;
import net.alteiar.lendyr.server.grpc.v1.mapper.GameStateMapper;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class CurrentStateProcessor implements GameContextListener {
  private final Object waitToken;
  private final GameContext gameContext;
  private final AtomicBoolean hasChanged;

  private final Object waitActionToken;
  private final LinkedList<LendyrGameEvent> actions;

  @Builder
  CurrentStateProcessor(@NonNull GameContext gameContext) {
    waitToken = new Object();
    this.gameContext = gameContext;
    this.hasChanged = new AtomicBoolean(true);

    this.waitActionToken = new Object();
    this.actions = new LinkedList<>();
  }

  @Override
  public void newAction(GameEvent action) {
    synchronized (waitActionToken) {
      LendyrGameEvent result = ActionMapper.INSTANCE.actionResultToDto(action);
      actions.add(result);
      waitActionToken.notifyAll();
    }
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

  public Optional<LendyrGameEvent> awaitNewAction(long timeoutMillis) throws InterruptedException {
    synchronized (waitActionToken) {
      if (actions.isEmpty()) {
        waitActionToken.wait(timeoutMillis);
      }
      if (!actions.isEmpty()) {
        return Optional.of(actions.pop());
      }
      return Optional.empty();
    }
  }

  public Optional<LendyrGameState> awaitNewState(long timeoutMillis) throws InterruptedException {
    synchronized (waitToken) {
      if (!hasChanged.get()) {
        waitToken.wait(timeoutMillis);
      }
      if (hasChanged.get()) {
        hasChanged.set(false);
        return Optional.of(currentGameState());
      }
      return Optional.empty();
    }
  }

  public LendyrGameState currentGameState() {
    return GameStateMapper.INSTANCE.businessToDto(gameContext.toModel());
  }
}
