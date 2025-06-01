package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
  private UUID id;
  private String name;
  private int speed;
  private int defense;

  private String portraitPath;
  private String tokenPath;
  private Position position;
  private Size size;

  private int armorRating;
  private int armorPenalty;

  private int healthPoint;
  private int currentHealthPoint;
  private Abilities abilities;

  private PersonaEquipped equipped;
  private Inventory inventory;
}
