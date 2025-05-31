package net.alteiar.lendyr.ai.combat;

import com.badlogic.gdx.math.Vector2;
import lombok.NonNull;
import net.alteiar.lendyr.ai.combat.geometry.GeometryUtils;
import net.alteiar.lendyr.ai.combat.geometry.PersonaWorldRepresentation;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.MajorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MinorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;

import java.util.List;

public class RangeCombatStrategy implements CombatAiActor {

  private final GameEntity game;
  private final PersonaWorldRepresentation worldRepresentation;

  RangeCombatStrategy(@NonNull GameEntity game, @NonNull PersonaWorldRepresentation worldRepresentation) {
    this.game = game;
    this.worldRepresentation = worldRepresentation;
  }

  @Override
  public TurnAction combatTurn(PersonaEntity persona) {
    List<Enemy> enemiesEntity = EnemyUtils.listEnemies(game, persona);

    Enemy enemy = selectTarget(enemiesEntity);

    TurnAction.ActionOrder actionOrder = TurnAction.ActionOrder.MAJOR_FIRST;
    MajorAction majorAction = AttackAction.builder().sourceId(persona.getId()).targetId(enemy.personaTarget().getId()).build();
    MinorAction minorAction;

    if (enemy.distance() < persona.getAttack().getNormalRange()) {
      minorAction = moveTo(persona, findSafePosition(persona, enemy));
    } else if (enemy.distance() < persona.getAttack().getLongRange()) {
      actionOrder = TurnAction.ActionOrder.MINOR_FIRST;
      minorAction = moveTo(persona, findCloserToEnemyPosition(persona, enemy));
    } else {
      actionOrder = TurnAction.ActionOrder.MINOR_FIRST;
      majorAction = null;
      minorAction = moveTo(persona, findCloserToEnemyPosition(persona, enemy));
    }

    return TurnAction.builder()
        .majorAction(majorAction)
        .minorAction(minorAction)
        .actionOrder(actionOrder)
        .build();
  }

  private MoveAction moveTo(PersonaEntity persona, Vector2 target) {
    List<Vector2> path = worldRepresentation.computePath(persona, target);
    if (path.isEmpty()) {
      return null;
    }
    return MoveAction.builder().characterId(persona.getId()).positions(path).build();
  }

  public Vector2 findCloserToEnemyPosition(PersonaEntity persona, Enemy enemy) {
    float maxDistance = persona.getMoveDistance() - 0.5f;
    float normalRange = persona.getAttack().getNormalRange();

    Vector2 optimal = GeometryUtils.findClosestAtRange(persona.getPosition(), enemy.personaTarget().getPosition(), maxDistance, normalRange);
    return worldRepresentation.findClosestAvailablePosition(persona, optimal);
  }

  public Vector2 findSafePosition(PersonaEntity persona, Enemy enemy) {
    float maxDistance = persona.getMoveDistance() - 0.5f;
    float normalRange = persona.getAttack().getNormalRange();

    Vector2 optimal = GeometryUtils.findFarthestAtRange(persona.getPosition(), enemy.personaTarget().getPosition(), maxDistance, normalRange);
    return worldRepresentation.findClosestAvailablePosition(persona, optimal);
  }

  public Enemy selectTarget(List<Enemy> enemiesEntity) {
    return findClosest(enemiesEntity);
  }

  public Enemy findClosest(List<Enemy> enemiesEntity) {
    return enemiesEntity.stream().min((o1, o2) -> (int) (Math.abs(o1.distance() - o2.distance()) * 1000))
        .orElseThrow(() -> new IllegalStateException("No Enemy found"));
  }
}
