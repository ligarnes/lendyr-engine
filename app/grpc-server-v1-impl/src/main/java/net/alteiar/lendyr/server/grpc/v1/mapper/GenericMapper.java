package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.google.protobuf.ByteString;
import net.alteiar.lendyr.grpc.model.v1.generic.LendyrPosition;
import net.alteiar.lendyr.grpc.model.v1.generic.LendyrSize;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

@Mapper
public interface GenericMapper {
  GenericMapper INSTANCE = Mappers.getMapper(GenericMapper.class);

  default ByteString convertUUIDToBytes(UUID uuid) {
    if (Objects.isNull(uuid)) {
      return null;
    }

    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return ByteString.copyFrom(bb.array());
  }

  default UUID convertBytesToUUID(ByteString bytes) {
    if (Objects.isNull(bytes) || bytes.isEmpty()) {
      return null;
    }
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes.toByteArray());
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();
    return new UUID(high, low);
  }

  default ByteString convertByteArrayToByteString(byte[] bytes) {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return ByteString.EMPTY;
    }

    return ByteString.copyFrom(bytes);
  }

  default byte[] convertByteStringToByteArray(ByteString bytes) {
    return bytes.toByteArray();
  }

  LendyrSize convertSizeDto(Size size);

  LendyrPosition convertPositionToDto(Position position);

  Position convertPositionFromDto(LendyrPosition position);
}
