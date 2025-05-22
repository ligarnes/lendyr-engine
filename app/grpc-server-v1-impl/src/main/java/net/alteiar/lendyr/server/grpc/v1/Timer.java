package net.alteiar.lendyr.server.grpc.v1;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class Timer {

  public static void time(String name, Runnable runnable) {
    Timer timer = Timer.builder().build();
    timer.startTimer();
    runnable.run();
    timer.stopTimer();
    log.info("{} took {} ms", name, timer.getElapsedTime());
  }

  private long startTime;
  private long endTime;

  public void startTimer() {
    this.startTime = System.currentTimeMillis();
  }

  public void stopTimer() {
    this.endTime = System.currentTimeMillis();
  }

  public long getElapsedTime() {
    return this.endTime - this.startTime;
  }
}
