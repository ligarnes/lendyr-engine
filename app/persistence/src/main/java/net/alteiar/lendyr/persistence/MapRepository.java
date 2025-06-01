package net.alteiar.lendyr.persistence;

import lombok.NonNull;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapRepository {
  private final JsonMapper jsonMapper;
  private final XmlObjectMapper mapper;

  private final Map<UUID, LocalMapDao> cache;

  public MapRepository(@NonNull JsonMapper jsonMapper, @NonNull XmlObjectMapper mapper) {
    this.jsonMapper = jsonMapper;
    this.mapper = mapper;
    this.cache = new ConcurrentHashMap<>();
  }

  private GameMap loadGameMap(@NonNull UUID id) {
    return jsonMapper.load("./maps/map-%s.json".formatted(id), GameMap.class);
  }

  public LocalMapDao findMapById(@NonNull UUID id) {
    return cache.computeIfAbsent(id, (mapId) -> {
      GameMap map = loadGameMap(mapId);
      TiledMap tiledMap = mapper.load(map.getPath(), TiledMap.class);
      return new LocalMapDao(map, tiledMap);
    });
  }
}
