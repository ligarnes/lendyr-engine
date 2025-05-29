package net.alteiar.lendyr.entity.action.combat.minor;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.DiceEngine;
import net.alteiar.lendyr.entity.GameEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.action.ActionResult;
import net.alteiar.lendyr.entity.action.GenericActionResult;
import net.alteiar.lendyr.entity.action.exception.NotAllowedException;
import net.alteiar.lendyr.entity.action.exception.NotEnoughActionException;
import net.alteiar.lendyr.entity.action.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
public class MoveAction implements MinorAction {
  private final UUID characterId;
  private final List<Vector2> positions;

  // Stateful variables
  PersonaEntity persona;

  @Builder
  public MoveAction(@NonNull UUID characterId, @NonNull List<Vector2> positions) {
    this.characterId = characterId;
    this.positions = positions;
  }

  @Override
  public void ensureAllowed(GameEntity context) {
    if (positions.isEmpty()) {
      throw new NotAllowedException("Move action requires at least one position");
    }

    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    if (context.getEncounter().isMinorActionUsed()) {
      throw new NotEnoughActionException(String.format("the persona with id [%s] has already consumed the minor action", characterId));
    }
    UUID currentPersonaId = context.getEncounter().getCurrentPersona().getPersonaId();
    if (!Objects.equals(currentPersonaId, characterId)) {
      throw new NotAllowedException(String.format("the persona with id [%s] is not the current ", characterId));
    }

    // Validate distance
    float totalDest = 0;
    Vector2 current = persona.getPosition();
    for (Vector2 target : positions) {
      totalDest += roundToQuarter(current.dst(target));
      current = target;

      // Validate no collision.
      if (context.getMap().checkCollision(characterId, target)) {
        throw new NotAllowedException("the move is illegal because of obstacle");
      }
    }

    if (totalDest > persona.getMoveDistance()) {
      log.info("Invalid distance: {} < {} for {}", persona.getMoveDistance(), totalDest, positions);
      throw new NotAllowedException(
          String.format("the persona with id [%s] cannot move this far; distance: %.1f ; max distance: %.1f ", characterId, persona.getMoveDistance(), totalDest));
    }
  }

  @Override
  public ActionResult apply(GameEntity context, DiceEngine diceEngine) {
    // Move to last position
    log.info("Move {} to {}", characterId, positions);
    persona.setPosition(positions.getLast());
    context.getEncounter().useMinorAction();

    return GenericActionResult.builder().build();
  }

  private float roundToQuarter(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
