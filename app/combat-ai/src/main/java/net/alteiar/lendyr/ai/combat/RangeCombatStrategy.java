package net.alteiar.lendyr.ai.combat;

import lombok.NonNull;
import net.alteiar.lendyr.ai.combat.geometry.PersonaWorldRepresentation;
import net.alteiar.lendyr.entity.GameEntityImpl;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.MajorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MinorAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.model.persona.Position;

import java.util.List;

public class RangeCombatStrategy implements CombatAiActor {

  private final GameEntityImpl game;
  private final PersonaWorldRepresentation worldRepresentation;

  RangeCombatStrategy(@NonNull GameEntityImpl game, @NonNull PersonaWorldRepresentation worldRepresentation) {
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
      minorAction = moveTo(persona, worldRepresentation.fleeFrom(persona, enemy.personaTarget().getPosition()));
    } else if (enemy.distance() < persona.getAttack().getLongRange()) {
      actionOrder = TurnAction.ActionOrder.MINOR_FIRST;
      minorAction = moveTo(persona, enemy.personaTarget().getPosition());
    } else {
      actionOrder = TurnAction.ActionOrder.MINOR_FIRST;
      majorAction = null;
      minorAction = moveTo(persona, enemy.personaTarget().getPosition());
    }

    return TurnAction.builder()
        .majorAction(majorAction)
        .minorAction(minorAction)
        .actionOrder(actionOrder)
        .build();
  }

  private MoveAction moveTo(PersonaEntity persona, Position target) {
    List<Position> path = worldRepresentation.pathTo(persona, target);
    return moveTo(persona, path);
  }

  private MoveAction moveTo(PersonaEntity persona, List<Position> path) {
    if (path.isEmpty()) {
      return null;
    }
    return MoveAction.builder().characterId(persona.getId()).positions(path).build();
  }

  public Enemy selectTarget(List<Enemy> enemiesEntity) {
    return findClosest(enemiesEntity);
  }

  public Enemy findClosest(List<Enemy> enemiesEntity) {
    return enemiesEntity.stream().min((o1, o2) -> (int) (Math.abs(o1.distance() - o2.distance()) * 1000))
        .orElseThrow(() -> new IllegalStateException("No Enemy found"));
  }
}
