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

  public LendyrGameServer(File dataFolder) {
    RepositoryFactory repoFactory = new RepositoryFactory(dataFolder);
    gameContext = GameContext.builder().repositoryFactory(repoFactory).build();
  }

  public void load(String saveName) {
    gameContext.load(saveName);
  }

  public void run(int port) throws IOException, InterruptedException {
    // Load game
    GrpcServer grpcServer = GrpcServer.builder().gameContext(gameContext).build();
    grpcServer.start(port);

    grpcServer.blockUntilShutdown();
  }
}
