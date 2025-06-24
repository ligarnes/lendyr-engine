package net.alteiar.lendyr.entity;

import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.encounter.Encounter;

import java.util.List;
import java.util.UUID;

public interface EncounterEntity {
  boolean isMajorActionUsed();

  boolean isMinorActionUsed();

  int getTurn();

  int getPersonaTeam(UUID personaId);

  List<CombatActor> getOpponents(int team);

  CombatActor getCurrentPersona();

  void newEncounter(List<CombatActor> combatActors);

  boolean isEncounterComplete();

  void useMinorAction();

  void useMajorAction();

  void endPlayerTurn();

  void newTurn();

  Encounter toModel();

  void setEncounter(Encounter encounter);
}
