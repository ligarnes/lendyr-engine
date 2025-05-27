package net.alteiar.lendyr.app;

import lombok.Data;
import net.alteiar.lendyr.app.grpc.GrpcServer;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.persistence.RepositoryFactory;

import java.io.File;
import java.io.IOException;

@Data
public class LendyrGameServer {

  private GameContext gameContext;
  private GrpcServer grpcServer;

  public LendyrGameServer(File dataFolder) {
    RepositoryFactory repoFactory = new RepositoryFactory(dataFolder);
    gameContext = GameContext.builder().repositoryFactory(repoFactory).build();
  }

  public void load(String saveName) {
    gameContext.load(saveName);
  }

  public void run(int port) throws IOException {
    // Load game
    grpcServer = GrpcServer.builder().gameContext(gameContext).build();
    grpcServer.start(port);
  }

  public void blockUntilShutdown() throws InterruptedException {
    grpcServer.blockUntilShutdown();
  }

  public void shutdown() throws InterruptedException {
    grpcServer.stop();
  }
}
