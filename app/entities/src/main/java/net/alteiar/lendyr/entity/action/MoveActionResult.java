package net.alteiar.lendyr.entity.action;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class MoveActionResult implements ActionResult {
  @NonNull
  UUID sourceId;
  @NonNull
  List<Position> path;
}
