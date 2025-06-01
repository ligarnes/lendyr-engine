module lendyr.game.persistence {
  exports net.alteiar.lendyr.persistence;
  exports net.alteiar.lendyr.persistence.dao;

  requires static lombok;

  requires lendyr.model;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.annotation;
  requires gdx;
  requires com.fasterxml.jackson.dataformat.xml;
}