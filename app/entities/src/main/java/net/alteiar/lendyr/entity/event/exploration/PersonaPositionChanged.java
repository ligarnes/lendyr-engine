package net.alteiar.lendyr.entity.event.exploration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.model.persona.Position;

import java.util.UUID;

@Value
@Builder
public class PersonaPositionChanged {
  @NonNull
  UUID sourceId;
  @NonNull
  Position position;
  Position nextPosition;
}
