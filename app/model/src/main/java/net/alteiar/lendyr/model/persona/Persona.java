package net.alteiar.lendyr.model.persona;

import com.badlogic.gdx.math.Vector2;
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
  private Vector2 position;
  private Size size;

  private int armorRating;
  private int armorPenalty;

  private int healthPoint;
  private int currentHealthPoint;
  private Abilities abilities;

  private PersonaEquipped equipped;
  private Inventory inventory;
}
