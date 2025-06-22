package net.alteiar.lendyr.model.map.layered.element;

import com.badlogic.gdx.math.Rectangle;

public interface MapElement {

  String getName();

  /// Retrieve the bounding box of the element.
  Rectangle getBoundingBox();

  /// Check collision with the provided rectangle
  boolean checkCollision(Rectangle rect);
}
