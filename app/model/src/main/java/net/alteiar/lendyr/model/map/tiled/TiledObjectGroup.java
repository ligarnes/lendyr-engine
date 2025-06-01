package net.alteiar.lendyr.model.map.tiled;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.map.tiled.object.TiledObject;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledObjectGroup {
  int id;
  String name;
  @JacksonXmlElementWrapper(useWrapping = false)
  List<TiledObject> object;
}
