package net.alteiar.lendyr.model.encounter;

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
public class WorldMap {
  String name;
  UUID mapId;
  List<UUID> entities;
}
