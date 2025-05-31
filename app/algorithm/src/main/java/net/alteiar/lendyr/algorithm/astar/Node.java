package net.alteiar.lendyr.algorithm.astar;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public abstract class Node<T extends Node<T>> {
  @Setter
  private T parent;
  @Setter
  private ArrayList<T> neighbours;
  @Setter
  private double cost, heuristic, function;
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private boolean valid;

  protected Node(boolean valid) {
    this.valid = valid;
  }

  public abstract double distanceTo(T dest);

  public abstract double heuristic(T dest);
}
