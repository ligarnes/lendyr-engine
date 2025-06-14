package net.alteiar.lendyr.server.grpc.v1.processor;

import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.GameContextListener;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameEvent;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.server.grpc.v1.mapper.EventMapper;
import net.alteiar.lendyr.server.grpc.v1.mapper.GameStateMapper;

import java.util.LinkedList;
import java.util.Optional;

public class CurrentStateProcessor implements GameContextListener {
  private final GameContext gameContext;

  private final Object waitActionToken;
  private final LinkedList<LendyrGameEvent> actions;

  @Builder
  CurrentStateProcessor(@NonNull GameContext gameContext) {
    this.gameContext = gameContext;

    this.waitActionToken = new Object();
    this.actions = new LinkedList<>();
  }

  @Override
  public void newAction(GameEvent action) {
    synchronized (waitActionToken) {
      LendyrGameEvent result = EventMapper.INSTANCE.eventResultToDto(action);
      actions.add(result);
      waitActionToken.notifyAll();
    }
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

  public LendyrGameState currentGameState() {
    return GameStateMapper.INSTANCE.businessToDto(gameContext.toModel());
  }
}
