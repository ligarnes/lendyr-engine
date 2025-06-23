package net.alteiar.lendyr.persistence;

import lombok.Getter;

import java.io.File;

@Getter
public final class RepositoryFactory {
  private final ItemRepository itemRepository;
  private final MapRepository mapRepository;
  private final SaveRepository saveRepository;
  private final PersonaRepository personaRepository;

  public RepositoryFactory(File dataFolder) {
    JsonMapper jsonMapper = JsonMapper.builder().dataRepository(dataFolder).build();
    XmlObjectMapper xmlMapper = XmlObjectMapper.builder().dataRepository(dataFolder).build();
    mapRepository = new MapRepository(jsonMapper, xmlMapper);
    saveRepository = new SaveRepository(jsonMapper);
    itemRepository = new ItemRepository(jsonMapper);
    personaRepository = new PersonaRepository(jsonMapper);
  }
}
