package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.map.layered.DynamicBlockingObject;
import net.alteiar.lendyr.model.map.layered.LayeredMap;
import net.alteiar.lendyr.model.map.layered.LayeredMapWithMovable;
import net.alteiar.lendyr.model.map.layered.MapFactory;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class PersonaWorldRepresentationTest {

  static LayeredMapWithMovable layeredMapWithMovable;
  static List<DynamicBlockingObject> obstacles;

  @BeforeAll
  static void beforeAll() throws IOException {
    XmlMapper mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    TiledMap tiledMap = mapper.readValue(PersonaWorldRepresentationTest.class.getResourceAsStream("/sample.tmx"), TiledMap.class);
    MapFactory mapFactory = new MapFactory(tiledMap);

    LayeredMap layeredMap = mapFactory.load();
    obstacles = new ArrayList<>();
    layeredMapWithMovable = new LayeredMapWithMovable() {
      @Override
      public LayeredMap getLayeredMap() {
        return layeredMap;
      }

      @Override
      public Stream<DynamicBlockingObject> getMovableObjects() {
        return obstacles.stream();
      }
    };
  }

  @BeforeEach
  void beforeEach() {
    obstacles.clear();
  }

  @Test
  void pathTo_withSimpleObstacle() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(1, 1, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(8f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(7, 1, 1));

    // Then
    Assertions.assertEquals(List.of(
        new Position(2, 1, 1),
        new Position(3, 1, 1),
        new Position(4, 1, 1),
        new Position(5, 1, 1),
        new Position(6, 0, 1),
        new Position(7, 1, 1)
    ), path);
  }

  @Test
  void pathTo_withLimitedSpeed() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(1, 1, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(8f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(15, 1, 1));

    // Then
    Assertions.assertEquals(List.of(
        new Position(2, 1, 1),
        new Position(3, 1, 1),
        new Position(4, 1, 1),
        new Position(5, 0, 1),
        new Position(6, 0, 1),
        new Position(7, 0, 1),
        new Position(8, 0, 1)
    ), path);
  }

  @Test
  void pathTo_changingLayer() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(4, 8, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(10f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(8, 13, 3));

    // Then
    Assertions.assertEquals(List.of(
        new Position(5, 8, 1),
        new Position(6, 8, 1),
        new Position(7, 7, 1),
        new Position(7, 8, 2),
        new Position(7, 9, 2),
        new Position(7, 10, 2),
        new Position(7, 11, 2),
        new Position(7, 12, 3),
        new Position(8, 13, 3)
    ), path);

  }

  @Test
  void pathTo_changing2Layer() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(5, 8, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(30f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(15, 18, 5));

    // Then
    Assertions.assertEquals(List.of(
        new Position(6, 8, 1),
        new Position(7, 7, 1),
        new Position(7, 8, 2),
        new Position(7, 9, 2),
        new Position(7, 10, 2),
        new Position(8, 11, 2),
        new Position(9, 12, 3),
        new Position(10, 12, 3),
        new Position(11, 13, 3),
        new Position(12, 13, 3),
        new Position(13, 13, 3),
        new Position(14, 13, 3),
        new Position(15, 13, 3),
        new Position(15, 14, 4),
        new Position(15, 15, 4),
        new Position(15, 16, 4),
        new Position(15, 17, 4),
        new Position(15, 18, 5)
    ), path);
  }

  @Test
  void pathTo_changingComplex() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(2, 2, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(1000f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(20, 18, 5));

    // Then
    Assertions.assertEquals(List.of(
        new Position(3, 3, 1),
        new Position(4, 4, 1),
        new Position(5, 5, 1),
        new Position(6, 6, 1),
        new Position(7, 7, 1),
        new Position(7, 8, 2),
        new Position(8, 9, 2),
        new Position(8, 10, 2),
        new Position(8, 11, 2),
        new Position(9, 12, 3),
        new Position(10, 12, 3),
        new Position(11, 12, 3),
        new Position(12, 13, 3),
        new Position(13, 13, 3),
        new Position(14, 13, 3),
        new Position(15, 13, 3),
        new Position(16, 14, 4),
        new Position(16, 15, 4),
        new Position(16, 16, 4),
        new Position(16, 17, 4),
        new Position(17, 17, 5),
        new Position(18, 17, 5),
        new Position(19, 17, 5),
        new Position(20, 18, 5)
    ), path);
  }

  @Test
  void pathTo_withObstacles_changingComplex() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(2, 2, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(1000f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    obstacles.add(new DynamicBlockingObject(new Rectangle(1, 4, 9, 3), 1));
    obstacles.add(new DynamicBlockingObject(new Rectangle(1, 7, 1, 8), 1));
    obstacles.add(new DynamicBlockingObject(new Rectangle(10, 6, 2, 1), 1));
    obstacles.add(new DynamicBlockingObject(new Rectangle(13, 6, 2, 1), 1));

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(20, 18, 5));

    // Then
    Assertions.assertEquals(List.of(
        new Position(3, 2, 1),
        new Position(4, 2, 1),
        new Position(5, 2, 1),
        new Position(6, 2, 1),
        new Position(7, 3, 1),
        new Position(8, 3, 1),
        new Position(9, 3, 1),
        new Position(10, 4, 1),
        new Position(11, 5, 1),
        new Position(12, 6, 1),
        new Position(11, 7, 1),
        new Position(10, 7, 1),
        new Position(9, 7, 1),
        new Position(8, 7, 1),
        new Position(8, 8, 2),
        new Position(8, 9, 2),
        new Position(8, 10, 2),
        new Position(8, 11, 2),
        new Position(9, 11, 3),
        new Position(10, 11, 3),
        new Position(11, 11, 3),
        new Position(12, 11, 3),
        new Position(13, 11, 3),
        new Position(14, 12, 3),
        new Position(15, 13, 3),
        new Position(16, 14, 4),
        new Position(16, 15, 4),
        new Position(16, 16, 4),
        new Position(16, 17, 4),
        new Position(17, 17, 5),
        new Position(18, 18, 5),
        new Position(19, 18, 5),
        new Position(20, 18, 5)
    ), path);
  }


  @Test
  void pathToObstacle_withObstacles_changingComplex() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(2, 2, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(1000f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    obstacles.add(new DynamicBlockingObject(new Rectangle(1, 4, 9, 3), 1));

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.pathTo(persona, new Position(3, 6, 1));

    // Then
    Assertions.assertEquals(List.of(
        new Position(1, 3, 1),
        new Position(0, 4, 1),
        new Position(0, 5, 1),
        new Position(0, 6, 1),
        new Position(1, 7, 1),
        new Position(2, 7, 1)
    ), path);
  }
}