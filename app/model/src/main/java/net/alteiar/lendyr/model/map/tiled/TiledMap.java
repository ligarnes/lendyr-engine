package net.alteiar.lendyr.model.map.tiled;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledMap {

  public static TiledMap load(File file) {
    try {
      XmlMapper mapper = new XmlMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return mapper.readValue(file, TiledMap.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private int width;
  private int height;
  private int tilewidth;
  private int tileheight;
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<TiledObjectGroup> objectgroup;

  public float getScaleX() {
    return 1 / (float) tilewidth;
  }

  public float getScaleY() {
    return 1 / (float) tileheight;
  }

  public Optional<TiledObjectGroup> getByName(String name) {
    return objectgroup.stream().filter(g -> Objects.equals(g.getName(), name)).findFirst();
  }
}
