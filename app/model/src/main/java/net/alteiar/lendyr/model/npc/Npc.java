package net.alteiar.lendyr.model.npc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.npc.behavior.Behavior;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Npc {
  private UUID personaId;
  private NpcRelationType relationType;
  private Behavior behavior;
}
