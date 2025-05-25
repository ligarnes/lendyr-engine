package net.alteiar.lendyr.engine.action.minor;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.engine.action.GameAction;
import net.alteiar.lendyr.engine.action.result.ActionResult;
import net.alteiar.lendyr.engine.action.result.GenericActionResult;
import net.alteiar.lendyr.engine.entity.exception.NotAllowedException;
import net.alteiar.lendyr.engine.entity.exception.NotEnoughActionException;
import net.alteiar.lendyr.engine.entity.exception.NotFoundException;
import net.alteiar.lendyr.model.persona.Persona;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Actions are not thread safe.
 */
@Log4j2
public class MoveAction implements GameAction {
  private final UUID characterId;
  private final List<Vector2> positions;

  // Stateful variables
  Persona persona;

  @Builder
  public MoveAction(@NonNull UUID characterId, @NonNull List<Vector2> positions) {
    this.characterId = characterId;
    this.positions = positions;
  }

  @Override
  public void ensureAllowed(GameContext context) {
    if (positions.isEmpty()) {
      throw new NotAllowedException("Move action requires at least one position");
    }

    persona = context.findById(characterId)
        .orElseThrow(() -> new NotFoundException(String.format("the persona with id [%s] does not exists", characterId)));

    if (context.getGame().getEncounter().isMinorActionUsed()) {
      throw new NotEnoughActionException(String.format("the persona with id [%s] has already consumed the minor action", characterId));
    }
    UUID currentPersonaId = context.getGame().getEncounter().getCurrentPersona().getPersonaId();
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
      if (context.getGame().getMap().checkCollision(characterId, target)) {
        throw new NotAllowedException("the move is illegal because of obstacle");
      }
    }

    double halfSpeed = Math.ceil(persona.getSpeed() / 2d);
    if (totalDest > halfSpeed) {
      log.info("Invalid distance: {} < {} for {}", halfSpeed, totalDest, positions);
      throw new NotAllowedException(
          String.format("the persona with id [%s] cannot move this far; distance: %.0f ; max distance: %.0f ", characterId, halfSpeed, totalDest));
    }
  }

  @Override
  public ActionResult apply(GameContext context) {
    // Move to last position
    log.info("Move {} to {}", characterId, positions);
    persona.setPosition(positions.getLast());
    context.getGame().getEncounter().useMinorAction();

    return GenericActionResult.builder().build();
  }

  private float roundToQuarter(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
