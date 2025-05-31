package net.alteiar.lendyr.entity.map;

import com.badlogic.gdx.math.Rectangle;

import java.util.stream.Stream;

public interface WorldMap {

  LayeredMap getLayeredMap();

  Stream<Rectangle> getMovableObjects();
}
