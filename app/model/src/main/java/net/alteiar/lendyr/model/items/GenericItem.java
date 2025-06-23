package net.alteiar.lendyr.model.items;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericItem implements Item {
  private UUID id;
  private String itemType;
  private String name;
  private String description;
  private String icon;
  private int cost;
  private int encumbrance;

  private ItemRarity rarity;
  private QuestReference quest;

  // Defensive property
  private int defenseBonus;
}
