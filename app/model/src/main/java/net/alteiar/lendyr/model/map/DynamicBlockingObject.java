package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DynamicBlockingObject {
  Rectangle rectangle;
  Integer layer;
}
