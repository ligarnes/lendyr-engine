package net.alteiar.lendyr.test.api;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusException;
import io.grpc.stub.BlockingClientCall;
import net.alteiar.lendyr.app.LendyrGameServer;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameEvent;
import net.alteiar.lendyr.grpc.model.v1.game.EmptyResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrLoadGameRequest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class MultiTurnActionProcessTest {

  private static LendyrGameServer server;

  @BeforeAll
  static void beforeAll() throws IOException, InterruptedException {
    File dataFolder = new File("../../assembly/data");
    server = new LendyrGameServer(dataFolder);
    server.run(48080);
  }

  @AfterAll
  static void afterAll() throws InterruptedException {
    server.shutdown();
  }

  private LendyrGameServiceGrpc.LendyrGameServiceBlockingV2Stub blockingStub;
  private boolean terminate;
  private AtomicReference<LendyrGameEvent> reference;

  @BeforeEach
  void beforeEach() {
    String target = "localhost:48080";
    ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
        .build();

    blockingStub = LendyrGameServiceGrpc.newBlockingV2Stub(channel);
    reference = new AtomicReference<>();
  }

  @Test
  void waitForPlay() {
    // Given
    Thread tr = Thread.ofPlatform().start(this::run);

    // When
    blockingStub.load(LendyrLoadGameRequest.newBuilder().setSaveName("dummy-test-1").build());
    blockingStub.resume(EmptyResponse.newBuilder().build());

    playTurn();
    playTurn();
    playTurn();

    // Then
    Awaitility.await().untilAsserted(() -> {
      LendyrGameEvent result = reference.get();
      Assertions.assertNotNull(result);
    });
    terminate = true;
    tr.interrupt();
  }

  private void playTurn() {
    Awaitility.await().untilAsserted(() -> {
      LendyrGameState currentState = blockingStub.currentState(EmptyResponse.newBuilder().build());
      Assertions.assertEquals(1, currentState.getEncounter().getActive().getActivePersonaIdx());
    });
    blockingStub.endTurn(EmptyResponse.newBuilder().build());
  }

  private void run() {
    BlockingClientCall<?, LendyrGameEvent> clientCall = blockingStub.registerActions(EmptyResponse.newBuilder().build());
    while (!terminate) {
      try {
        LendyrGameEvent action = clientCall.read();
        reference.set(action);
      } catch (InterruptedException | StatusException e) {
        e.printStackTrace();
      }
    }
    clientCall.cancel("Client terminate", null);
  }
}
