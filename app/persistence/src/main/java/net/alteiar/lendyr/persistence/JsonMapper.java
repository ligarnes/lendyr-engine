package net.alteiar.lendyr.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.io.File;
import java.io.IOException;

public class JsonMapper {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final File dataRepository;

  @Builder
  JsonMapper(File dataRepository) {
    this.dataRepository = dataRepository;
  }

  public <E> E load(String file, Class<E> clazz) {
    try {
      return objectMapper.readValue(new File(dataRepository, file), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void write(String file, Object obj) {
    try {
      objectMapper.writeValue(new File(dataRepository, file), obj);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
