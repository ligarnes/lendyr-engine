package net.alteiar.lendyr.entity.action;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class MoveActionResult implements ActionResult {
  @NonNull
  UUID sourceId;
  @NonNull
  List<Vector2> path;
}
