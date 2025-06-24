package net.alteiar.lendyr.entity;

import lombok.AllArgsConstructor;
import net.alteiar.lendyr.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PlayerEntity {
  private GameEntity game;
  private Player player;

  public UUID getPlayerId() {
    return player.getId();
  }

  public String getPlayerName() {
    return player.getName();
  }

  public List<PersonaEntity> getControlledPersonas() {
    return player.getControlledPersonaIds().stream().map(game::findById).filter(Optional::isPresent).map(Optional::get).toList();
  }

  public boolean isPlayerControlled(UUID personaId) {
    return player.getControlledPersonaIds().contains(personaId);
  }

  public Player toModel() {
    return player;
  }
}
