package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import net.alteiar.lendyr.model.Player;
import net.alteiar.lendyr.model.encounter.GameMap;
import net.alteiar.lendyr.model.map.LocalMap;
import net.alteiar.lendyr.model.map.layered.DynamicBlockingObject;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import net.alteiar.lendyr.model.npc.Npc;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.persistence.dao.LocalMapDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class LocalMapEntityTest {
  LocalMapEntity localMapEntity;
  PersonaEntity entityOnMap1;
  PersonaEntity entityOnMap2;
  GameEntity gameEntity;

  @BeforeEach
  void beforeEach() {
    GameMap map = new GameMap();
    TiledMap tiledMap = TiledMap.load(new File("../../assembly/data/tiled/sample.tmx"));
    LocalMapDao localMapDao = new LocalMapDao(map, tiledMap);

    entityOnMap1 = newEntityAt(new Position(4, 4, 1));
    entityOnMap2 = newEntityAt(new Position(4, 4, 2));

    Player player = Mockito.mock(Player.class);
    Mockito.when(player.getControlledPersonaIds()).thenReturn(List.of());

    gameEntity = Mockito.mock(GameEntity.class);
    Mockito.when(gameEntity.findById(entityOnMap1.getId())).thenReturn(Optional.of(entityOnMap1));
    Mockito.when(gameEntity.findById(entityOnMap2.getId())).thenReturn(Optional.of(entityOnMap2));
    Mockito.when(gameEntity.getPlayer()).thenReturn(player);

    Npc npc1 = new Npc(entityOnMap1.getId(), null, null);
    Npc npc2 = new Npc(entityOnMap2.getId(), null, null);
    LocalMap localMap = LocalMap.builder().entities(List.of(npc1, npc2)).build();

    localMapEntity = LocalMapEntity.builder().gameEntity(gameEntity).build();
    localMapEntity.load(localMap, localMapDao);
  }

  @Test
  void checkCollision_normal_noCollision() {
    // Given
    Position newPosition = new Position(3, 3, 1);

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    Assertions.assertFalse(collision);
  }

  @Test
  void checkCollision_betweenTwoLayer_noCollision() {
    // TODO do it using before/after
    // Given
    Position newPosition = new Position(7, 12, 2);

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    //Assertions.assertFalse(collision);
  }

  // 22:42:22.702 [game-engine] WARN  net.alteiar.lendyr.entity.action.combat.minor.MoveAction - Collision of Izidor at (8.0,11.0,2) from (7.0,8.0,2)
  //22:42:22.704 [game-engine] WARN  net.alteiar.lendyr.engine.PersonaEngine - AI try unauthorized action: MoveAction(characterId=b66c4a7e-9f5f-40c7-82d4-f442e5c7fe81, positions=[(7.0,9.0,2), (8.0,10.0,2), (8.0,11.0,2)], persona=PersonaEntity{name=Izidor})

  @Test
  void checkCollision_bug_collision() {
    // TODO do it using before/after
    // Given
    Position newPosition = new Position(8, 11, 2);

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    Assertions.assertFalse(collision);
  }

  @Test
  void checkCollision_outOfMap_collision() {
    // Given
    Position newPosition = new Position(-1, -1, 1);

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    Assertions.assertTrue(collision);
  }

  @Test
  void checkCollision_outOfLayer_collision() {
    // Given
    Position newPosition = new Position(2, 2, 2);

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    Assertions.assertTrue(collision);
  }

  @Test
  void checkCollision_withOther_collision() {
    // Given
    Position newPosition = entityOnMap1.getPosition();

    UUID personaId = UUID.randomUUID();
    PersonaEntity persona = newEntityAt(newPosition);
    Mockito.when(gameEntity.findById(personaId)).thenReturn(Optional.of(persona));

    // When
    boolean collision = localMapEntity.checkCollision(personaId, newPosition);

    // Then
    Assertions.assertTrue(collision);
  }


  private PersonaEntity newEntityAt(Position position) {
    DynamicBlockingObject boundingBox = new DynamicBlockingObject(new Rectangle(position.getX(), position.getY(), 1, 1), position.getLayer());

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getId()).thenReturn(UUID.randomUUID());
    Mockito.when(persona.getBoundingBoxAt(position)).thenReturn(boundingBox);
    Mockito.when(persona.getDefenceBoundingBox()).thenReturn(boundingBox);
    Mockito.when(persona.getPosition()).thenReturn(position);
    Mockito.when(persona.getLayer()).thenReturn(position.getLayer());

    return persona;
  }
}