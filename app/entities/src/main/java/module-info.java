module lendyr.game.entities {
  requires static lombok;

  requires com.fasterxml.jackson.databind;
  requires org.apache.logging.log4j;
  requires lendyr.model;
  requires gdx;
  requires lendyr.game.persistence;
  requires com.fasterxml.jackson.dataformat.xml;

  exports net.alteiar.lendyr.entity;
  exports net.alteiar.lendyr.entity.action;
  exports net.alteiar.lendyr.entity.action.exception;
  exports net.alteiar.lendyr.entity.action.combat;
  exports net.alteiar.lendyr.entity.action.combat.major;
  exports net.alteiar.lendyr.entity.action.combat.minor;
  exports net.alteiar.lendyr.entity.map;
}