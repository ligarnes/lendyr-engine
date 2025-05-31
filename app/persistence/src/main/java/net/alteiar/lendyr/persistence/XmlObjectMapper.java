package net.alteiar.lendyr.persistence;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Builder;

import java.io.File;
import java.io.IOException;

public class XmlObjectMapper {
  private final XmlMapper mapper;
  private final File dataRepository;

  @Builder
  XmlObjectMapper(File dataRepository) {
    this.dataRepository = dataRepository;
    this.mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public <E> E load(String file, Class<E> clazz) {
    try {
      return mapper.readValue(new File(dataRepository, file), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void write(String file, Object obj) {
    try {
      mapper.writeValue(new File(dataRepository, file), obj);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
