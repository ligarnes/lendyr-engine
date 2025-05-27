package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrCombatActor;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrEncounter;
import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.encounter.Encounter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncounterMapperTest {

  @Test
  void test() {
    Encounter encounter = RandomProvider.INSTANCE.nextObject(Encounter.class);

    LendyrEncounter dto = EncounterMapper.INSTANCE.businessToDto(encounter);

    Assertions.assertEquals(encounter.getTurn(), dto.getCurrentTurn());
    Assertions.assertEquals(encounter.getCurrentPersona().getInitiativeIdx(), dto.getActive().getActivePersonaIdx());
    Assertions.assertEquals(encounter.getCurrentPersona().isMajorActionUsed(), dto.getActive().getMajorActionUsed());
    Assertions.assertEquals(encounter.getCurrentPersona().isMinorActionUsed(), dto.getActive().getMinorActionUsed());

    Assertions.assertEquals(encounter.getInitiative().size(), dto.getInitiativeOrderCount());

    for (int i = 0; i < encounter.getInitiative().size(); i++) {
      CombatActor combatActor = encounter.getInitiative().get(i);
      LendyrCombatActor dtoActor = dto.getInitiativeOrder(i);
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(combatActor.getPersonaId()), dtoActor.getPersonaId());
      Assertions.assertEquals(combatActor.getInitiative(), dtoActor.getInitiative());
      Assertions.assertEquals(combatActor.getTeam(), dtoActor.getTeam());
    }
  }
}