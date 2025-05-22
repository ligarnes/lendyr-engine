package net.alteiar.lendyr.engine.entity.stunt;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class LethalBlowStunt implements Stunt {

  @Override
  public StuntEffect execute() {
    log.info("Lethal blow");
    return StuntEffect.builder()
        .bonusDiceDamage(2)
        .build();
  }
}
