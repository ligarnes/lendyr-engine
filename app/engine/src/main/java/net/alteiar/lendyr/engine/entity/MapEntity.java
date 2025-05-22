package net.alteiar.lendyr.engine.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.model.encounter.Encounter;
import net.alteiar.lendyr.model.encounter.EncounterMap;

import java.util.List;
import java.util.UUID;

public class MapEntity {
  private final GameContext gameContext;

  private EncounterMap map;
  private List<PersonaEntity> personaEntities;

  @Builder
  public MapEntity(GameContext gameContext) {
    this.gameContext = gameContext;
  }

  public void load(Encounter encounter) {
    this.map = gameContext.getMapRepository().findMapById(encounter.getMapId());
    personaEntities.clear();
    personaEntities.addAll(encounter.getCurrentState().getInitiative().stream()
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
    return map.getWalls().stream().anyMatch(newPositionBox::overlaps);
  }

  private boolean checkCollisionWithOtherEntities(UUID personaId, Rectangle newPositionBox) {
    return personaEntities.stream().filter(p -> !personaId.equals(p.getId()))
        .map(PersonaEntity::getDefenceBoundingBox)
        .anyMatch(newPositionBox::overlaps);
  }

}
