package net.alteiar.lendyr.test.api;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.alteiar.lendyr.app.LendyrGameServer;
import net.alteiar.lendyr.grpc.model.v1.game.EmptyResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrPlayState;
import net.alteiar.lendyr.server.grpc.v1.mapper.GenericMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GameProcessTest {

  @BeforeAll
  static void beforeAll() throws IOException, InterruptedException {
    File dataFolder = new File("../../assembly/data");
    LendyrGameServer server = new LendyrGameServer(dataFolder);
    server.load("dummy-test-1");
    server.run(48081);
  }

  private LendyrGameServiceGrpc.LendyrGameServiceBlockingV2Stub blockingStub;

  @BeforeEach
  void beforeEach() {
    String target = "localhost:48081";
    ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
        .build();

    blockingStub = LendyrGameServiceGrpc.newBlockingV2Stub(channel);
  }

  @Test
  void waitForPlay() {
    // Given
    UUID mapId = UUID.fromString("f2659d86-a181-47a5-b201-1e6b2b48b94f");
    // When
    blockingStub.resume(EmptyResponse.newBuilder().build());

    // Then
    Awaitility.await().untilAsserted(() -> {
      LendyrGameState state = blockingStub.currentState(EmptyResponse.newBuilder().build());
      Assertions.assertEquals(3, state.getPersonaCount());
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(mapId), state.getMap().getMapId());
      Assertions.assertEquals(LendyrPlayState.COMBAT, state.getPlayState());
      Assertions.assertEquals(3, state.getEncounter().getInitiativeOrderCount());
    });
  }
}
