package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.action.*;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.ChargeAttackAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.entity.action.exception.NotSupportedException;
import net.alteiar.lendyr.entity.action.exception.ProcessingException;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import net.alteiar.lendyr.model.persona.Position;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    throw new NotSupportedException(String.format("The action %s is not supported yet.", action.getActionsCase().name()));
  }

  default LendyrActionResult actionResultToDto(ActionResult actionResult) {
    if (actionResult instanceof GenericActionResult) {
      return LendyrActionResult.newBuilder().build();
    } else if (actionResult instanceof AttackActionResult attackActionResult) {
      return LendyrActionResult.newBuilder().setAttack(attackResultToDto(attackActionResult)).build();
    } else if (actionResult instanceof MoveActionResult attackActionResult) {
      return LendyrActionResult.newBuilder().setMove(moveResultToDto(attackActionResult)).build();
    } else if (actionResult instanceof ChargeAttackActionResult attackActionResult) {
      return LendyrActionResult.newBuilder().setCharge(chargeAttackResultToDto(attackActionResult)).build();
    }

    throw new ProcessingException(String.format("Cannot map the action result of type %s", actionResult.getClass().getSimpleName()));
  }

  LendyrAttackActionResult attackResultToDto(AttackActionResult attackActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrMoveActionResult moveResultToDto(MoveActionResult moveActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrChargeActionResult chargeAttackResultToDto(ChargeAttackActionResult moveActionResult);
}
