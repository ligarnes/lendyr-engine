package net.alteiar.lendyr.ai.combat.geometry;

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

class PersonaWorldRepresentationFleeTest {

  static LayeredMapWithMovable layeredMapWithMovable;
  static List<DynamicBlockingObject> obstacles;

  @BeforeAll
  static void beforeAll() throws IOException {
    XmlMapper mapper = new XmlMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    TiledMap tiledMap = mapper.readValue(PersonaWorldRepresentationFleeTest.class.getResourceAsStream("/sample.tmx"), TiledMap.class);
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
  void fleeFrom_direct() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(5, 5, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(8f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.fleeFrom(persona, new Position(4, 5, 1));

    // Then
    Assertions.assertEquals(List.of(
        new Position(6, 4, 1),
        new Position(7, 3, 1),
        new Position(8, 2, 1),
        new Position(9, 1, 1),
        new Position(10, 0, 1)
    ), path);
  }

  @Test
  void fleeFrom_multiLayer() {
    PersonaWorldRepresentation personaWorldRepresentation = new PersonaWorldRepresentation(layeredMapWithMovable);

    PersonaEntity persona = Mockito.mock(PersonaEntity.class);
    Mockito.when(persona.getPosition()).thenReturn(new Position(5, 5, 1));
    Mockito.when(persona.getMoveDistance()).thenReturn(8f);
    Mockito.when(persona.getSize()).thenReturn(Size.builder().width(1).height(1).build());

    // When
    personaWorldRepresentation.update();
    List<Position> path = personaWorldRepresentation.fleeFrom(persona, new Position(4, 4, 1));

    // Then
    Assertions.assertEquals(List.of(
        new Position(6, 6, 1),
        new Position(7, 7, 1),
        new Position(7, 8, 2),
        new Position(7, 9, 2),
        new Position(7, 10, 2),
        new Position(8, 11, 2)
    ), path);
  }
}