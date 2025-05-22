package net.alteiar.lendyr.algorithm.astar;

public abstract class Network<N extends Node> {
  public abstract Iterable<N> getNodes();
}
