module lendyr.service {
  requires static lombok;

  requires com.fasterxml.jackson.databind;
  requires org.apache.logging.log4j;
  requires io.grpc;

  requires lendyr.model;
  requires lendyr.grpc.api;
  requires lendyr.grpc.server.v1;
  requires lendyr.game.engine;
  requires io.grpc.stub;
  requires com.google.protobuf;
  requires org.mapstruct;
  requires lendyr.game.persistence;
}