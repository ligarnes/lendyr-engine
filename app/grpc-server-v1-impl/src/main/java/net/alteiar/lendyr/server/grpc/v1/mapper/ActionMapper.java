package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.ChargeAttackAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.entity.action.exception.NotSupportedException;
import net.alteiar.lendyr.entity.action.exploration.MoveToTargetAction;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import net.alteiar.lendyr.model.persona.Position;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(
    uses = GenericMapper.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ActionMapper {
  ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

  default GameAction gameActionToBusiness(LendyrAction action) {
    if (action == null) {
      return null;
    }
    if (action.hasMove()) {
      LendyrMoveAction move = action.getMove();
      UUID characterId = GenericMapper.INSTANCE.convertBytesToUUID(move.getSourceId());
      List<Position> positions = move.getPositionList().stream().map(GenericMapper.INSTANCE::convertPositionFromDto).toList();
      return MoveAction.builder().characterId(characterId).positions(positions).build();
    }
    if (action.hasAttack()) {
      LendyrAttackAction attack = action.getAttack();
      UUID sourceId = GenericMapper.INSTANCE.convertBytesToUUID(attack.getSourceId());
      UUID targetId = GenericMapper.INSTANCE.convertBytesToUUID(attack.getTargetId());
      return AttackAction.builder().sourceId(sourceId).targetId(targetId).build();
    }
    if (action.hasChargeAttack()) {
      LendyrChargeAttackAction charge = action.getChargeAttack();
      UUID sourceId = GenericMapper.INSTANCE.convertBytesToUUID(charge.getSourceId());
      UUID targetId = GenericMapper.INSTANCE.convertBytesToUUID(charge.getTargetId());
      List<Position> positions = charge.getPathList().stream().map(GenericMapper.INSTANCE::convertPositionFromDto).toList();
      return ChargeAttackAction.builder().sourceId(sourceId).targetId(targetId).positions(positions).build();
    }
    if (action.hasMoveTo()) {
      LendyrMoveToAction moveTo = action.getMoveTo();
      UUID sourceId = GenericMapper.INSTANCE.convertBytesToUUID(moveTo.getSourceId());
      Position targetPosition = GenericMapper.INSTANCE.convertPositionFromDto(moveTo.getPosition());
      return MoveToTargetAction.builder().characterId(sourceId).targetPosition(targetPosition).build();
    }

    throw new NotSupportedException(String.format("The action %s is not supported yet.", action.getActionsCase().name()));
  }
}
