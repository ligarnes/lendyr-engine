package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocalMapEntity {
  private final GameEntity gameEntity;

  private LocalMap localMap;
  private GameMap gameMap;
  private List<PersonaEntity> personaEntities;

  @Builder
  public LocalMapEntity(GameEntity gameEntity) {
    this.gameEntity = gameEntity;
    this.personaEntities = new ArrayList<>();
  }

  public int getWidth() {
    return gameMap.getWorldWidth();
  }

  public int getHeight() {
    return gameMap.getWorldHeight();
  }

  public void load(LocalMap map, GameMap gameMap) {
    this.localMap = map;
    this.gameMap = gameMap;
    personaEntities.clear();
    personaEntities.addAll(map.getEntities().stream()
        .map(gameEntity::findById)
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
    PersonaEntity source = gameEntity.findById(persona).orElseThrow(() -> new IllegalArgumentException("The persona does not exists"));
    Rectangle newPositionBox = source.getBoundingBoxAt(newPosition);

    return checkCollision(newPositionBox);
  }

  /**
   * True if any object collides with the persona and the expected new position.
   *
   * @param newPositionBox the desired position bounding box
   * @return true if the position generates a collision
   */
  public boolean checkCollision(Rectangle newPositionBox) {
    return isOutOfMap(newPositionBox)
        || checkCollisionWithWalls(newPositionBox)
        || checkCollisionWithOtherEntities(newPositionBox);
  }

  private boolean isOutOfMap(Rectangle newPositionBox) {
    return newPositionBox.x + newPositionBox.width > gameMap.getWorldWidth()
        || newPositionBox.x < 0
        || newPositionBox.y + newPositionBox.height > gameMap.getWorldHeight()
        || newPositionBox.y < 0;
  }

  private boolean checkCollisionWithWalls(Rectangle newPositionBox) {
    return false;// gameMap.getWalls().stream().anyMatch(newPositionBox::overlaps);
  }

  private boolean checkCollisionWithOtherEntities(Rectangle newPositionBox) {
    return personaEntities.stream()
        .map(PersonaEntity::getDefenceBoundingBox)
        .anyMatch(newPositionBox::overlaps);
  }

  public LocalMap toModel() {
    return localMap;
  }
}
