package net.alteiar.lendyr.engine.entity.stunt;

import lombok.Builder;
import lombok.Value;
import net.alteiar.lendyr.engine.entity.status.StatusEntity;

@Value
@Builder
public class StuntEffect {
  StatusEntity newStatus;
  StatusEntity targetStatus;
  int bonusDiceDamage;
}
