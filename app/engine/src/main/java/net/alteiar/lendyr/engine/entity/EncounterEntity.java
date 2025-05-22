package net.alteiar.lendyr.engine.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.model.encounter.Encounter;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class EncounterEntity {
  private final GameContext gameContext;

  @Getter
  private Encounter encounter;

  private final List<PersonaEntity> personaEntities;

  @Builder
  EncounterEntity(@NonNull GameContext gameContext) {
    this.gameContext = gameContext;
    personaEntities = new ArrayList<>();
  }

  /**
   * Load an encounter.
   *
   * @param encounter the encounter
   */
  public void load(Encounter encounter) {
    this.encounter = encounter;
    personaEntities.clear();
    personaEntities.addAll(encounter.getCurrentState().getInitiative().stream()
        .map(gameContext.getGame()::findById)
        .map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("The persona could not be found")))
        .toList());
  }

  public void useMinorAction() {
    this.encounter.getCurrentState().getCurrentPersona().setMinorActionUsed(true);
  }

  public void useMajorAction() {
    this.encounter.getCurrentState().getCurrentPersona().setMajorActionUsed(true);
  }

  public void endPlayerTurn() {
    // Reset actions
    this.encounter.getCurrentState().getCurrentPersona().setMajorActionUsed(false);
    this.encounter.getCurrentState().getCurrentPersona().setMinorActionUsed(false);

    // Next player
    int currentIdx = this.encounter.getCurrentState().getCurrentPersona().getInitiativeIdx();
    int nextIdx = currentIdx + 1;
    this.encounter.getCurrentState().getCurrentPersona().setInitiativeIdx(nextIdx);
    if (nextIdx >= this.encounter.getCurrentState().getInitiative().size()) {
      newTurn();
    }
  }

  public void newTurn() {
    this.encounter.getCurrentState().setTurn(this.encounter.getCurrentState().getTurn() + 1);
    this.encounter.getCurrentState().getCurrentPersona().setInitiativeIdx(0);
  }

  public Encounter toModel() {
    return this.encounter;
  }
}
