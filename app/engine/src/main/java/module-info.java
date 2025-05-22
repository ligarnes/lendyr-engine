module lendyr.game.engine {
  requires static lombok;

  requires com.fasterxml.jackson.databind;
  requires org.apache.logging.log4j;
  requires io.grpc;

  requires lendyr.model;
  requires gdx;
  requires lendyr.game.persistence;
  requires java.desktop;

  exports net.alteiar.lendyr.engine;
  exports net.alteiar.lendyr.engine.entity;
  exports net.alteiar.lendyr.engine.entity.exception;
  exports net.alteiar.lendyr.engine.entity.status;
  exports net.alteiar.lendyr.engine.entity.stunt;
  exports net.alteiar.lendyr.engine.action;
  exports net.alteiar.lendyr.engine.action.result;
  exports net.alteiar.lendyr.engine.random;
  exports net.alteiar.lendyr.engine.action.minor;
  exports net.alteiar.lendyr.engine.action.major;
}