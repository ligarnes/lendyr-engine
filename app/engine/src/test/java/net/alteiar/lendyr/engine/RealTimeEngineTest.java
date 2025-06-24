package net.alteiar.lendyr.engine;

import net.alteiar.lendyr.entity.GameEntityImpl;
import net.alteiar.lendyr.entity.LocalMapEntity;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.model.map.layered.LayeredMap;
import net.alteiar.lendyr.model.map.layered.MapFactory;
import net.alteiar.lendyr.model.map.tiled.TiledMap;
import net.alteiar.lendyr.model.persona.Persona;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;
import net.alteiar.lendyr.persistence.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class RealTimeEngineTest {

  RealTimeEngine realTimeEngine;
  List<PersonaEntity> personaEntities;
  ItemRepository itemRepository;

  @BeforeEach
  void beforeEach() {
    TiledMap tiledMap = TiledMap.load(new File("../../assembly/data/tiled/sample.tmx"));
    LayeredMap layeredMap = new MapFactory(tiledMap).load();

    personaEntities = new ArrayList<>();

    LocalMapEntity localMapEntity = Mockito.mock(LocalMapEntity.class);
    Mockito.when(localMapEntity.getLayeredMap()).thenReturn(layeredMap);
    Mockito.when(localMapEntity.getPcEntities()).thenReturn(personaEntities);

    GameEntityImpl gameEntity = Mockito.mock(GameEntityImpl.class);
    Mockito.when(gameEntity.getMap()).thenReturn(localMapEntity);

    GameContext context = Mockito.mock(GameContext.class);
    Mockito.when(context.getGame()).thenReturn(gameEntity);

    realTimeEngine = new RealTimeEngine(context);
    itemRepository = Mockito.mock(ItemRepository.class);
  }

  @Test
  void movePersona_diagonal() {
    PersonaEntity persona = PersonaEntity.builder().persona(newPersona()).itemRepository(itemRepository).build();
    persona.setPosition(new Position(1, 1, 1));
    persona.setNextPosition(new Position(11, 11, 1));

    // When
    realTimeEngine.movePersona(0.033f, persona);

    // Then
    Assertions.assertEquals(1.0777818f, persona.getPosition().getX());
    Assertions.assertEquals(1.0777818f, persona.getPosition().getY());
    Assertions.assertEquals(1, persona.getPosition().getLayer());
  }


  @Test
  void movePersona_direct() {
    PersonaEntity persona = PersonaEntity.builder().persona(newPersona()).itemRepository(itemRepository).build();
    persona.setPosition(new Position(1, 1, 1));
    persona.setNextPosition(new Position(1, 11, 1));

    // When
    realTimeEngine.movePersona(0.033f, persona);

    // Then
    Assertions.assertEquals(1f, persona.getPosition().getX());
    Assertions.assertEquals(1.11f, persona.getPosition().getY());
    Assertions.assertEquals(1, persona.getPosition().getLayer());
  }

  @Test
  void movePersona_multiple_direct_reached() {
    PersonaEntity persona = PersonaEntity.builder().persona(newPersona()).itemRepository(itemRepository).build();
    persona.setPosition(new Position(1, 1, 1));
    persona.setNextPosition(new Position(1, 11, 1));

    // When
    for (int i = 0; i < 122; i++) {
      realTimeEngine.movePersona(0.033f, persona);
    }

    // Then
    Assertions.assertEquals(1f, persona.getPosition().getX());
    Assertions.assertEquals(11f, persona.getPosition().getY());
    Assertions.assertEquals(1, persona.getPosition().getLayer());
  }

  @Test
  void movePersona_multipleSmallStep_equivalent() {
    PersonaEntity persona = PersonaEntity.builder().persona(newPersona()).itemRepository(itemRepository).build();
    persona.setPosition(new Position(1, 1, 1));
    persona.setNextPosition(new Position(1, 11, 1));

    // When
    realTimeEngine.movePersona(0.033f, persona);

    // Then
    Assertions.assertEquals(1f, persona.getPosition().getX());
    Assertions.assertEquals(1.11f, persona.getPosition().getY());
    Assertions.assertEquals(1, persona.getPosition().getLayer());
  }

  @Test
  void movePersona_targetPosition_equivalent() {
    PersonaEntity persona = PersonaEntity.builder().persona(newPersona()).itemRepository(itemRepository).build();
    persona.setPosition(new Position(1, 1, 1));
    persona.setTargetPosition(new Position(1, 11, 1));
    personaEntities.add(persona);

    // When
    for (int i = 0; i < 122; i++) {
      realTimeEngine.update(0.033f);
    }

    // Then
    Assertions.assertEquals(1f, persona.getPosition().getX());
    Assertions.assertEquals(11.0f, persona.getPosition().getY());
    Assertions.assertEquals(1, persona.getPosition().getLayer());
  }

  private Persona newPersona() {
    return Persona.builder().id(UUID.randomUUID()).speed(10).position(new Position()).size(new Size(1, 1)).build();
  }
}