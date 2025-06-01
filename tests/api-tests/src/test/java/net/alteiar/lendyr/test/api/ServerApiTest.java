package net.alteiar.lendyr.test.api;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.alteiar.lendyr.app.LendyrGameServer;
import net.alteiar.lendyr.grpc.model.v1.game.*;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.server.grpc.v1.mapper.GenericMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ServerApiTest {
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

  @BeforeEach
  void beforeEach() {
    String target = "localhost:48080";
    ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
        .build();

    blockingStub = LendyrGameServiceGrpc.newBlockingV2Stub(channel);
  }

  @Test
  void getMap() {
    // Given
    UUID mapId = UUID.fromString("f2659d86-a181-47a5-b201-1e6b2b48b94f");
    LendyrGetById getById = LendyrGetById.newBuilder().setId(GenericMapper.INSTANCE.convertUUIDToBytes(mapId)).build();

    // When
    LendyrMap map = blockingStub.getMaps(getById);

    // Then
    Assertions.assertEquals("tiled/sample.tmx", map.getPath());
    Assertions.assertEquals(30, map.getWorldWidth());
    Assertions.assertEquals(20, map.getWorldHeight());
    Assertions.assertEquals(0.03125f, map.getScale());
  }

  @Test
  void currentState() {
    // Given
    UUID mapId = UUID.fromString("f2659d86-a181-47a5-b201-1e6b2b48b94f");
    // When
    blockingStub.load(LendyrLoadGameRequest.newBuilder().setSaveName("dummy-test-1").build());
    LendyrGameState state = blockingStub.currentState(EmptyResponse.newBuilder().build());

    // Then
    Assertions.assertEquals(3, state.getPersonaCount());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(mapId), state.getMap().getMapId());
    Assertions.assertEquals(LendyrPlayState.PAUSE, state.getPlayState());
    Assertions.assertEquals(3, state.getEncounter().getInitiativeOrderCount());
    Assertions.assertEquals("Ulfrik", state.getPersona(0).getName());
    Assertions.assertEquals(12, state.getPersona(0).getPosition().getX());
    Assertions.assertEquals(12, state.getPersona(0).getPosition().getY());
    Assertions.assertEquals(3, state.getPersona(0).getPosition().getLayer());
  }
}
