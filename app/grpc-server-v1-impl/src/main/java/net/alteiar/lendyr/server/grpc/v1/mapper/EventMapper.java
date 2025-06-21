package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.entity.action.exception.ProcessingException;
import net.alteiar.lendyr.entity.event.GameEvent;
import net.alteiar.lendyr.entity.event.GameModeChanged;
import net.alteiar.lendyr.entity.event.GameSaved;
import net.alteiar.lendyr.entity.event.combat.AttackGameEvent;
import net.alteiar.lendyr.entity.event.combat.ChargeAttackGameEvent;
import net.alteiar.lendyr.entity.event.combat.MoveGameEvent;
import net.alteiar.lendyr.entity.event.combat.NextCombatPersonaGameEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaChangedEvent;
import net.alteiar.lendyr.entity.event.exploration.PersonaPositionChanged;
import net.alteiar.lendyr.entity.event.exploration.RealtimeUpdateEvent;
import net.alteiar.lendyr.grpc.model.v1.encounter.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = GenericMapper.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface EventMapper {
  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  default LendyrGameEvent eventResultToDto(GameEvent gameEvent) {

    return switch (gameEvent) {
      //  Combat
      case AttackGameEvent attackGameEvent -> LendyrGameEvent.newBuilder().setAttack(attackResultToDto(attackGameEvent)).build();
      case MoveGameEvent moveGameEvent -> LendyrGameEvent.newBuilder().setMove(moveResultToDto(moveGameEvent)).build();
      case ChargeAttackGameEvent chargeAttackGameEvent -> LendyrGameEvent.newBuilder().setCharge(chargeAttackResultToDto(chargeAttackGameEvent)).build();
      case NextCombatPersonaGameEvent nextCombatPersona -> LendyrGameEvent.newBuilder().setNextCombatPersona(nextCombatPersonToDto(nextCombatPersona)).build();

      // Exploration
      case RealtimeUpdateEvent realtimeUpdateEvent -> LendyrGameEvent.newBuilder().setRealtimeUpdate(reatimeUpdateToDto(realtimeUpdateEvent)).build();

      case PersonaChangedEvent personaChangedEvent -> LendyrGameEvent.newBuilder().setPersonaUpdated(
          LendyrPersonaChanged.newBuilder().setUpdatedPersona(PersonaMapper.INSTANCE.personaToDto(personaChangedEvent.getPersona()))).build();

      //
      case GameSaved gameSaved -> LendyrGameEvent.newBuilder().setSaved(savedToDto(gameSaved)).build();
      case GameModeChanged gameModeChanged -> LendyrGameEvent.newBuilder().setGameModeChanged(gameModeChangedToDto(gameModeChanged)).build();

      default -> throw new ProcessingException(String.format("Cannot map the action result of type %s", gameEvent.getClass().getSimpleName()));
    };
  }

  @Mapping(source = "positions", target = "positionsList")
  LendyrRealTimeUpdate reatimeUpdateToDto(RealtimeUpdateEvent event);

  @Mapping(target = "hasNextPosition", expression = "java(position.getNextPosition() != null)")
  LendyrPersonaPositionChanged positionChangedToDto(PersonaPositionChanged position);

  LendyrNextCombatPersona nextCombatPersonToDto(NextCombatPersonaGameEvent nextCombatPersonaGameEvent);

  LendyrGameSaved savedToDto(GameSaved saved);

  LendyrGameModeChanged gameModeChangedToDto(GameModeChanged gameModeChanged);

  LendyrAttackActionResult attackResultToDto(AttackGameEvent attackActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrMoveActionResult moveResultToDto(MoveGameEvent moveActionResult);

  @Mapping(source = "path", target = "positionList")
  LendyrChargeActionResult chargeAttackResultToDto(ChargeAttackGameEvent moveActionResult);
}
