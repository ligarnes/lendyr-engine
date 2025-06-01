package net.alteiar.lendyr.entity.map;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.entity.map.element.MapElement;

@Value
@Builder
public class Bridge {
  int lower;
  int upper;
  MapElement region;
}
