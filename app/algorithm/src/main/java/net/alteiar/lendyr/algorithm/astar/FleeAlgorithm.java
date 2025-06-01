package net.alteiar.lendyr.algorithm.astar;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class FleeAlgorithm<E extends Node<E>> {
  @Getter
  private final ArrayList<E> path;

  private final PriorityQueue<E> frontier;
  private final Map<E, E> cameFrom;
  private final Map<E, Float> costSoFar;

  public FleeAlgorithm() {
    this.path = new ArrayList<>();
    this.frontier = new PriorityQueue<>((Comparator<Node<E>>) (o1, o2) -> (int) Math.ceil(o1.getFunction() - o2.getFunction()));
    this.cameFrom = new HashMap<>();
    this.costSoFar = new HashMap<>();
  }

  public List<E> solve(E start, E danger, float maxDistance) {
    reset();
    E end = solveInternal(start, danger, maxDistance);
    retracePath(start, end);
    return path.reversed();
  }


  private E solveInternal(E start, E danger, float maxDistance) {
    this.frontier.add(start);
    this.cameFrom.put(start, start);
    this.costSoFar.put(start, 0.0f);

    E latest = null;
    while (!frontier.isEmpty()) {
      E current = frontier.poll();
      latest = current;

      if (costSoFar.get(current) >= maxDistance) {
        return current;
      }

      current.getValidNeighbours().forEach(next -> {
        float newCost = costSoFar.get(current) + current.distanceTo(next);

        if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
          costSoFar.put(next, newCost);
          float priority = newCost + next.fleeHeuristic(danger);
          next.setFunction(priority);
          frontier.add(next);
          cameFrom.put(next, current);
        }
      });
    }
    return latest;
  }

  public void reset() {
    this.path.clear();
    this.frontier.clear();
    this.cameFrom.clear();
    this.costSoFar.clear();
  }

  private void retracePath(E start, E goal) {
    this.path.clear();
    if (!cameFrom.containsKey(goal)) {
      return;
    }
    E current = goal;
    this.path.add(goal);
    while (cameFrom.get(current) != start) {
      this.path.add(cameFrom.get(current));
      current = cameFrom.get(current);
    }
  }
}
