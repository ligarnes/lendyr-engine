package net.alteiar.lendyr.entity.map;

import com.badlogic.gdx.math.Rectangle;
import lombok.Value;
import net.alteiar.lendyr.entity.map.element.MapElement;

import java.util.ArrayList;
import java.util.List;

@Value
public class StaticMapLayer {
  float width;
  float height;
  List<MapElement> mapElements;

  StaticMapLayer(float width, float height) {
    this.width = width;
    this.height = height;
    mapElements = new ArrayList<>();
  }

  public boolean checkCollision(Rectangle rect) {
    return mapElements.stream().anyMatch(mapElement -> mapElement.checkCollision(rect));
  }
}
