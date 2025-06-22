package net.alteiar.lendyr.model.map.layered;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.model.map.layered.element.MapElement;

@Value
@Builder
@AllArgsConstructor
public class Bridge {
  int lower;
  int upper;
  MapElement region;
}
