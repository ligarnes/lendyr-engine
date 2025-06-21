package net.alteiar.lendyr.model.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "itemType", visible = true, defaultImpl = GenericItem.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Weapon.class, name = Item.WEAPON),
    @JsonSubTypes.Type(value = Armor.class, name = Item.ARMOR),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.BELT),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.PANTS),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.BOOTS),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.CLOAK),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.NECKLESS),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.GLOVES),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.RING),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.FOOD),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.MAGIC_ITEMS),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.AMMO),
    @JsonSubTypes.Type(value = GenericItem.class, name = Item.OTHER)
})
public interface Item {
  String WEAPON = "WEAPON";
  String ARMOR = "ARMOR";
  String BELT = "BELT";
  String PANTS = "PANTS";
  String BOOTS = "BOOTS";
  String CLOAK = "CLOAK";
  String NECKLESS = "NECKLESS";
  String GLOVES = "GLOVES";
  String RING = "RING";
  String FOOD = "FOOD";
  String MAGIC_ITEMS = "MAGIC_ITEMS";
  String AMMO = "AMMO";
  String OTHER = "OTHER";


  UUID getId();

  String getItemType();
}
