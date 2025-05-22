package net.alteiar.lendyr.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

class RepositoryFactoryTest {

  static RepositoryFactory repositoryFactory;

  @BeforeAll
  static void beforeEach() {
    ClassLoader classLoader = RepositoryFactoryTest.class.getClassLoader();
    File root = new File(classLoader.getResource("root.txt").getFile()).getParentFile();
    repositoryFactory = new RepositoryFactory(root);
  }

  @Test
  void loadAll() {
    Assertions.assertNotNull(repositoryFactory.getItemRepository());
    Assertions.assertNotNull(repositoryFactory.getMapRepository());
  }

}