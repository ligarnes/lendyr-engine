package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
  int gold;
  int silver;
  int copper;
  List<PersonaItem> backpack;

  public void removeItem(UUID itemId) {
    backpack.removeIf(p -> p.getItemId().equals(itemId));
  }

  public void addToBackpack(PersonaItem backpack) {
    this.backpack.add(backpack);
  }
}
