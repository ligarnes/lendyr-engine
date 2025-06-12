package net.alteiar.lendyr.entity.event.exploration;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.entity.event.GameEvent;

import java.util.List;

@Value
@Builder
public class RealtimeUpdateEvent implements GameEvent {
  private List<PersonaPositionChanged> positions;
}
