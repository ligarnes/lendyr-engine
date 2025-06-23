package net.alteiar.lendyr.test.api;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.alteiar.lendyr.app.LendyrGameServer;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameMode;
import net.alteiar.lendyr.grpc.model.v1.game.EmptyResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrLoadGameRequest;
import net.alteiar.lendyr.server.grpc.v1.mapper.GenericMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GameProcessTest {

  private static LendyrGameServer server;

  @BeforeAll
  static void beforeAll() throws IOException {
    File dataFolder = new File("../../assembly/data");
    server = new LendyrGameServer(dataFolder);
    server.run(48080);
  }

  @AfterAll
  static void afterAll() throws InterruptedException {
    server.shutdown();
  }

  private LendyrGameServiceGrpc.LendyrGameServiceBlockingV2Stub blockingStub;

  @BeforeEach
  void beforeEach() {
    String target = "localhost:48080";
    ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
        .build();

    blockingStub = LendyrGameServiceGrpc.newBlockingV2Stub(channel);
  }

  @Test
  void waitForPlay() {
    // Given
    UUID mapId = UUID.fromString("f2659d86-a181-47a5-b201-1e6b2b48b94f");
    // When
    blockingStub.load(LendyrLoadGameRequest.newBuilder().setSaveName("dummy-test-1").build());
    blockingStub.resume(EmptyResponse.newBuilder().build());

    // Then
    Awaitility.await().untilAsserted(() -> {
      LendyrGameState state = blockingStub.currentState(EmptyResponse.newBuilder().build());
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(mapId), state.getMap().getMapId());
      Assertions.assertEquals(LendyrGameMode.COMBAT, state.getPlayState());
      Assertions.assertEquals(3, state.getEncounter().getInitiativeOrderCount());
    });
  }
}
