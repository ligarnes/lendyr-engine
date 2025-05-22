package net.alteiar.lendyr.persistence;

import net.alteiar.lendyr.model.Game;

public class SaveRepository {
  private final JsonMapper jsonMapper;

  public SaveRepository(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public Game load(String saveName) {
    return jsonMapper.load("./saves/%s.json".formatted(saveName), Game.class);
  }

  public void save(String saveName, Game game) {
    jsonMapper.write("./saves/%s.json".formatted(saveName), game);
  }
}
