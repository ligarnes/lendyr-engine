package net.alteiar.lendyr.model.map.layered;

import java.util.stream.Stream;

public interface LayeredMapWithMovable {

  /**
   * The layered map (static only).
   *
   * @return the map
   */
  LayeredMap getLayeredMap();

  /**
   * The movable objects (that might change position)
   *
   * @return the objects
   */
  Stream<DynamicBlockingObject> getMovableObjects();
}
