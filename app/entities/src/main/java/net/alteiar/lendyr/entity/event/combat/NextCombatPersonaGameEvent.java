package net.alteiar.lendyr.entity.event.combat;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.alteiar.lendyr.entity.event.GameEvent;

import java.util.UUID;

@Value
@AllArgsConstructor
public class NextCombatPersonaGameEvent implements GameEvent {
  int currentTurn;
  UUID newPlayerId;
}
