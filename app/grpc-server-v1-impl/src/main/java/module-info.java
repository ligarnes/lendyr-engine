module lendyr.grpc.server.v1 {
  requires static lombok;

  requires com.fasterxml.jackson.databind;
  requires org.apache.logging.log4j;
  requires io.grpc;

  requires lendyr.model;
  requires lendyr.game.engine;
  requires lendyr.grpc.api;
  requires lendyr.game.persistence;
  requires io.grpc.stub;
  requires org.mapstruct;
  requires com.google.protobuf;
  requires gdx;

  exports net.alteiar.lendyr.server.grpc.v1;
  exports net.alteiar.lendyr.server.grpc.v1.mapper;
}