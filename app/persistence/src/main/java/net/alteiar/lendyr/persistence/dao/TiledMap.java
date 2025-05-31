package net.alteiar.lendyr.persistence.dao;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledMap {
  private int width;
  private int height;
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<TiledObjectGroup> objectgroup;
}
