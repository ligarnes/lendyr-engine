package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.action.GameAction;
import net.alteiar.lendyr.entity.action.combat.major.AttackAction;
import net.alteiar.lendyr.entity.action.combat.major.ChargeAttackAction;
import net.alteiar.lendyr.entity.action.combat.minor.MoveAction;
import net.alteiar.lendyr.entity.action.exception.NotSupportedException;
import net.alteiar.lendyr.entity.action.exception.ProcessingException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.GameModeChanged;
import net.alteiar.lendyr.entity.event.GameSaved;
import net.alteiar.lendyr.entity.event.combat.AttackGameEvent;
import net.alteiar.lendyr.entity.event.combat.ChargeAttackGameEvent;
import net.alteiar.lendyr.entity.event.combat.MoveGameEvent;
import net.alteiar.lendyr.entity.event.combat.NextCombatPersonaGameEvent;
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

  default LendyrGameEvent actionResultToDto(GameEvent gameEvent) {


    return switch (gameEvent) {
      case AttackGameEvent attackGameEvent -> LendyrGameEvent.newBuilder().setAttack(attackResultToDto(attackGameEvent)).build();
      case MoveGameEvent moveGameEvent -> LendyrGameEvent.newBuilder().setMove(moveResultToDto(moveGameEvent)).build();
      case ChargeAttackGameEvent chargeAttackGameEvent -> LendyrGameEvent.newBuilder().setCharge(chargeAttackResultToDto(chargeAttackGameEvent)).build();
      case NextCombatPersonaGameEvent nextCombatPersona -> LendyrGameEvent.newBuilder().setNextCombatPersona(nextCombatPersonToDto(nextCombatPersona)).build();

      case GameSaved gameSaved -> LendyrGameEvent.newBuilder().setSaved(savedToDto(gameSaved)).build();
      case GameModeChanged gameModeChanged -> LendyrGameEvent.newBuilder().setGameModeChanged(gameModeChangedToDto(gameModeChanged)).build();

      default -> throw new ProcessingException(String.format("Cannot map the action result of type %s", gameEvent.getClass().getSimpleName()));
    };
  }

  LendyrNextCombatPersona nextCombatPersonToDto(NextCombatPersonaGameEvent nextCombatPersonaGameEvent);

  LendyrGameSaved savedToDto(GameSaved saved);

  LendyrGameModeChanged gameModeChangedToDto(GameModeChanged gameModeChanged);

  LendyrAttackActionResult attackResultToDto(AttackGameEvent attackActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrMoveActionResult moveResultToDto(MoveGameEvent moveActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrChargeActionResult chargeAttackResultToDto(ChargeAttackGameEvent moveActionResult);
}
