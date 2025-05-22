module lendyr.model {
  requires static lombok;
  requires com.fasterxml.jackson.annotation;
  requires java.desktop;
  requires gdx;

  exports net.alteiar.lendyr.model;
  exports net.alteiar.lendyr.model.encounter;
  exports net.alteiar.lendyr.model.persona;
  exports net.alteiar.lendyr.model.items;
}