package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.engine.action.GameAction;
import net.alteiar.lendyr.engine.action.major.AttackAction;
import net.alteiar.lendyr.engine.action.major.ChargeAttackAction;
import net.alteiar.lendyr.engine.action.minor.MoveAction;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.action.result.AttackActionResult;
import net.alteiar.lendyr.engine.action.result.GenericActionResult;
import net.alteiar.lendyr.engine.entity.exception.NotSupportedException;
import net.alteiar.lendyr.engine.entity.exception.ProcessingException;
import net.alteiar.lendyr.engine.random.SkillResult;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ActionMapper {
  ActionMapper INSTANCE = Mappers.getMapper(ActionMapper.class);

  default GameAction dtoToBusiness(LendyrAction action) {
    if (action == null) {
      return null;
    }
    if (action.hasMove()) {
      LendyrMoveAction move = action.getMove();
      UUID characterId = GenericMapper.INSTANCE.convertBytesToUUID(move.getSourceId());
      List<Vector2> positions = move.getPositionList().stream().map(GenericMapper.INSTANCE::convertPositionFromDto).toList();
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
      List<Vector2> positions = charge.getPathList().stream().map(GenericMapper.INSTANCE::convertPositionFromDto).toList();
      return ChargeAttackAction.builder().sourceId(sourceId).targetId(targetId).positions(positions).build();
    }

    throw new NotSupportedException(String.format("The action %s is not supported yet.", action.getActionsCase().name()));
  }

  default LendyrActionResult businessToDto(ActionResult actionResult) {
    if (actionResult instanceof GenericActionResult) {
      return LendyrActionResult.newBuilder().setType(LendyrActionResultStatusType.SUCCESS).build();
    } else if (actionResult instanceof AttackActionResult attackActionResult) {
      return LendyrActionResult.newBuilder().setType(LendyrActionResultStatusType.SUCCESS)
          .setAttack(LendyrAttackActionResult.newBuilder()
              .setAttackResult(businessToDto(attackActionResult.getAttackResult()))
              .setRawDamage(attackActionResult.getRawDamage())
              .setMitigatedDamage(attackActionResult.getMitigatedDamage()))
          .build();
    }

    throw new ProcessingException(String.format("Cannot map the action result of type %s", actionResult.getClass().getSimpleName()));
  }

  default LendyrSkillResult businessToDto(SkillResult skillResult) {
    return LendyrSkillResult.newBuilder()
        .setDice1(skillResult.getDie1())
        .setDice2(skillResult.getDie2())
        .setStunDie(skillResult.getStunDie())
        .setBonus(skillResult.getBonus())
        .build();
  }
}
