package net.alteiar.lendyr.model.map.element;

import com.badlogic.gdx.math.Rectangle;

public interface MapElement {

  String getName();

  boolean checkCollision(Rectangle rect);
}
