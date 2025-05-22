package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    byte[] data = Files.readAllBytes(Paths.get(FullMapperTest.class.getResource("/dummy-test-1.json").toURI()));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    Assertions.assertEquals(3, game.getPersonas().size());
  }

  @Test
  public void test() throws IOException, URISyntaxException {
    byte[] data = Files.readAllBytes(Paths.get(FullMapperTest.class.getResource("/dummy-test-1.json").toURI()));

    ObjectMapper mapper = new ObjectMapper();
    Game game = mapper.readValue(data, Game.class);

    LendyrGameState gameDto = GameStateMapper.INSTANCE.businessToDto(game);

    Assertions.assertEquals(game.getEncounter().getName(), gameDto.getEncounter().getName());
    Assertions.assertEquals(game.getPersonas().size(), gameDto.getPersonaCount());
  }
}
