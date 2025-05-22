package net.alteiar.lendyr.client;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import net.alteiar.lendyr.client.grpc.CharacterClient;

public class MainClient {

  public static void main(String[] args) {
    String target = "localhost:50051";
    ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
        .build();

    CharacterClient client = new CharacterClient(channel);

  }
}
