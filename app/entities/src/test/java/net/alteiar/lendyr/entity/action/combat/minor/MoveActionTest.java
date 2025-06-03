package net.alteiar.lendyr.entity.action.combat.minor;

import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.LocalMapEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotEnoughActionException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;
import net.alteiar.lendyr.model.encounter.CombatActor;
import net.alteiar.lendyr.model.persona.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class MoveActionTest {

  @Test
  void ensureAllowed_noPosition() {
    GameEntity game = Mockito.mock(GameEntity.class);
    MoveAction moveAction = MoveAction.builder().characterId(UUID.randomUUID()).positions(List.of()).build();

    // When
    NotAllowedException ex = Assertions.assertThrows(NotAllowedException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("Move action requires at least one position", ex.getMessage());
  }

  @Test
  void ensureAllowed_entityNotFound() {
    GameEntity game = Mockito.mock(GameEntity.class);
    MoveAction moveAction = MoveAction.builder().characterId(UUID.randomUUID()).positions(List.of(new Position(1, 1, 1))).build();

    // When
    NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("the persona with id [%s] does not exists".formatted(moveAction.getCharacterId()), ex.getMessage());
  }

  @Test
  void ensureAllowed_minorActionAlreadyUsed() {
    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());

    EncounterEntity encounter = Mockito.mock(EncounterEntity.class);
    Mockito.when(encounter.isMinorActionUsed()).thenReturn(true);

    GameEntity game = Mockito.mock(GameEntity.class);
    Mockito.when(game.findById(persona.getId())).thenReturn(Optional.of(persona));
    Mockito.when(game.getEncounter()).thenReturn(encounter);

    MoveAction moveAction = MoveAction.builder().characterId(persona.getId()).positions(List.of(new Position(1, 1, 1))).build();

    // When
    NotEnoughActionException ex = Assertions.assertThrows(NotEnoughActionException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("the persona with id [%s] has already consumed the minor action".formatted(moveAction.getCharacterId()), ex.getMessage());
  }

  @Test
  void ensureAllowed_notCurrentUser() {
    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());

    CombatActor combatActor = CombatActor.builder().personaId(UUID.randomUUID()).build();

    EncounterEntity encounter = Mockito.mock(EncounterEntity.class);
    Mockito.when(encounter.isMinorActionUsed()).thenReturn(false);
    Mockito.when(encounter.getCurrentPersona()).thenReturn(combatActor);

    GameEntity game = Mockito.mock(GameEntity.class);
    Mockito.when(game.findById(persona.getId())).thenReturn(Optional.of(persona));
    Mockito.when(game.getEncounter()).thenReturn(encounter);

    MoveAction moveAction = MoveAction.builder().characterId(persona.getId()).positions(List.of(new Position(1, 1, 1))).build();

    // When
    NotAllowedException ex = Assertions.assertThrows(NotAllowedException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("the persona with id [%s] is not the current ".formatted(moveAction.getCharacterId()), ex.getMessage());
  }

  @Test
  void ensureAllowed_collision() {
    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());
    Mockito.when(persona.getPosition()).thenReturn(new Position(0, 0, 1));

    CombatActor combatActor = CombatActor.builder().personaId(persona.getId()).build();

    EncounterEntity encounter = Mockito.mock(EncounterEntity.class);
    Mockito.when(encounter.isMinorActionUsed()).thenReturn(false);
    Mockito.when(encounter.getCurrentPersona()).thenReturn(combatActor);

    LocalMapEntity map = Mockito.mock(LocalMapEntity.class);
    Mockito.when(map.checkCollision(Mockito.any(), Mockito.any())).thenReturn(true);

    GameEntity game = Mockito.mock(GameEntity.class);
    Mockito.when(game.findById(persona.getId())).thenReturn(Optional.of(persona));
    Mockito.when(game.getEncounter()).thenReturn(encounter);
    Mockito.when(game.getMap()).thenReturn(map);

    MoveAction moveAction = MoveAction.builder().characterId(persona.getId()).positions(List.of(new Position(1, 1, 1))).build();

    // When
    NotAllowedException ex = Assertions.assertThrows(NotAllowedException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("the move is illegal because of obstacle", ex.getMessage());
  }

  @Test
  void ensureAllowed_tooLong() {
    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());
    Mockito.when(persona.getMoveDistance()).thenReturn(5f);
    Mockito.when(persona.getPosition()).thenReturn(new Position(0, 0, 1));

    CombatActor combatActor = CombatActor.builder().personaId(persona.getId()).build();

    EncounterEntity encounter = Mockito.mock(EncounterEntity.class);
    Mockito.when(encounter.isMinorActionUsed()).thenReturn(false);
    Mockito.when(encounter.getCurrentPersona()).thenReturn(combatActor);

    LocalMapEntity map = Mockito.mock(LocalMapEntity.class);
    Mockito.when(map.checkCollision(Mockito.any(), Mockito.any())).thenReturn(false);

    GameEntity game = Mockito.mock(GameEntity.class);
    Mockito.when(game.findById(persona.getId())).thenReturn(Optional.of(persona));
    Mockito.when(game.getEncounter()).thenReturn(encounter);
    Mockito.when(game.getMap()).thenReturn(map);

    MoveAction moveAction = MoveAction.builder().characterId(persona.getId()).positions(List.of(new Position(1, 10, 1))).build();

    // When
    NotAllowedException ex = Assertions.assertThrows(NotAllowedException.class, () -> moveAction.ensureAllowed(game));

    // Then
    Assertions.assertEquals("the persona with id [%s] cannot move this far; distance: 10.0 ; max distance: 5.0 ".formatted(persona.getId()), ex.getMessage());
  }

  @Test
  void ensureAllowed_valid() {
    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());
    Mockito.when(persona.getMoveDistance()).thenReturn(5f);
    Mockito.when(persona.getPosition()).thenReturn(new Position(0, 0, 1));

    CombatActor combatActor = CombatActor.builder().personaId(persona.getId()).build();

    EncounterEntity encounter = Mockito.mock(EncounterEntity.class);
    Mockito.when(encounter.isMinorActionUsed()).thenReturn(false);
    Mockito.when(encounter.getCurrentPersona()).thenReturn(combatActor);

    LocalMapEntity map = Mockito.mock(LocalMapEntity.class);
    Mockito.when(map.checkCollision(Mockito.any(), Mockito.any())).thenReturn(false);

    GameEntity game = Mockito.mock(GameEntity.class);
    Mockito.when(game.findById(persona.getId())).thenReturn(Optional.of(persona));
    Mockito.when(game.getEncounter()).thenReturn(encounter);
    Mockito.when(game.getMap()).thenReturn(map);

    MoveAction moveAction = MoveAction.builder().characterId(persona.getId()).positions(List.of(new Position(1, 3, 1))).build();

    // When
    Assertions.assertDoesNotThrow(() -> moveAction.ensureAllowed(game));
  }
}