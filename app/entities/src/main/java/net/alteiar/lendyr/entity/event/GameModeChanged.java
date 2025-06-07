package net.alteiar.lendyr.entity.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.alteiar.lendyr.model.PlayState;

@Value
@AllArgsConstructor
public class GameModeChanged implements GameEvent {
  PlayState newMode;
}
