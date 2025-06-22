package net.alteiar.lendyr.model.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalMap {
  String name;
  UUID mapId;
  List<UUID> entities;
  List<ItemContainer> itemContainers;
}
