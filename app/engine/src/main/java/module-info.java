module lendyr.game.engine {
  requires static lombok;

  requires com.fasterxml.jackson.databind;
  requires org.apache.logging.log4j;
  requires io.grpc;

  requires lendyr.model;
  requires gdx;
  requires lendyr.game.persistence;
  requires java.desktop;
  requires lendyr.game.entities;
  requires lendyr.combat.ai;

  exports net.alteiar.lendyr.engine;
  exports net.alteiar.lendyr.engine.random;
}