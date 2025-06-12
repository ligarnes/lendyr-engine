package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrEncounter;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FullMapperTest {

  @Test
  public void loadModel() throws IOException, URISyntaxException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/dummy-test-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    Assertions.assertEquals(3, game.getPersonas().size());
  }

  @Test
  public void test() throws IOException, URISyntaxException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/dummy-test-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    LendyrGameState gameDto = GameStateMapper.INSTANCE.businessToDto(game);

    Assertions.assertEquals(EncounterMapper.INSTANCE.businessToDto(game.getEncounter()), gameDto.getEncounter());
    Assertions.assertEquals(WorldMapMapper.INSTANCE.worldMapToDto(game.getLocalMap()), gameDto.getMap());
    Assertions.assertEquals(game.getPersonas().size(), gameDto.getPersonaCount());
  }


  @Test
  public void loadExploration() throws IOException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/exploration-complex-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    Assertions.assertEquals(3, game.getPersonas().size());
  }

  @Test
  public void explorationMapper() throws IOException {
    byte[] data = Files.readAllBytes(Paths.get("../../assembly/data/saves/exploration-complex-1.json"));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    LendyrGameState gameDto = GameStateMapper.INSTANCE.businessToDto(game);

    Assertions.assertEquals(LendyrEncounter.getDefaultInstance(), gameDto.getEncounter());
    Assertions.assertEquals(WorldMapMapper.INSTANCE.worldMapToDto(game.getLocalMap()), gameDto.getMap());
    Assertions.assertEquals(game.getPersonas().size(), gameDto.getPersonaCount());
  }
}
