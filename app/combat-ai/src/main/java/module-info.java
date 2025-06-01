module lendyr.combat.ai {
  exports net.alteiar.lendyr.ai.combat;

  requires lendyr.game.entities;
  requires lendyr.model;
  requires static lombok;
  requires gdx;
  requires com.fasterxml.jackson.dataformat.xml;
  requires com.fasterxml.jackson.databind;
  requires lendyr.game.persistence;

  requires org.apache.logging.log4j;
  requires lendyr.game.algorithm;
}