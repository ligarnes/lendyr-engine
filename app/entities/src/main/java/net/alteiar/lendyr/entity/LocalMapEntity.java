package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.encounter.LocalMap;
import net.alteiar.lendyr.model.map.DynamicBlockingObject;
import net.alteiar.lendyr.model.map.LayeredMap;
import net.alteiar.lendyr.model.map.LayeredMapWithMovable;
import net.alteiar.lendyr.model.map.MapFactory;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Log4j2
public class LocalMapEntity implements LayeredMapWithMovable {
  private final GameEntity gameEntity;

  private LocalMap localMap;
  private GameMap gameMap;
  private final List<PersonaEntity> personaEntities;
  @Getter
  private LayeredMap layeredMap;

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

  public void load(LocalMap map, LocalMapDao gameMap) {
    this.localMap = map;
    this.gameMap = gameMap.getMap();
    this.layeredMap = new MapFactory(gameMap.getTiledMap()).load();
    personaEntities.clear();
    personaEntities.addAll(map.getEntities().stream()
        .map(gameEntity::findById)
        .map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("The persona could not be found")))
        .toList());
  }

  @Override
  public Stream<DynamicBlockingObject> getMovableObjects() {
    return personaEntities.stream().map(PersonaEntity::getDefenceBoundingBox);
  }

  /**
   * True if any object collides with the persona and the expected new position.
   *
   * @param persona     the persona
   * @param newPosition the new position of the persona
   * @return true if the position generates a collision
   */
  public boolean checkCollision(UUID persona, Position newPosition) {
    PersonaEntity source = gameEntity.findById(persona).orElseThrow(() -> new IllegalArgumentException("The persona does not exists"));
    DynamicBlockingObject newPositionBox = source.getBoundingBoxAt(newPosition);

    return checkCollision(newPositionBox);
  }

  /**
   * True if any object collides with the persona and the expected new position.
   *
   * @param newPositionBox the desired position bounding box
   * @return true if the position generates a collision
   */
  public boolean checkCollision(DynamicBlockingObject newPositionBox) {
    return isOutOfMap(newPositionBox)
        || checkCollisionWithOtherEntities(newPositionBox)
        || layeredMap.checkCollision(newPositionBox.getLayer(), newPositionBox.getRectangle());
  }

  private boolean isOutOfMap(DynamicBlockingObject newPositionBox) {
    return newPositionBox.getRectangle().x + newPositionBox.getRectangle().width > gameMap.getWorldWidth()
        || newPositionBox.getRectangle().x < newPositionBox.getRectangle().width
        || newPositionBox.getRectangle().y + newPositionBox.getRectangle().height > gameMap.getWorldHeight()
        || newPositionBox.getRectangle().y < newPositionBox.getRectangle().height;
  }

  private boolean checkCollisionWithOtherEntities(DynamicBlockingObject newPositionBox) {
    return getMovableObjects()
        .filter(d -> Objects.equals(newPositionBox.getLayer(), d.getLayer()))
        .map(DynamicBlockingObject::getRectangle)
        .anyMatch(r -> newPositionBox.getRectangle().overlaps(r));
  }

  public LocalMap toModel() {
    return localMap;
  }

  public void debug() {
    log.info("layer {}: ", 1);
    for (int y = gameMap.getWorldHeight(); y >= 0; y--) {
      StringBuilder builder = new StringBuilder();
      for (int x = 0; x < gameMap.getWorldWidth(); x++) {
        builder.append(checkCollision(new DynamicBlockingObject(new Rectangle(x, y, 1, 1), 1)) ? "x" : "_").append(" ");
      }
      log.info(builder.toString());
    }

    log.info("layer {}: ", 2);
    for (int y = gameMap.getWorldHeight(); y >= 0; y--) {
      StringBuilder builder = new StringBuilder();
      for (int x = 0; x < gameMap.getWorldWidth(); x++) {
        builder.append(checkCollision(new DynamicBlockingObject(new Rectangle(x, y, 1, 2), 2)) ? "x" : "_").append(" ");
      }
      log.info(builder.toString());
    }
  }
}
