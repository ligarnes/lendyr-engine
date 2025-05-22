package net.alteiar.lendyr.client.grpc;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.grpc.model.v1.game.CreatedResponse;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrCharacterServiceGrpc;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGetById;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrPersona;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

@Log4j2
public class CharacterClient {


  private final LendyrCharacterServiceGrpc.LendyrCharacterServiceBlockingStub blockingStub;

  /**
   * Construct client for accessing HelloWorld server using the existing channel.
   */
  public CharacterClient(Channel channel) {
    blockingStub = LendyrCharacterServiceGrpc.newBlockingStub(channel);
  }

  public UUID create(LendyrPersona character) {
    try {
      CreatedResponse response = blockingStub.create(character);
      return convertBytesToUUID(response.getId());
    } catch (StatusRuntimeException ex) {
      log.warn("RPC failed: {}", ex.getStatus(), ex);
      throw new IllegalStateException(ex);
    }
  }

  public LendyrPersona getCharacter(UUID id) {
    ByteString bytes = convertUUIDToBytes(id);
    return blockingStub.get(LendyrGetById.newBuilder().setId(bytes).build());
  }

  ByteString convertUUIDToBytes(UUID uuid) {
    if (Objects.isNull(uuid)) {
      return null;
    }

    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return ByteString.copyFrom(bb.array());
  }

  UUID convertBytesToUUID(ByteString bytes) {
    if (Objects.isNull(bytes) || bytes.isEmpty()) {
      return null;
    }
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes.toByteArray());
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();
    return new UUID(high, low);
  }
}
