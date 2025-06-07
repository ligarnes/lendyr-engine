package net.alteiar.lendyr.entity.event;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GameSaved implements GameEvent {
  String name;
}
