package net.alteiar.lendyr.entity.event;

public interface GameEvent {
  ///  Define if the world has changed.
  ///
  /// @return true, if anything in the world state changed.
  default boolean hasWorldChanged() {
    return true;
  }
}
