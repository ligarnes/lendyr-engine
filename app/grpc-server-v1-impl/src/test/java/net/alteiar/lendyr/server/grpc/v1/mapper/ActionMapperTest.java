package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.entity.SkillResult;
import net.alteiar.lendyr.entity.action.*;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.ChargeAttackAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import net.alteiar.lendyr.grpc.model.v1.generic.LendyrPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class ActionMapperTest {

  @Test
  void gameActionToBusiness_attack() {
    LendyrAction dto = LendyrAction.newBuilder()
        .setAttack(LendyrAttackAction.newBuilder()
            .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .setTargetId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .build())
        .build();

    GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(dto);

    Assertions.assertInstanceOf(AttackAction.class, action);
    AttackAction attackAction = (AttackAction) action;
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getAttack().getSourceId()), attackAction.getSourceId());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getAttack().getTargetId()), attackAction.getTargetId());
  }

  @Test
  void gameActionToBusiness_move() {
    LendyrAction dto = LendyrAction.newBuilder()
        .setMove(LendyrMoveAction.newBuilder()
            .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .addPosition(LendyrPosition.newBuilder().setX(3f).setY(1f).build())
            .addPosition(LendyrPosition.newBuilder().setX(4f).setY(2f).build())
            .addPosition(LendyrPosition.newBuilder().setX(5f).setY(3f).build())
            .build())
        .build();

    GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(dto);

    Assertions.assertInstanceOf(MoveAction.class, action);
    MoveAction attackAction = (MoveAction) action;
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getMove().getSourceId()), attackAction.getCharacterId());
    Assertions.assertEquals(dto.getMove().getPositionCount(), attackAction.getPositions().size());

    for (int i = 0; i < dto.getMove().getPositionCount(); i++) {
      Assertions.assertEquals(dto.getMove().getPosition(i).getX(), attackAction.getPositions().get(i).x);
      Assertions.assertEquals(dto.getMove().getPosition(i).getY(), attackAction.getPositions().get(i).y);
    }
  }

  @Test
  void gameActionToBusiness_charge() {
    LendyrAction dto = LendyrAction.newBuilder()
        .setChargeAttack(LendyrChargeAttackAction.newBuilder()
            .setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .setTargetId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .addPath(LendyrPosition.newBuilder().setX(3f).setY(1f).build())
            .addPath(LendyrPosition.newBuilder().setX(4f).setY(2f).build())
            .addPath(LendyrPosition.newBuilder().setX(5f).setY(3f).build())
            .build())
        .build();

    GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(dto);

    Assertions.assertInstanceOf(ChargeAttackAction.class, action);
    ChargeAttackAction attackAction = (ChargeAttackAction) action;
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getChargeAttack().getSourceId()), attackAction.getSourceId());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getChargeAttack().getTargetId()), attackAction.getTargetId());
    Assertions.assertEquals(dto.getChargeAttack().getPathCount(), attackAction.getPositions().size());

    for (int i = 0; i < dto.getChargeAttack().getPathCount(); i++) {
      Assertions.assertEquals(dto.getChargeAttack().getPath(i).getX(), attackAction.getPositions().get(i).x);
      Assertions.assertEquals(dto.getChargeAttack().getPath(i).getY(), attackAction.getPositions().get(i).y);
    }
  }

  @Test
  void actionResultToDto_generic() {
    // Given
    GenericActionResult result = GenericActionResult.builder().build();
    // When
    LendyrActionResult dto = ActionMapper.INSTANCE.actionResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrActionResult.ActionsCase.ACTIONS_NOT_SET, dto.getActionsCase());
  }

  @Test
  void actionResultToDto_attackResult() {
    // Given
    AttackActionResult result = newAttackResult();
    // When
    LendyrActionResult dto = ActionMapper.INSTANCE.actionResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrActionResult.ActionsCase.ATTACK, dto.getActionsCase());
  }

  @Test
  void actionResultToDto_moveResult() {
    // Given
    ActionResult result = newMoveAction();
    // When
    LendyrActionResult dto = ActionMapper.INSTANCE.actionResultToDto(result);
    // Then
    Assertions.assertEquals(LendyrActionResult.ActionsCase.MOVE, dto.getActionsCase());
  }

  @Test
  void attackResultToDto() {
    // Given
    AttackActionResult result = newAttackResult();

    // When
    LendyrAttackActionResult dto = ActionMapper.INSTANCE.attackResultToDto(result);

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
    MoveActionResult result = newMoveAction();

    // When
    LendyrMoveActionResult dto = ActionMapper.INSTANCE.moveResultToDto(result);

    // Then
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(result.getSourceId()), dto.getSourceId());
    Assertions.assertEquals(result.getPath().size(), dto.getPositionCount());

    for (int i = 0; i < result.getPath().size(); i++) {
      Assertions.assertEquals(result.getPath().get(i).x, dto.getPosition(i).getX());
      Assertions.assertEquals(result.getPath().get(i).y, dto.getPosition(i).getY());
    }
  }

  private AttackActionResult newAttackResult() {
    SkillResult skillResult = SkillResult.builder()
        .bonus(1)
        .dice1(2)
        .dice2(3)
        .stunDie(4)
        .build();
    return AttackActionResult.builder()
        .attackResult(skillResult)
        .sourceId(UUID.randomUUID())
        .targetId(UUID.randomUUID())
        .mitigatedDamage(2)
        .rawDamage(3)
        .build();
  }

  private MoveActionResult newMoveAction() {
    return MoveActionResult.builder()
        .sourceId(UUID.randomUUID())
        .path(List.of(
            RandomProvider.INSTANCE.nextObject(Vector2.class),
            RandomProvider.INSTANCE.nextObject(Vector2.class),
            RandomProvider.INSTANCE.nextObject(Vector2.class)
        ))
        .build();
  }
}