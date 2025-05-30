package net.alteiar.lendyr.ai.combat;

import com.badlogic.gdx.math.Vector2;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.ai.combat.geometry.GeometryUtils;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.MajorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MinorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;

import java.util.List;

@Log4j2
public class MeleeCombatStrategy implements CombatAiActor {

  private final GameEntity game;

  MeleeCombatStrategy(@NonNull GameEntity game) {
    this.game = game;
  }

  @Override
  public TurnAction combatTurn(PersonaEntity persona) {
    List<Enemy> enemiesEntity = EnemyUtils.listEnemies(game, persona);
    Enemy enemy = selectTarget(enemiesEntity);

    TurnAction.ActionOrder actionOrder = TurnAction.ActionOrder.MINOR_FIRST;
    MajorAction majorAction = AttackAction.builder().sourceId(persona.getId()).targetId(enemy.personaTarget().getId()).build();
    MinorAction minorAction;

    if (enemy.distance() <= persona.getAttack().getNormalRange()) {
      log.info("Enemy is in range (dist: {} vs attack range: {})", enemy.distance(), persona.getAttack().getNormalRange());
      minorAction = null;
    } else {
      log.info("Enemy is too far, just move (dist: {} vs attack range: {})", enemy.distance(), persona.getAttack().getNormalRange());
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
    List<Vector2> path = Pathfinding.computePath(game, persona, target);
    return MoveAction.builder().characterId(persona.getId()).positions(path).build();
  }

  public Vector2 findCloserToEnemyPosition(PersonaEntity persona, Enemy enemy) {
    float maxDistance = persona.getMoveDistance() - 0.5f;
    float normalRange = persona.getAttack().getNormalRange();

    return GeometryUtils.findClosestAtRange(persona.getPosition(), enemy.personaTarget().getPosition(), maxDistance, normalRange);
  }

  public Enemy selectTarget(List<Enemy> enemiesEntity) {
    return findClosest(enemiesEntity);
  }

  public Enemy findClosest(List<Enemy> enemiesEntity) {
    return enemiesEntity.stream().min((o1, o2) -> (int) (Math.abs(o1.distance() - o2.distance()) * 1000))
        .orElseThrow(() -> new IllegalStateException("No Enemy found"));
  }
}
