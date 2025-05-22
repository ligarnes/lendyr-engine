package net.alteiar.lendyr.model.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "itemType", visible = true, defaultImpl = GenericItem.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Weapon.class, name = "WEAPON"),
    @JsonSubTypes.Type(value = Armor.class, name = "ARMOR"),
    @JsonSubTypes.Type(value = GenericItem.class, name = "OTHER")
})
public interface Item {
  UUID getId();
}
