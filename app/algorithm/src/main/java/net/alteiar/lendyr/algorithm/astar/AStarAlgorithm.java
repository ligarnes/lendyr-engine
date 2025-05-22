package net.alteiar.lendyr.algorithm.astar;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Objects;

public class AStarAlgorithm<N extends Network<E>, E extends Node<E>> {

  @Getter
  private final N network;
  @Getter
  private final ArrayList<E> path;

  private final ArrayList<E> openList;
  private final ArrayList<E> closedList;

  public AStarAlgorithm(N network) {
    this.network = network;

    this.path = new ArrayList<>();
    this.openList = new ArrayList<>();
    this.closedList = new ArrayList<>();
  }

  public void solve(E start, E end) {
    reset();

    if (start == null && end == null) {
      return;
    }

    if (Objects.equals(start, end)) {
      return;
    }

    this.openList.add(start);

    while (!openList.isEmpty()) {
      E current = getLowestF();

      if (current.equals(end)) {
        retracePath(current);
        break;
      }

      openList.remove(current);
      closedList.add(current);

      for (E n : current.getNeighbours()) {
        if (closedList.contains(n) || !n.isValid()) {
          continue;
        }

        double tempScore = current.getCost() + current.distanceTo(n);

        if (openList.contains(n)) {
          if (tempScore < n.getCost()) {
            n.setCost(tempScore);
            n.setParent(current);
          }
        } else {
          n.setCost(tempScore);
          openList.add(n);
          n.setParent(current);
        }

        n.setHeuristic(n.heuristic(end));
        n.setFunction(n.getCost() + n.getHeuristic());
      }
    }
  }

  public void reset() {
    this.path.clear();
    this.openList.clear();
    this.closedList.clear();
  }

  private void retracePath(E current) {
    E temp = current;
    this.path.add(current);

    while (temp.getParent() != null) {
      this.path.add(temp.getParent());
      temp = temp.getParent();
    }
  }

  private E getLowestF() {
    E lowest = openList.getFirst();
    for (E n : openList) {
      if (n.getFunction() < lowest.getFunction()) {
        lowest = n;
      }
    }
    return lowest;
  }
}
