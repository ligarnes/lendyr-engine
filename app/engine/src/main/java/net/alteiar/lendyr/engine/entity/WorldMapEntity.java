package net.alteiar.lendyr.engine.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldMapEntity {
  private final GameContext gameContext;

  private WorldMap worldMap;
  private GameMap gameMap;
  private List<PersonaEntity> personaEntities;

  @Builder
  public WorldMapEntity(GameContext gameContext) {
    this.gameContext = gameContext;
    this.personaEntities = new ArrayList<>();
  }

  public void load(WorldMap map) {
    this.worldMap = map;
    this.gameMap = gameContext.getMapRepository().findMapById(map.getMapId());
    personaEntities.clear();
    personaEntities.addAll(map.getEntities().stream()
        .map(gameContext.getGame()::findById)
        .map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("The persona could not be found")))
        .toList());
  }

  /**
   * True if any object collides with the persona and the expected new position.
   *
   * @param persona     the persona
   * @param newPosition the new position of the persona
   * @return true if the position generates a collision
   */
  public boolean checkCollision(UUID persona, Vector2 newPosition) {
    PersonaEntity source = gameContext.getGame().findById(persona).orElseThrow(() -> new IllegalArgumentException("The persona does not exists"));
    Rectangle newPositionBox = source.getBoundingBoxAt(newPosition);

    return checkCollisionWithWalls(newPositionBox)
        || checkCollisionWithOtherEntities(persona, newPositionBox);
  }

  private boolean checkCollisionWithWalls(Rectangle newPositionBox) {
    return false;// gameMap.getWalls().stream().anyMatch(newPositionBox::overlaps);
  }

  private boolean checkCollisionWithOtherEntities(UUID personaId, Rectangle newPositionBox) {
    return personaEntities.stream().filter(p -> !personaId.equals(p.getId()))
        .map(PersonaEntity::getDefenceBoundingBox)
        .anyMatch(newPositionBox::overlaps);
  }

  public WorldMap toModel() {
    return worldMap;
  }
}
