module lendyr.model {
  requires static lombok;
  requires com.fasterxml.jackson.annotation;
  requires java.desktop;
  requires gdx;
  requires org.apache.logging.log4j;
  requires com.fasterxml.jackson.dataformat.xml;
  requires com.fasterxml.jackson.databind;

  exports net.alteiar.lendyr.model;
  exports net.alteiar.lendyr.model.encounter;
  exports net.alteiar.lendyr.model.persona;
  exports net.alteiar.lendyr.model.items;
  exports net.alteiar.lendyr.model.map;
  exports net.alteiar.lendyr.model.map.layered;
  exports net.alteiar.lendyr.model.map.layered.element;
  exports net.alteiar.lendyr.model.map.tiled;
  exports net.alteiar.lendyr.model.map.tiled.object;
}