package net.alteiar.lendyr.entity.event.exploration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.model.map.ItemContainer;

@Value
@Builder
@AllArgsConstructor
public class ItemContainerChangedEvent implements GameEvent {
  ItemContainer itemContainer;
}
