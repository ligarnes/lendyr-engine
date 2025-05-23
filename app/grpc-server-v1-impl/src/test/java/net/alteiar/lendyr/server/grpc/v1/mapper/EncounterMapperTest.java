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

    Assertions.assertEquals(encounter.getName(), dto.getName());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(encounter.getMapId()), dto.getMapId());
    Assertions.assertEquals(encounter.getCurrentState().getTurn(), dto.getCurrentState().getCurrentTurn());
    Assertions.assertEquals(encounter.getCurrentState().getCurrentPersona().getInitiativeIdx(), dto.getCurrentState().getActive().getActivePersonaIdx());
    Assertions.assertEquals(encounter.getCurrentState().getCurrentPersona().isMajorActionUsed(), dto.getCurrentState().getActive().getMajorActionUsed());
    Assertions.assertEquals(encounter.getCurrentState().getCurrentPersona().isMinorActionUsed(), dto.getCurrentState().getActive().getMinorActionUsed());
    
    Assertions.assertEquals(encounter.getCurrentState().getInitiative().size(), dto.getCurrentState().getInitiativeOrderCount());

    for (int i = 0; i < encounter.getCurrentState().getInitiative().size(); i++) {
      CombatActor combatActor = encounter.getCurrentState().getInitiative().get(i);
      LendyrCombatActor dtoActor = dto.getCurrentState().getInitiativeOrder(i);
      Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(combatActor.getPersonaId()), dtoActor.getPersonaId());
      Assertions.assertEquals(combatActor.getInitiative(), dtoActor.getInitiative());
      Assertions.assertEquals(combatActor.getTeam(), dtoActor.getTeam());
    }
  }
}