package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;
import net.alteiar.lendyr.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameStateMapper {
  GameStateMapper INSTANCE = Mappers.getMapper(GameStateMapper.class);

  default LendyrGameState businessToDto(Game game) {

    LendyrGameState.Builder builder = LendyrGameState.newBuilder()
        .setEncounter(EncounterMapper.INSTANCE.businessToDto(game.getEncounter()));

    game.getPersonas().stream().map(PersonaMapper.INSTANCE::personaToDto).forEach(builder::addPersona);

    return builder.build();
  }
}
