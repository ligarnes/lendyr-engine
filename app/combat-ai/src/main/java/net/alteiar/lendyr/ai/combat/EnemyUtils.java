package net.alteiar.lendyr.ai.combat;

import lombok.experimental.UtilityClass;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.encounter.CombatActor;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class EnemyUtils {

  public static List<Enemy> listEnemies(GameEntity gameEntity, PersonaEntity persona) {
    int team = gameEntity.getEncounter().getPersonaTeam(persona.getId());
    List<CombatActor> enemies = gameEntity.getEncounter().getOpponents(team);

    return enemies.stream()
        .map(CombatActor::getPersonaId)
        .map(gameEntity::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .filter(p -> !p.isDefeated())
        .map(p -> createEnemy(p, persona))
        .toList();
  }

  public Enemy createEnemy(PersonaEntity personaEntity, PersonaEntity currentPersona) {
    return Enemy.builder()
        .personaTarget(personaEntity)
        .attackType(personaEntity.getAttack().getAttackType())
        .distance(personaEntity.getPosition().dst(currentPersona.getPosition()))
        .build();
  }
}
