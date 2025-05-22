package net.alteiar.lendyr.persistence;

import lombok.NonNull;
import net.alteiar.lendyr.model.encounter.EncounterMap;

import java.util.UUID;

public class MapRepository {
  private final JsonMapper jsonMapper;

  public MapRepository(@NonNull JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public EncounterMap findMapById(@NonNull UUID id) {
    return jsonMapper.load("./maps/map-%s.json".formatted(id), EncounterMap.class);
  }
}
