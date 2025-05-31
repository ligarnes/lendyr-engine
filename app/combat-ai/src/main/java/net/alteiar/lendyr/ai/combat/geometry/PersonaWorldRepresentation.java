package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.algorithm.battlemap.GridNetwork;
import net.alteiar.lendyr.algorithm.battlemap.Tile;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.map.LayeredMap;
import net.alteiar.lendyr.entity.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PersonaWorldRepresentation {
  private final WorldMap map;
  private Pathfinding layer1;

  public PersonaWorldRepresentation(WorldMap map) {
    this.map = map;
  }

  private void load() {
    if (layer1 != null) {
      return;
    }
    if (map.getLayeredMap() != null) {
      GridNetwork gridNetwork1 = generate(map.getLayeredMap(), 1);
      layer1 = Pathfinding.builder().gridNetwork(gridNetwork1).build();
    }
  }

  public void update() {
    load();
    if (map.getLayeredMap() != null) {
      layer1.getGridNetwork().getTiles().forEach(Tile::reset);
      this.map.getMovableObjects().forEach(rectangle -> {
        layer1.getGridNetwork().find(Math.round(rectangle.getX()), Math.round(rectangle.getY())).setMovableObject();
      });
      debug();
    }
  }

  public Vector2 findClosestAvailablePosition(PersonaEntity entity, Vector2 target) {
    clampToWorld(entity, target);
    Tile targetTile = layer1.getGridNetwork().find(Math.round(target.x), Math.round(target.y));
    log.info("Find closest position to ({},{})", targetTile.getX(), targetTile.getY());
    Vector2 found = findClosestAvailablePosition(entity, targetTile);
    log.info("Resolved to ({},{})", found.x, found.y);
    return found;
  }

  private Vector2 findClosestAvailablePosition(PersonaEntity entity, Tile target) {
    if (target.isValid()) {
      return new Vector2(target.getX(), target.getY());
    }

    List<Tile> neighbors = target.getNeighbours();
    for (Tile neighbor : neighbors) {
      if (neighbor.isValid()) {
        return new Vector2(neighbor.getX(), neighbor.getY());
      }
    }

    // Recursive find for the next
    return findClosestAvailablePosition(entity, target.getNeighbours().get(0));
  }

  public List<Vector2> computePath(PersonaEntity entity, Vector2 end) {
    return layer1.computePath(entity, end);
  }

  private GridNetwork generate(LayeredMap mapEntity, int level) {
    ArrayList<Tile> tiles = new ArrayList<>();

    int width = (int) mapEntity.getWidth();
    int height = (int) mapEntity.getHeight();

    for (float i = 0; i < mapEntity.getWidth(); i++) {
      for (float j = 0; j < mapEntity.getHeight(); j++) {
        boolean collision = mapEntity.checkCollision(level, new Rectangle(i, j, 1, 1));
        Tile t = new Tile(new Vector2(i, j), !collision);
        tiles.add(t);
      }
    }

    GridNetwork gridNetwork = GridNetwork.builder().width(width).height(height).tiles(tiles).build();
    for (Tile t : gridNetwork.getTiles()) {
      t.calculateNeighbours(gridNetwork);
    }

    return gridNetwork;
  }

  private Vector2 clampToWorld(PersonaEntity persona, Vector2 target) {
    float personaWidth = persona.getSize().getWidth();
    float personaHeigth = persona.getSize().getHeight();
    target.x = MathUtils.clamp(target.x, personaWidth, layer1.getGridNetwork().getWidth() - personaWidth);
    target.y = MathUtils.clamp(target.y, personaHeigth, layer1.getGridNetwork().getHeight() - personaHeigth);
    return target;
  }

  public void debug() {
    log.info("layer {}: ", 1);
    layer1.debug();
  }
}
