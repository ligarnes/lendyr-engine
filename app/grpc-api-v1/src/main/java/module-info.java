module lendyr.grpc.api {
  exports net.alteiar.lendyr.grpc.model.v1.game;
  exports net.alteiar.lendyr.grpc.model.v1.persona;
  exports net.alteiar.lendyr.grpc.model.v1.encounter;
  exports net.alteiar.lendyr.grpc.model.v1.player;
  exports net.alteiar.lendyr.grpc.model.v1.generic;
  exports net.alteiar.lendyr.grpc.model.v1.item;
  exports net.alteiar.lendyr.grpc.model.v1.map;

  requires static java.compiler;

  requires io.grpc;
  requires java.annotation;
  requires io.grpc.stub;
  requires com.google.protobuf;
  requires com.google.common;
  requires io.grpc.protobuf;
}