package net.alteiar.lendyr.server.grpc.v1.mapper;

import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;
import net.alteiar.lendyr.model.items.GenericItem;
import net.alteiar.lendyr.model.items.Item;
import net.alteiar.lendyr.model.items.Weapon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemMapperTest {

  @Test
  void weaponToDto() {
    Weapon item = RandomProvider.INSTANCE.nextObject(Weapon.class);

    LendyrItem dto = ItemMapper.INSTANCE.weaponToDto(item);

    Assertions.assertEquals(item.getName(), dto.getName());
    Assertions.assertEquals(item.getDescription(), dto.getDescription());
    Assertions.assertEquals(item.getIcon(), dto.getIcon());
    Assertions.assertEquals(item.getCost(), dto.getCost());
    Assertions.assertEquals(item.getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(LendyrItem.PropertiesCase.WEAPON, dto.getPropertiesCase());
    Assertions.assertEquals(item.getAbility().name(), dto.getWeapon().getAbility().name());
    Assertions.assertEquals(item.getFocus().name(), dto.getWeapon().getFocus().name());
    Assertions.assertEquals(item.getAttackType().name(), dto.getWeapon().getAttackType().name());
    Assertions.assertEquals(item.getWeaponGroup(), dto.getWeapon().getWeaponGroup());
    Assertions.assertEquals(item.getDamageDice(), dto.getWeapon().getDamageDice());
    Assertions.assertEquals(item.getDamageFixed(), dto.getWeapon().getDamageFixed());
    Assertions.assertEquals(item.getMinStr(), dto.getWeapon().getMinStr());
  }

  @Test
  void genericToDto() {
    GenericItem item = RandomProvider.INSTANCE.nextObject(GenericItem.class);

    LendyrItem dto = ItemMapper.INSTANCE.genericToDto(item);

    Assertions.assertEquals(item.getName(), dto.getName());
    Assertions.assertEquals(item.getDescription(), dto.getDescription());
    Assertions.assertEquals(item.getIcon(), dto.getIcon());
    Assertions.assertEquals(item.getCost(), dto.getCost());
    Assertions.assertEquals(item.getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(LendyrItem.PropertiesCase.PROPERTIES_NOT_SET, dto.getPropertiesCase());
  }

  @Test
  void itemToDto_generic() {
    Item item = RandomProvider.INSTANCE.nextObject(GenericItem.class);

    LendyrItem dto = ItemMapper.INSTANCE.itemToDto(item);

    Assertions.assertEquals(((GenericItem) item).getName(), dto.getName());
    Assertions.assertEquals(((GenericItem) item).getDescription(), dto.getDescription());
    Assertions.assertEquals(((GenericItem) item).getIcon(), dto.getIcon());
    Assertions.assertEquals(((GenericItem) item).getCost(), dto.getCost());
    Assertions.assertEquals(((GenericItem) item).getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(LendyrItem.PropertiesCase.PROPERTIES_NOT_SET, dto.getPropertiesCase());
  }

  @Test
  void itemToDto_weapon() {
    Item item = RandomProvider.INSTANCE.nextObject(Weapon.class);

    LendyrItem dto = ItemMapper.INSTANCE.itemToDto(item);

    Assertions.assertEquals(((Weapon) item).getName(), dto.getName());
    Assertions.assertEquals(((Weapon) item).getDescription(), dto.getDescription());
    Assertions.assertEquals(((Weapon) item).getIcon(), dto.getIcon());
    Assertions.assertEquals(((Weapon) item).getCost(), dto.getCost());
    Assertions.assertEquals(((Weapon) item).getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(LendyrItem.PropertiesCase.WEAPON, dto.getPropertiesCase());
    Assertions.assertEquals(((Weapon) item).getAbility().name(), dto.getWeapon().getAbility().name());
    Assertions.assertEquals(((Weapon) item).getFocus().name(), dto.getWeapon().getFocus().name());
    Assertions.assertEquals(((Weapon) item).getAttackType().name(), dto.getWeapon().getAttackType().name());
    Assertions.assertEquals(((Weapon) item).getWeaponGroup(), dto.getWeapon().getWeaponGroup());
    Assertions.assertEquals(((Weapon) item).getDamageDice(), dto.getWeapon().getDamageDice());
    Assertions.assertEquals(((Weapon) item).getDamageFixed(), dto.getWeapon().getDamageFixed());
    Assertions.assertEquals(((Weapon) item).getMinStr(), dto.getWeapon().getMinStr());
    Assertions.assertEquals(((Weapon) item).getDamageAbility().name(), dto.getWeapon().getDamageAbility().name());
    Assertions.assertEquals(((Weapon) item).getNormalRange(), dto.getWeapon().getNormalRange());
    Assertions.assertEquals(((Weapon) item).getLongRange(), dto.getWeapon().getLongRange());
    Assertions.assertEquals(((Weapon) item).isPenetrating(), dto.getWeapon().getPenetrating());
  }
}