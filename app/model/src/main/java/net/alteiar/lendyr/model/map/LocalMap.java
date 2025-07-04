package net.alteiar.lendyr.model.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.npc.Npc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalMap {
  String name;
  UUID mapId;
  List<Npc> entities;
  List<ItemContainer> itemContainers = new ArrayList<>();
}
