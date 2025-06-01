package net.alteiar.lendyr.persistence.dao;

import lombok.AllArgsConstructor;
import lombok.Value;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.map.tiled.TiledMap;

@Value
@AllArgsConstructor
public class LocalMapDao {
  GameMap map;
  TiledMap tiledMap;
}
