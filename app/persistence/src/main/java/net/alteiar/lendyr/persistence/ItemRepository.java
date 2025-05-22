package net.alteiar.lendyr.persistence;

import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.items.Weapon;
import net.alteiar.lendyr.persistence.dao.ItemListDao;

import java.util.*;

/**
 * Repository to retrieve the items in the game.
 */
public class ItemRepository {
  private final Map<UUID, Item> items;

  ItemRepository(JsonMapper jsonMapper) {
    ItemListDao dao = jsonMapper.load("./items/data-items.json", ItemListDao.class);
    items = new HashMap<>(dao.getItems().size());
    dao.getItems().forEach(item -> items.put(item.getId(), item));
  }

  public Collection<Item> findAll() {
    return items.values();
  }

  public Optional<Item> findById(UUID id) {
    return Optional.ofNullable(items.get(id));
  }

  public Optional<Weapon> findWeaponById(UUID id) {
    return findById(id).filter(i -> i instanceof Weapon).map(Weapon.class::cast);
  }
}
