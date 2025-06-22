package net.alteiar.lendyr.ai.combat.geometry;

import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.representation.DynamicPathfinding;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.map.layered.LayeredMapWithMovable;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;

@Log4j2
public class PersonaWorldRepresentation {
  private final DynamicPathfinding pathfinding;

  public PersonaWorldRepresentation(LayeredMapWithMovable map) {
    this.pathfinding = new DynamicPathfinding(map);
  }

  public void update() {
    pathfinding.update();
  }

  public List<Position> fleeFrom(PersonaEntity entity, Position target) {
    return pathfinding.fleeFrom(entity.getPosition(), target, entity.getMoveDistance());
  }

  public List<Position> pathTo(PersonaEntity entity, Position end) {
    return pathfinding.pathTo(entity.getPosition(), end, entity.getSize(), entity.getMoveDistance());
  }
}
