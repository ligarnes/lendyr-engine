package net.alteiar.lendyr.model.map;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.model.map.element.MapElement;

@Value
@Builder
public class Bridge {
  int lower;
  int upper;
  MapElement region;
}
