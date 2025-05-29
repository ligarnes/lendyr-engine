package net.alteiar.lendyr.entity;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.encounter.Encounter;

import java.util.List;
import java.util.UUID;

@Log4j2
public final class EncounterEntity {
  @Setter
  private Encounter encounter;

  public boolean isMajorActionUsed() {
    return encounter.getCurrentPersona().isMajorActionUsed();
  }

  public boolean isMinorActionUsed() {
    return encounter.getCurrentPersona().isMinorActionUsed();
  }

  public int getPersonaTeam(UUID personaId) {
    return encounter.getInitiative().stream().filter(c -> personaId.equals(c.getPersonaId())).mapToInt(CombatActor::getTeam).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No team found"));
  }

  public List<CombatActor> getOpponents(int team) {
    return encounter.getInitiative().stream().filter(c -> team != c.getTeam()).toList();
  }

  public CombatActor getCurrentPersona() {
    int idx = encounter.getCurrentPersona().getInitiativeIdx();

    return this.encounter.getInitiative().get(idx);
  }

  public boolean isEncounterComplete() {
    List<Integer> remainingTeams = encounter.getInitiative().stream().map(CombatActor::getTeam).distinct().toList();
    return remainingTeams.size() <= 1;
  }

  public void useMinorAction() {
    this.encounter.getCurrentPersona().setMinorActionUsed(true);
  }

  public void useMajorAction() {
    this.encounter.getCurrentPersona().setMajorActionUsed(true);
  }

  public void endPlayerTurn() {
    // Reset actions
    this.encounter.getCurrentPersona().setMajorActionUsed(false);
    this.encounter.getCurrentPersona().setMinorActionUsed(false);

    // Next player
    int currentIdx = this.encounter.getCurrentPersona().getInitiativeIdx();
    int nextIdx = currentIdx + 1;
    this.encounter.getCurrentPersona().setInitiativeIdx(nextIdx);
    if (nextIdx >= this.encounter.getInitiative().size()) {
      newTurn();
    }
  }

  public void newTurn() {
    this.encounter.setTurn(this.encounter.getTurn() + 1);
    this.encounter.getCurrentPersona().setInitiativeIdx(0);
  }

  public Encounter toModel() {
    return this.encounter;
  }
}
