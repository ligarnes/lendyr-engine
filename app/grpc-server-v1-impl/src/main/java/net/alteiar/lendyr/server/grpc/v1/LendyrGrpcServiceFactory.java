package net.alteiar.lendyr.server.grpc.v1;

import io.grpc.BindableService;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.engine.GameContext;

import java.util.List;

@Value
public class LendyrGrpcServiceFactory {

  LendyrGameServiceImpl gameServiceImpl;

  @Builder
  LendyrGrpcServiceFactory(@NonNull GameContext gameContext) {
    gameServiceImpl = LendyrGameServiceImpl.builder().gameContext(gameContext).build();
  }

  public List<BindableService> getBindableServices() {
    return List.of(gameServiceImpl);
  }
}
