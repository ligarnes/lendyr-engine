package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaEquipped {
  private Map<EquippedLocation, PersonaItem> equippedLocations;

  public PersonaItem getEquippedWeapon() {
    PersonaItem leftHand = get(EquippedLocation.HAND_1);
    PersonaItem rightHand = get(EquippedLocation.HAND_2);

    if (leftHand != null) {
      return leftHand;
    }
    return rightHand;
  }

  public PersonaItem get(EquippedLocation location) {
    return equippedLocations.get(location);
  }

  public void equip(EquippedLocation location, PersonaItem equippedItem) {
    equippedLocations.put(location, equippedItem);
  }

  public PersonaItem removeFrom(EquippedLocation location) {
    return equippedLocations.remove(location);
  }

  public Collection<PersonaItem> toList() {
    return equippedLocations.values();
  }
}
