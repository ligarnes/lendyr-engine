package net.alteiar.lendyr.algorithm.movement;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Stream;

public abstract class Node<T extends Node<T>> {
  @Getter
  @Setter
  private T parent;
  @Setter
  private List<T> neighbours;
  @Getter
  @Setter
  private float function;
  @Getter
  @Setter
  private boolean valid;

  protected Node(boolean valid) {
    this.valid = valid;
  }

  public Stream<T> getValidNeighbours() {
    return neighbours.stream().filter(Node::isValid);
  }

  public abstract float distanceTo(T dest);

  public abstract float heuristic(T dest);

  public abstract float fleeHeuristic(T dest);
}
