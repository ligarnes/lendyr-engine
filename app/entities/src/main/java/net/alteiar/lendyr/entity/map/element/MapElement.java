package net.alteiar.lendyr.entity.map.element;

import com.badlogic.gdx.math.Rectangle;

public interface MapElement {

  String getName();

  boolean checkCollision(Rectangle rect);
}
