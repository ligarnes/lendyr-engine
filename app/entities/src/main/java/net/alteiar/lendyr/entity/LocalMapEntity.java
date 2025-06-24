package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.npc.NpcEntity;
import net.alteiar.lendyr.model.map.ItemContainer;
import net.alteiar.lendyr.model.map.LocalMap;
import net.alteiar.lendyr.model.map.layered.*;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;

import java.util.*;
import java.util.stream.Stream;

@Log4j2
public class LocalMapEntity implements LayeredMapWithMovable {
  private final GameEntity gameEntity;

  private LocalMap localMap;

  @Getter
  private List<PersonaEntity> pcEntities;
  @Getter
  private final List<NpcEntity> npcEntities;
  @Getter
  private LayeredMap layeredMap;
  @Getter
  private List<ItemContainer> itemContainers;

  @Builder
  public LocalMapEntity(GameEntity gameEntity) {
    this.gameEntity = gameEntity;
    this.pcEntities = new ArrayList<>();
    this.npcEntities = new ArrayList<>();
    this.itemContainers = new ArrayList<>();
  }

  public Optional<ItemContainer> getItemContainerById(UUID id) {
    return itemContainers.stream().filter(c -> Objects.equals(id, c.getId())).findFirst();
  }

  public void load(LocalMap map, LocalMapDao gameMap) {
    this.localMap = map;
    this.layeredMap = new MapFactory(gameMap.getTiledMap()).load();
    pcEntities.clear();
    pcEntities = gameEntity.getPlayer().getControlledPersonas();
    npcEntities.clear();
    npcEntities.addAll(map.getEntities().stream().map(n -> new NpcEntity(gameEntity, n)).toList());
    if (map.getItemContainers() != null) {
      itemContainers.addAll(map.getItemContainers());
    }
  }

  @Override
  public Stream<DynamicBlockingObject> getMovableObjects() {
    return Stream.concat(
        pcEntities.stream().filter(PersonaEntity::isAlive).map(PersonaEntity::getDefenceBoundingBox),
        Stream.concat(
            npcEntities.stream().filter(NpcEntity::isAlive).map(NpcEntity::getPersona).map(PersonaEntity::getDefenceBoundingBox),
            itemContainers.stream().filter(ItemContainer::isBlocking).map(ItemContainer::getBoundingBox)
        ));
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

    Optional<Bridge> bridge = this.getLayeredMap().getBridge(newPositionBox.getRectangle());
    if (bridge.isPresent()) {
      // Check only with other objects
      return checkCollisionWithOtherEntities(newPositionBox);
    }
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
    return newPositionBox.getRectangle().x + newPositionBox.getRectangle().width > layeredMap.getWidth()
        || newPositionBox.getRectangle().x < newPositionBox.getRectangle().width
        || newPositionBox.getRectangle().y + newPositionBox.getRectangle().height > layeredMap.getHeight()
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
    for (float y = layeredMap.getHeight(); y >= 0; y--) {
      StringBuilder builder = new StringBuilder();
      for (int x = 0; x < layeredMap.getHeight(); x++) {
        builder.append(checkCollision(new DynamicBlockingObject(new Rectangle(x, y, 1, 1), 1)) ? "x" : "_").append(" ");
      }
      log.info(builder.toString());
    }

    log.info("layer {}: ", 2);
    for (float y = layeredMap.getHeight(); y >= 0; y--) {
      StringBuilder builder = new StringBuilder();
      for (float x = 0; x < layeredMap.getWidth(); x++) {
        builder.append(checkCollision(new DynamicBlockingObject(new Rectangle(x, y, 1, 2), 2)) ? "x" : "_").append(" ");
      }
      log.info(builder.toString());
    }
  }
}
