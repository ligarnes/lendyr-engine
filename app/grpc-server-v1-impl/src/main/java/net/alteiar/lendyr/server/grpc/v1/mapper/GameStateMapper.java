package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrGameMode;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.model.Game;
import net.alteiar.lendyr.model.PlayState;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface GameStateMapper {
  GameStateMapper INSTANCE = Mappers.getMapper(GameStateMapper.class);

  default LendyrGameState businessToDto(Game game) {

    LendyrGameState.Builder builder = LendyrGameState.newBuilder()
        .setEncounter(EncounterMapper.INSTANCE.businessToDto(game.getEncounter()));

    game.getPersonas().stream().map(PersonaMapper.INSTANCE::personaToDto).forEach(builder::addPersona);

    Optional.ofNullable(game.getLocalMap()).map(WorldMapMapper.INSTANCE::worldMapToDto).ifPresent(builder::setMap);
    Optional.ofNullable(game.getPlayer()).map(PlayerMapper.INSTANCE::businessToDto).ifPresent(builder::setPlayers);
    builder.setPlayState(playStateToDto(game.getPlayState()));

    return builder.build();
  }

  LendyrGameMode playStateToDto(PlayState playState);
}
