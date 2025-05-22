package net.alteiar.lendyr.model.persona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaEquipped {
  PersonaItem leftHand;
  PersonaItem rightHand;
  PersonaItem twoHanded;

  public PersonaItem getEquippedWeapon() {
    if (Objects.nonNull(leftHand)) {
      return leftHand;
    } else if (Objects.nonNull(rightHand)) {
      return rightHand;
    } else {
      return twoHanded;
    }
  }
}
