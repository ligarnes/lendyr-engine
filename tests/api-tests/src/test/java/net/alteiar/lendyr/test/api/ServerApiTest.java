package net.alteiar.lendyr.test.api;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.alteiar.lendyr.app.LendyrGameServer;
import net.alteiar.lendyr.grpc.model.v1.game.*;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrMap;
import net.alteiar.lendyr.server.grpc.v1.mapper.GenericMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ServerApiTest {

  @BeforeAll
  static void beforeAll() throws IOException, InterruptedException {
    File dataFolder = new File("../../assembly/data");
    LendyrGameServer server = new LendyrGameServer(dataFolder);
    server.load("dummy-test-1");
    server.run(48080);
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
    LendyrGameState state = blockingStub.currentState(EmptyResponse.newBuilder().build());

    // Then
    Assertions.assertEquals(3, state.getPersonaCount());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(mapId), state.getMap().getMapId());
    Assertions.assertEquals(LendyrPlayState.PAUSE, state.getPlayState());
    Assertions.assertEquals(3, state.getEncounter().getInitiativeOrderCount());
  }
}
