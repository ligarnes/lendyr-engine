package net.alteiar.lendyr.engine.entity.stunt;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class MightyBlowStunt implements Stunt {

  @Override
  public StuntEffect execute() {
    log.info("Mighty blow");
    return StuntEffect.builder()
        .bonusDiceDamage(1)
        .build();
  }
}
