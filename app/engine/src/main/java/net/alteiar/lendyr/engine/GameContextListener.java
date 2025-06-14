package net.alteiar.lendyr.engine;

import net.alteiar.lendyr.entity.event.GameEvent;

public interface GameContextListener {

  /// A new action is published
  void newAction(GameEvent action);
}
