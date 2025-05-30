package net.alteiar.lendyr.engine;

import net.alteiar.lendyr.entity.action.ActionResult;

public interface GameContextListener {

  ///  The game changed.
  void gameChanged();

  /// A new action is published
  void newAction(ActionResult action);
}
