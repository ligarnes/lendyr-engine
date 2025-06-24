package net.alteiar.lendyr.model.npc.behavior;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.persona.Position;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Static implements Behavior {
  private String behaviorType;
  private Position position;
}
