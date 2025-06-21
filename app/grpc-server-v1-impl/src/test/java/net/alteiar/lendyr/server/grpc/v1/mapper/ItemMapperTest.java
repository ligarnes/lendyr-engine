package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.google.protobuf.ByteString;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;
import net.alteiar.lendyr.model.items.GenericItem;
import net.alteiar.lendyr.model.items.Weapon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemMapperTest {

  @Test
  void weaponToDto() {
    Weapon item = RandomProvider.INSTANCE.nextObject(Weapon.class);
    item.setItemType("WEAPON");

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

    Assertions.assertEquals(item.getRarity().name(), dto.getRarity().name());
    Assertions.assertEquals(item.getItemType(), dto.getItemType().name());

    // Quest
    Assertions.assertEquals(item.getQuest() != null, dto.getQuest().getIsQuest());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(item.getQuest().getQuestId()), dto.getQuest().getQuestId());
  }

  @Test
  void genericToDto() {
    GenericItem item = RandomProvider.INSTANCE.nextObject(GenericItem.class);
    item.setItemType("NECKLESS");

    LendyrItem dto = ItemMapper.INSTANCE.genericToDto(item);

    Assertions.assertEquals(item.getName(), dto.getName());
    Assertions.assertEquals(item.getDescription(), dto.getDescription());
    Assertions.assertEquals(item.getIcon(), dto.getIcon());
    Assertions.assertEquals(item.getCost(), dto.getCost());
    Assertions.assertEquals(item.getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(LendyrItem.PropertiesCase.PROPERTIES_NOT_SET, dto.getPropertiesCase());

    Assertions.assertEquals(item.getRarity().name(), dto.getRarity().name());
    Assertions.assertEquals(item.getItemType(), dto.getItemType().name());

    // Quest
    Assertions.assertEquals(item.getQuest() != null, dto.getQuest().getIsQuest());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(item.getQuest().getQuestId()), dto.getQuest().getQuestId());
  }

  @Test
  void itemToDto_generic() {
    GenericItem item = RandomProvider.INSTANCE.nextObject(GenericItem.class);
    item.setItemType("NECKLESS");

    LendyrItem dto = ItemMapper.INSTANCE.itemToDto(item);

    Assertions.assertEquals(item.getName(), dto.getName());
    Assertions.assertEquals(item.getDescription(), dto.getDescription());
    Assertions.assertEquals(item.getIcon(), dto.getIcon());
    Assertions.assertEquals(item.getCost(), dto.getCost());
    Assertions.assertEquals(item.getEncumbrance(), dto.getEncumbrance());
    Assertions.assertEquals(item.getRarity().name(), dto.getRarity().name());
    Assertions.assertEquals(item.getItemType(), dto.getItemType().name());

    // Quest
    Assertions.assertEquals(item.getQuest() != null, dto.getQuest().getIsQuest());
    Assertions.assertEquals(GenericMapper.INSTANCE.convertUUIDToBytes(item.getQuest().getQuestId()), dto.getQuest().getQuestId());

    Assertions.assertEquals(LendyrItem.PropertiesCase.PROPERTIES_NOT_SET, dto.getPropertiesCase());
  }

  @Test
  void itemToDto_weapon() {
    Weapon item = RandomProvider.INSTANCE.nextObject(Weapon.class);
    item.setItemType("WEAPON");
    item.setQuest(null);

    LendyrItem dto = ItemMapper.INSTANCE.itemToDto(item);

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
    Assertions.assertEquals(item.getDamageAbility().name(), dto.getWeapon().getDamageAbility().name());
    Assertions.assertEquals(item.getNormalRange(), dto.getWeapon().getNormalRange());
    Assertions.assertEquals(item.getLongRange(), dto.getWeapon().getLongRange());
    Assertions.assertEquals(item.isPenetrating(), dto.getWeapon().getPenetrating());

    Assertions.assertEquals(item.getRarity().name(), dto.getRarity().name());
    Assertions.assertEquals(item.getItemType(), dto.getItemType().name());

    // Quest
    Assertions.assertFalse(dto.getQuest().getIsQuest());
    Assertions.assertEquals(ByteString.empty(), dto.getQuest().getQuestId());
  }
}