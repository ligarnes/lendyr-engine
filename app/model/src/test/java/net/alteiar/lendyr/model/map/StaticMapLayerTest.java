package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StaticMapLayerTest {

  @Test
  void isInLayer_polygon() {
    float[] vertices = new float[]{
        0.0f, 20.0f,
        4.0f, 20.0f,
        4.0f, 9.0f,
        7.0f, 9.0f,
        7.0f, 8.0f,
        9.0f, 8.0f,
        9.0f, 9.0f, 24.0f, 9.0f, 24.0f, 15.0f, 28.0f, 15.0f, 28.0f, 20.0f, 30.0f, 20.0f, 30.0f, 0.0f, 0.0f, 0.0f};
    StaticMapLayer staticMapLayer = new StaticMapLayer(20, 30, new Polygon(vertices), List.of(), List.of());

    Assertions.assertTrue(staticMapLayer.isInLayer(new Rectangle(0, 0, 1, 1)));
    Assertions.assertTrue(staticMapLayer.isInLayer(new Rectangle(0, 19, 1, 1)));
    Assertions.assertTrue(staticMapLayer.isInLayer(new Rectangle(29, 0, 1, 1)));
    Assertions.assertTrue(staticMapLayer.isInLayer(new Rectangle(29, 19, 1, 1)));
  }
}