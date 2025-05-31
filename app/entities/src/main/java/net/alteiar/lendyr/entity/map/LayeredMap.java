package net.alteiar.lendyr.entity.map;

import com.badlogic.gdx.math.Rectangle;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Value
@Log4j2
public class LayeredMap {
  float width;
  float height;
  Map<Integer, StaticMapLayer> layers;

  LayeredMap(float width, float height) {
    this.width = width;
    this.height = height;
    layers = new HashMap<>();
  }

  public boolean checkCollision(int layer, Rectangle rect) {
    return layers.get(layer).checkCollision(rect);
  }
}
