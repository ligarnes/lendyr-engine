package net.alteiar.lendyr.model.npc.behavior;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patrol implements Behavior {
  private String behaviorType;
  private List<Position> positions;
}
