package net.alteiar.lendyr.entity.event.exploration;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.model.persona.Persona;

@Value
@Builder
public class PersonaChangedEvent implements GameEvent {
  Persona persona;
}
