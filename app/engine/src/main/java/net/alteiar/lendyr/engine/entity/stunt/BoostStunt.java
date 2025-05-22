package net.alteiar.lendyr.engine.entity.stunt;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.entity.status.StatusFactory;

@Log4j2
@Builder
public class BoostStunt implements Stunt {
  private int amount;

  @Override
  public StuntEffect execute() {
    log.info("Boost {}", amount);
    return StuntEffect.builder()
        .newStatus(StatusFactory.boost(amount))
        .build();
  }
}
