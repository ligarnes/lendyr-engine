package net.alteiar.lendyr.entity;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.encounter.CurrentPersona;
import net.alteiar.lendyr.model.encounter.Encounter;

import java.util.List;
import java.util.UUID;

@Log4j2
public final class EncounterEntityImpl implements EncounterEntity {
  private final GameEntity gameEntity;
  @Setter
  private Encounter encounter;

  EncounterEntityImpl(@NonNull GameEntity gameEntity) {
    this.gameEntity = gameEntity;
  }

  @Override
  public boolean isMajorActionUsed() {
    return encounter.getCurrentPersona().isMajorActionUsed();
  }

  @Override
  public boolean isMinorActionUsed() {
    return encounter.getCurrentPersona().isMinorActionUsed();
  }

  @Override
  public int getTurn() {
    return encounter.getTurn();
  }

  @Override
  public int getPersonaTeam(UUID personaId) {
    return encounter.getInitiative().stream().filter(c -> personaId.equals(c.getPersonaId())).mapToInt(CombatActor::getTeam).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No team found"));
  }

  @Override
  public List<CombatActor> getOpponents(int team) {
    return encounter.getInitiative().stream().filter(c -> team != c.getTeam()).toList();
  }

  @Override
  public CombatActor getCurrentPersona() {
    int idx = encounter.getCurrentPersona().getInitiativeIdx();

    return this.encounter.getInitiative().get(idx);
  }

  @Override
  public void newEncounter(List<CombatActor> combatActors) {
    if (!isEncounterComplete()) {
      throw new IllegalStateException("Already in combat");
    }

    encounter = new Encounter();
    encounter.setTurn(0);
    encounter.setInitiative(combatActors);
    encounter.setCurrentPersona(
        CurrentPersona.builder()
            .initiativeIdx(0)
            .minorActionUsed(false)
            .majorActionUsed(false)
            .build()
    );
  }

  @Override
  public boolean isEncounterComplete() {
    if (encounter == null) {
      return true;
    }

    List<Integer> remainingTeams = encounter.getInitiative().stream()
        .filter(p -> !gameEntity.findById(p.getPersonaId()).map(PersonaEntity::isDefeated).orElse(true))
        .map(CombatActor::getTeam)
        .distinct().toList();
    return remainingTeams.size() <= 1;
  }

  @Override
  public void useMinorAction() {
    this.encounter.getCurrentPersona().setMinorActionUsed(true);
  }

  @Override
  public void useMajorAction() {
    this.encounter.getCurrentPersona().setMajorActionUsed(true);
  }

  @Override
  public void endPlayerTurn() {
    // Reset actions
    this.encounter.getCurrentPersona().setMajorActionUsed(false);
    this.encounter.getCurrentPersona().setMinorActionUsed(false);

    // Next player alive
    log.info("Next player turn, was {}", this.encounter.getCurrentPersona().getInitiativeIdx());
    boolean isNextDead = true;
    while (isNextDead) {
      int currentIdx = this.encounter.getCurrentPersona().getInitiativeIdx();
      int nextIdx = currentIdx + 1;
      this.encounter.getCurrentPersona().setInitiativeIdx(nextIdx);
      if (nextIdx >= this.encounter.getInitiative().size()) {
        newTurn();
      }
      isNextDead = gameEntity.findById(getCurrentPersona().getPersonaId()).map(PersonaEntity::isDefeated).orElse(true);
    }
    log.info("Next player turn, now is {}", this.encounter.getCurrentPersona().getInitiativeIdx());
  }

  @Override
  public void newTurn() {
    this.encounter.setTurn(this.encounter.getTurn() + 1);
    this.encounter.getCurrentPersona().setInitiativeIdx(0);
  }

  @Override
  public Encounter toModel() {
    return this.encounter;
  }
}
