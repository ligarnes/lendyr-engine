package net.alteiar.lendyr.entity.event.exploration;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.event.GameEvent;

@Value
@Builder
public class PersonaChangedEvent implements GameEvent {
  PersonaEntity persona;
}
