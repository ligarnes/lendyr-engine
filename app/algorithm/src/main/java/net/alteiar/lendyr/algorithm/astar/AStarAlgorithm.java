package net.alteiar.lendyr.algorithm.astar;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public class AStarAlgorithm<N extends Network<E>, E extends Node<E>> {
  @Getter
  private final N network;
  private final ArrayList<E> path;

  private final ArrayList<E> openList;
  private final ArrayList<E> closedList;

  public AStarAlgorithm(N network) {
    this.network = network;

    this.path = new ArrayList<>();
    this.openList = new ArrayList<>();
    this.closedList = new ArrayList<>();
  }

  public List<E> solve(E start, E end) {
    reset();

    if (start == null && end == null) {
      return path;
    }

    if (Objects.equals(start, end)) {
      return path;
    }

    solveInternal(start, end);
    retracePath(end);
    return path.reversed();
  }

  private void solveInternal(E start, E end) {
    this.openList.add(start);

    while (!openList.isEmpty()) {
      E current = getLowestF();

      if (current.equals(end)) {
        break;
      }

      for (E next : current.getNeighbours()) {
        if (next.equals(end)) {
          next.setParent(current);
          return;
        }

        if (closedList.contains(next) || !next.isValid()) {
          continue;
        }

        double newCost = current.getCost() + current.distanceTo(next);

        if (openList.contains(next)) {
          if (newCost < next.getCost()) {
            next.setCost(newCost);
            next.setParent(current);
          }
        } else {
          next.setCost(newCost);
          openList.add(next);
          next.setParent(current);
        }

        next.setHeuristic(next.heuristic(end));
        next.setFunction(next.getCost() + next.getHeuristic());
      }

      closedList.add(current);
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
    openList.remove(lowest);
    return lowest;
  }
}
