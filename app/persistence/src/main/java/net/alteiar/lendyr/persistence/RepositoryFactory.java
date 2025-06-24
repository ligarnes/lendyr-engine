package net.alteiar.lendyr.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RepositoryFactory {
  private static RepositoryFactory INSTANCE;

  public static RepositoryFactory initialize(File dataFolder) {
    JsonMapper jsonMapper = JsonMapper.builder().dataRepository(dataFolder).build();
    XmlObjectMapper xmlMapper = XmlObjectMapper.builder().dataRepository(dataFolder).build();
    MapRepository mapRepository = new MapRepository(jsonMapper, xmlMapper);
    SaveRepository saveRepository = new SaveRepository(jsonMapper);
    ItemRepository itemRepository = new ItemRepository(jsonMapper);
    PersonaRepository personaRepository = new PersonaRepository(jsonMapper);
    INSTANCE = new RepositoryFactory(itemRepository, mapRepository, saveRepository, personaRepository);
    return INSTANCE;
  }

  public static RepositoryFactory get() {
    return INSTANCE;
  }

  private final ItemRepository itemRepository;
  private final MapRepository mapRepository;
  private final SaveRepository saveRepository;
  private final PersonaRepository personaRepository;
}
