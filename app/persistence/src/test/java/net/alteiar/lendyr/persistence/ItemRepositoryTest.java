package net.alteiar.lendyr.persistence;

import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.items.Weapon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

class ItemRepositoryTest {

  static ItemRepository itemRepository;

  @BeforeAll
  static void beforeEach() {
    ClassLoader classLoader = RepositoryFactoryTest.class.getClassLoader();
    File root = new File(classLoader.getResource("root.txt").getFile()).getParentFile();
    itemRepository = RepositoryFactory.initialize(root).getItemRepository();
  }

  @Test
  void findAll() {

    // When
    Collection<Item> items = itemRepository.findAll();

    // Then
    Assertions.assertEquals(4, items.size());
  }

  @Test
  void verifyIds() {
    Assertions.assertTrue(itemRepository.findById(UUID.fromString("6b8d8149-a13d-4935-8c52-a704566fec49")).isPresent());
    Assertions.assertTrue(itemRepository.findById(UUID.fromString("79370ee4-049f-4681-95e1-256fe11aa8c6")).isPresent());
    Assertions.assertTrue(itemRepository.findById(UUID.fromString("ad3bdf17-5356-414c-96f4-c2fb8d5e0ffe")).isPresent());
    Assertions.assertTrue(itemRepository.findById(UUID.fromString("484889ca-df2e-4a78-b5e1-fbe2eea8a796")).isPresent());
  }

  @Test
  void findById() {
    UUID itemId = UUID.fromString("484889ca-df2e-4a78-b5e1-fbe2eea8a796");

    // When
    Optional<Item> items = itemRepository.findById(itemId);

    // Then
    Assertions.assertTrue(items.isPresent());
    Assertions.assertEquals(itemId, items.get().getId());
    Assertions.assertInstanceOf(Weapon.class, items.get());
  }

  @Test
  void findWeaponById() {
    UUID itemId = UUID.fromString("484889ca-df2e-4a78-b5e1-fbe2eea8a796");

    // When
    Optional<Weapon> items = itemRepository.findWeaponById(itemId);

    // Then
    Assertions.assertTrue(items.isPresent());
    Assertions.assertEquals(itemId, items.get().getId());
    Assertions.assertEquals(1, items.get().getDamageDice());
    Assertions.assertEquals(16f, items.get().getLongRange());
  }
}