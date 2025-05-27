package net.alteiar.lendyr.app;

import java.io.File;
import java.io.IOException;

public class MainServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    /* The port on which the server should run */
    int port = 50051;

    LendyrGameServer lendyrGameServer = new LendyrGameServer(new File("./data"));
    lendyrGameServer.run(port);
//    lendyrGameServer.load("dummy-test-1");
    lendyrGameServer.blockUntilShutdown();
  }
}
