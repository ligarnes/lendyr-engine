package net.alteiar.lendyr.entity.npc;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.npc.Npc;
import net.alteiar.lendyr.model.npc.NpcRelationType;
import net.alteiar.lendyr.model.npc.behavior.Behavior;

@Log4j2
public class NpcEntity {
  @Getter
  private final PersonaEntity persona;
  @Getter
  private Behavior behavior;
  @Getter
  private NpcRelationType relationType;

  public NpcEntity(GameEntity gameEntity, Npc npc) {
    this.persona = gameEntity.findById(npc.getPersonaId()).orElseThrow(() -> new RuntimeException("Persona not found"));
    this.behavior = npc.getBehavior();
    this.relationType = npc.getRelationType();
  }

  public boolean isAlive() {
    return !persona.isDefeated();
  }

  public boolean isEnemy() {
    return relationType == NpcRelationType.ENEMY;
  }
}
