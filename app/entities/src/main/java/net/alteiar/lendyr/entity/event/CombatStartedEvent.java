package net.alteiar.lendyr.entity.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.alteiar.lendyr.entity.EncounterEntity;

@Value
@AllArgsConstructor
public class CombatStartedEvent implements GameEvent {
  EncounterEntity encounter;
}
