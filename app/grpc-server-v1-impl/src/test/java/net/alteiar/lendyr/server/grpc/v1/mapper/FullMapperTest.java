package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrEncounter;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.PlayState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FullMapperTest {

  @Test
  public void loadModel() throws IOException, URISyntaxException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/dummy-test-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    Assertions.assertEquals(UUID.fromString("af30c918-1a29-4b6d-a2c1-f5c8949ba08b"), game.getPlayer().getId());
    Assertions.assertEquals("Lendyr", game.getPlayer().getName());
    Assertions.assertEquals(1, game.getPlayer().getControlledPersonaIds().size());
    Assertions.assertEquals(UUID.fromString("b66c4a7e-9f5f-40c7-82d4-f442e5c7fe80"), game.getPlayer().getControlledPersonaIds().getFirst());
    Assertions.assertEquals(PlayState.COMBAT, game.getPlayState());
    Assertions.assertEquals(UUID.fromString("f2659d86-a181-47a5-b201-1e6b2b48b94f"), game.getLocalMap().getMapId());
    Assertions.assertEquals("Les ruines", game.getLocalMap().getName());
    Assertions.assertEquals(0, game.getLocalMap().getItemContainers().size());
    Assertions.assertEquals(3, game.getLocalMap().getEntities().size());
    Assertions.assertEquals(UUID.fromString("b66c4a7e-9f5f-40c7-82d4-f442e5c7fe79"), game.getLocalMap().getEntities().get(0));
    Assertions.assertEquals(UUID.fromString("b66c4a7e-9f5f-40c7-82d4-f442e5c7fe80"), game.getLocalMap().getEntities().get(1));
    Assertions.assertEquals(UUID.fromString("b66c4a7e-9f5f-40c7-82d4-f442e5c7fe81"), game.getLocalMap().getEntities().get(2));
  }

  @Test
  public void test() throws IOException, URISyntaxException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/dummy-test-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    LendyrGameState gameDto = GameStateMapper.INSTANCE.businessToDto(game);

    Assertions.assertEquals(EncounterMapper.INSTANCE.businessToDto(game.getEncounter()), gameDto.getEncounter());
    Assertions.assertEquals(WorldMapMapper.INSTANCE.worldMapToDto(game.getLocalMap()), gameDto.getMap());
    // Assertions.assertEquals(game.getPersonas().size(), gameDto.getPersonaCount());
  }


  @Test
  public void loadExploration() throws IOException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/exploration-complex-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    // Assertions.assertEquals(3, game.getPersonas().size());
  }

  @Test
  public void explorationMapper() throws IOException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/exploration-complex-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    LendyrGameState gameDto = GameStateMapper.INSTANCE.businessToDto(game);

    Assertions.assertEquals(LendyrEncounter.getDefaultInstance(), gameDto.getEncounter());
    Assertions.assertEquals(WorldMapMapper.INSTANCE.worldMapToDto(game.getLocalMap()), gameDto.getMap());
    // Assertions.assertEquals(game.getPersonas().size(), gameDto.getPersonaCount());
  }
}
