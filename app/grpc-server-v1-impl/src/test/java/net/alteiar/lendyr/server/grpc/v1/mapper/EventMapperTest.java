package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.GameModeChanged;
import net.alteiar.lendyr.entity.event.combat.AttackGameEvent;
import net.alteiar.lendyr.entity.event.combat.MoveGameEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaPositionChanged;
import net.alteiar.lendyr.entity.event.exploration.RealtimeUpdateEvent;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import net.alteiar.lendyr.model.PlayState;
import net.alteiar.lendyr.model.persona.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class EventMapperTest {

  @Test
  void actionResultToDto_RealtimeUpdateEvent() {
    // Given
    RealtimeUpdateEvent result = RealtimeUpdateEvent.builder().build();
    // When
    LendyrGameEvent dto = EventMapper.INSTANCE.eventResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrGameEvent.ActionsCase.REALTIMEUPDATE, dto.getActionsCase());
  }

  @Test
  void actionResultToDto_attackResult() {
    // Given
    AttackGameEvent result = newAttackResult();
    // When
    LendyrGameEvent dto = EventMapper.INSTANCE.eventResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrGameEvent.ActionsCase.ATTACK, dto.getActionsCase());
  }

  @Test
  void actionResultToDto_moveResult() {
    // Given
    GameEvent result = newMoveAction();
    // When
    LendyrGameEvent dto = EventMapper.INSTANCE.eventResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrGameEvent.ActionsCase.MOVE, dto.getActionsCase());
  }

  @Test
  void realtimeUpdateToDto() {
    // Given
    RealtimeUpdateEvent result = RealtimeUpdateEvent.builder()
        .positions(List.of(
            PersonaPositionChanged.builder().sourceId(UUID.randomUUID()).position(new Position(1, 1, 1)).build(),
            PersonaPositionChanged.builder().sourceId(UUID.randomUUID()).position(new Position(2, 2, 2)).build()
        ))
        .build();

    // When
    LendyrRealTimeUpdate dto = EventMapper.INSTANCE.reatimeUpdateToDto(result);

    // Then
    Assertions.assertEquals(2, dto.getPositionsCount());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getPositions().get(0).getSourceId()), dto.getPositions(0).getSourceId());
    Assertions.assertEquals(result.getPositions().get(0).getPosition().getX(), dto.getPositions(0).getPosition().getX());
    Assertions.assertEquals(result.getPositions().get(0).getPosition().getY(), dto.getPositions(0).getPosition().getY());
    Assertions.assertEquals(result.getPositions().get(0).getPosition().getLayer(), dto.getPositions(0).getPosition().getLayer());

    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getPositions().get(1).getSourceId()), dto.getPositions(1).getSourceId());
    Assertions.assertEquals(result.getPositions().get(1).getPosition().getX(), dto.getPositions(1).getPosition().getX());
    Assertions.assertEquals(result.getPositions().get(1).getPosition().getY(), dto.getPositions(1).getPosition().getY());
    Assertions.assertEquals(result.getPositions().get(1).getPosition().getLayer(), dto.getPositions(1).getPosition().getLayer());
  }


  @Test
  void attackResultToDto() {
    // Given
    AttackGameEvent result = newAttackResult();

    // When
    LendyrAttackActionResult dto = EventMapper.INSTANCE.attackResultToDto(result);

    // Then
    Assertions.assertEquals(result.getRawDamage(), dto.getRawDamage());
    Assertions.assertEquals(result.getMitigatedDamage(), dto.getMitigatedDamage());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getSourceId()), dto.getSourceId());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getTargetId()), dto.getTargetId());
    Assertions.assertEquals(result.getAttackResult().getBonus(), dto.getAttackResult().getBonus());
    Assertions.assertEquals(result.getAttackResult().getDice1(), dto.getAttackResult().getDice1());
    Assertions.assertEquals(result.getAttackResult().getDice2(), dto.getAttackResult().getDice2());
    Assertions.assertEquals(result.getAttackResult().getStunDie(), dto.getAttackResult().getStunDie());
  }

  @Test
  void moveResultToDto() {
    // Given
    MoveGameEvent result = newMoveAction();

    // When
    LendyrMoveActionResult dto = EventMapper.INSTANCE.moveResultToDto(result);

    // Then
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getSourceId()), dto.getSourceId());
    Assertions.assertEquals(result.getPath().size(), dto.getPositionCount());

    for (int i = 0; i < result.getPath().size(); i++) {
      Assertions.assertEquals(result.getPath().get(i).getX(), dto.getPosition(i).getX());
      Assertions.assertEquals(result.getPath().get(i).getY(), dto.getPosition(i).getY());
      Assertions.assertEquals(result.getPath().get(i).getLayer(), dto.getPosition(i).getLayer());
    }
  }

  @Test
  void gameStateToDto() {
    // Given
    GameModeChanged result = new GameModeChanged(PlayState.GAME_OVER);

    // When
    LendyrGameModeChanged dto = EventMapper.INSTANCE.gameModeChangedToDto(result);

    // Then
    Assertions.assertEquals(LendyrGameMode.GAME_OVER, dto.getNewMode());
  }

  private AttackGameEvent newAttackResult() {
    SkillResult skillResult = SkillResult.builder()
        .bonus(1)
        .dice1(2)
        .dice2(3)
        .stunDie(4)
        .build();
    return AttackGameEvent.builder()
        .attackResult(skillResult)
        .sourceId(UUID.randomUUID())
        .targetId(UUID.randomUUID())
        .mitigatedDamage(2)
        .rawDamage(3)
        .build();
  }

  private MoveGameEvent newMoveAction() {
    return MoveGameEvent.builder()
        .sourceId(UUID.randomUUID())
        .path(List.of(
            RandomProvider.INSTANCE.nextObject(Position.class),
            RandomProvider.INSTANCE.nextObject(Position.class),
            RandomProvider.INSTANCE.nextObject(Position.class)
        ))
        .build();
  }
}