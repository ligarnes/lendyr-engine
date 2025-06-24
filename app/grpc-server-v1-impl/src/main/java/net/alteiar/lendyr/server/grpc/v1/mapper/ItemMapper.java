package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.google.protobuf.ByteString;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrQuest;
import net.alteiar.lendyr.model.items.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = GenericMapper.class,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ItemMapper {
  ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

  @Mapping(source = "weaponGroup", target = "weapon.weaponGroup")
  @Mapping(source = "damageDice", target = "weapon.damageDice")
  @Mapping(source = "damageFixed", target = "weapon.damageFixed")
  @Mapping(source = "minStr", target = "weapon.minStr")
  @Mapping(source = "ability", target = "weapon.ability")
  @Mapping(source = "focus", target = "weapon.focus")
  @Mapping(source = "attackType", target = "weapon.attackType")
  @Mapping(source = "damageAbility", target = "weapon.damageAbility")
  @Mapping(source = "penetrating", target = "weapon.penetrating")
  @Mapping(source = "normalRange", target = "weapon.normalRange")
  @Mapping(source = "longRange", target = "weapon.longRange")
  LendyrItem weaponToDto(Weapon attack);

  default LendyrItem itemToDto(Item item) {
    if (item instanceof Weapon weapon) {
      return weaponToDto(weapon);

    } else if (item instanceof GenericItem generic) {
      return genericToDto(generic);
    } else if (item instanceof Armor armor) {
      return armorToDto(armor);
    }

    return null;
  }

  default LendyrQuest convert(QuestReference questReference) {
    ByteString questId = questReference != null ? GenericMapper.INSTANCE.convertUUIDToBytes(questReference.getQuestId()) : ByteString.empty();
    return LendyrQuest.newBuilder().setIsQuest(questReference != null).setQuestId(questId).build();
  }

  LendyrItem genericToDto(GenericItem item);

  @Mapping(source = "armorRating", target = "armor.armorRating")
  @Mapping(source = "armorPenalty", target = "armor.armorPenalty")
  LendyrItem armorToDto(Armor armor);
}
