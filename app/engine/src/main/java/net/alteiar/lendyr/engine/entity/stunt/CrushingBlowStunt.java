package net.alteiar.lendyr.engine.entity.stunt;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.entity.status.StatusFactory;

@Log4j2
@Builder
public class CrushingBlowStunt implements Stunt {
  @Override
  public StuntEffect execute() {
    log.info("Crushing blow");
    return StuntEffect.builder()
        .targetStatus(StatusFactory.crushingBlow())
        .build();
  }
}
