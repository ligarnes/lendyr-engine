package net.alteiar.lendyr.model.map.layered;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DynamicBlockingObject {
  Rectangle rectangle;
  Integer layer;

  public boolean overlap(DynamicBlockingObject other) {
    return rectangle.overlaps(other.getRectangle()) && layer.equals(other.getLayer());
  }
}
