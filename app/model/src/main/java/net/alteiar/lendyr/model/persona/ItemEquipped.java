package net.alteiar.lendyr.model.persona;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Value
@Builder
public class ItemEquipped {
  UUID armor;
  UUID belt;
  UUID gloves;
  UUID shoes;
  UUID neckless;
  UUID ring1;
  UUID leftHand;
  UUID rightHand;
  UUID twoHanded;

  public List<UUID> getItems() {
    return Stream.of(armor, belt, gloves, shoes, neckless, ring1, leftHand, rightHand, twoHanded).filter(Objects::nonNull).toList();
  }
}
