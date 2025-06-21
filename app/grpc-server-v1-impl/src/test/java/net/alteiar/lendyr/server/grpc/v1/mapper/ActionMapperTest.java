package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.ChargeAttackAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.entity.action.exploration.EquipAction;
import net.alteiar.lendyr.entity.action.exploration.MoveToTargetAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import net.alteiar.lendyr.grpc.model.v1.generic.LendyrPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class ActionMapperTest {

  @Test
  void gameActionToBusiness_equip() {
    LendyrAction dto = LendyrAction.newBuilder()
        .setEquip(LendyrEquip.newBuilder()
            .setPersonaId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .setEquipmentId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .setLocation(LendyrEquippedLocation.ARMOR))
        .build();

    GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(dto);

    Assertions.assertInstanceOf(EquipAction.class, action);
    EquipAction attackAction = (EquipAction) action;
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getEquip().getPersonaId()), attackAction.getCharacterId());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getEquip().getEquipmentId()), attackAction.getItemId());
    Assertions.assertEquals(ActionMapper.INSTANCE.locationToModel(dto.getEquip().getLocation()), attackAction.getLocation());
  }

  @Test
  void gameActionToBusiness_moveTo() {
    LendyrAction dto = LendyrAction.newBuilder()
        .setMoveTo(LendyrMoveToAction.newBuilder().setSourceId(GenericMapper.INSTANCE.convertUUIDToBytes(UUID.randomUUID()))
            .setPosition(LendyrPosition.newBuilder().setX(1.2f).setY(2.1f).setLayer(3)))
        .build();

    GameAction action = ActionMapper.INSTANCE.gameActionToBusiness(dto);

    Assertions.assertInstanceOf(MoveToTargetAction.class, action);
    MoveToTargetAction attackAction = (MoveToTargetAction) action;
    Assertions.assertEquals(GenericMapper.INSTANCE.convertBytesToUUID(dto.getMoveTo().getSourceId()), attackAction.getCharacterId());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertPositionFromDto(dto.getMoveTo().getPosition()), attackAction.getTargetPosition());
  }


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
      Assertions.assertEquals(dto.getMove().getPosition(i).getX(), attackAction.getPositions().get(i).getX());
      Assertions.assertEquals(dto.getMove().getPosition(i).getY(), attackAction.getPositions().get(i).getY());
      Assertions.assertEquals(dto.getMove().getPosition(i).getLayer(), attackAction.getPositions().get(i).getLayer());
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
      Assertions.assertEquals(dto.getChargeAttack().getPath(i).getX(), attackAction.getPositions().get(i).getX());
      Assertions.assertEquals(dto.getChargeAttack().getPath(i).getY(), attackAction.getPositions().get(i).getY());
      Assertions.assertEquals(dto.getChargeAttack().getPath(i).getLayer(), attackAction.getPositions().get(i).getLayer());
    }
  }
}