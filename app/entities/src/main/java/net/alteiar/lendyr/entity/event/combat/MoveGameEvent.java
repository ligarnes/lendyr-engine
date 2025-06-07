package net.alteiar.lendyr.entity.event.combat;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class MoveGameEvent implements GameEvent {
  @NonNull
  UUID sourceId;
  @NonNull
  List<Position> path;

  boolean minorActionUsed;
  boolean majorActionUsed;
}
