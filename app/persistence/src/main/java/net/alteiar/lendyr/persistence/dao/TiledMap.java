package net.alteiar.lendyr.persistence.dao;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledMap {
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
