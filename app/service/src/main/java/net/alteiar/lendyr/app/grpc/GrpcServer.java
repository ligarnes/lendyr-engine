package net.alteiar.lendyr.app.grpc;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.engine.GameContext;
import net.alteiar.lendyr.server.grpc.v1.LendyrGrpcServiceFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class GrpcServer {
  private Server server;
  private final LendyrGrpcServiceFactory serviceFactory;

  @Builder
  GrpcServer(GameContext gameContext) {
    this.serviceFactory = LendyrGrpcServiceFactory.builder().gameContext(gameContext).build();
  }

  public void start(int port) throws IOException {
    ExecutorService executor = Executors.newFixedThreadPool(20, new ThreadFactory() {
      final AtomicInteger threadNumber = new AtomicInteger(1);

      @Override
      public Thread newThread(Runnable r) {
        return Thread.ofPlatform().name(String.format("grpc-server-%s", threadNumber.getAndIncrement())).unstarted(r);
      }
    });
    ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create()).executor(executor);
    serviceFactory.getBindableServices().forEach(serverBuilder::addService);

    server = serverBuilder
        .keepAliveTime(5, TimeUnit.SECONDS)
        .keepAliveTimeout(1, TimeUnit.SECONDS)
        .permitKeepAliveTime(5, TimeUnit.SECONDS)
        .permitKeepAliveWithoutCalls(true)
        .build();
    server.start();
    log.info("Server started, listening on {}", port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Use stderr here since the logger may have been reset by its JVM shutdown hook.
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      try {
        GrpcServer.this.stop();
      } catch (InterruptedException e) {
        if (server != null) {
          server.shutdownNow();
        }
        e.printStackTrace(System.err);
      } finally {
        executor.shutdown();
      }
      System.err.println("*** server shut down");
    }));
  }

  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
