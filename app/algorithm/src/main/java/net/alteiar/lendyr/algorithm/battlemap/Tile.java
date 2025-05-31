package net.alteiar.lendyr.algorithm.battlemap;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.algorithm.astar.Node;

import java.util.ArrayList;

public class Tile extends Node<Tile> {
  @Getter
  private final Vector2 vector2;
  @Getter
  private boolean movableObject;

  @Builder
  public Tile(Vector2 position, boolean valid) {
    super(valid);
    this.vector2 = position;
  }

  public void reset() {
    if (this.movableObject) {
      this.setValid(true);
      this.movableObject = false;
    }
    this.setCost(0);
    this.setHeuristic(0);
    this.setFunction(0);
    this.setParent(null);
  }

  public void setMovableObject() {
    this.setValid(false);
    this.movableObject = true;
  }

  public float getX() {
    return vector2.x;
  }

  public float getY() {
    return vector2.y;
  }

  public void calculateNeighbours(GridNetwork grid) {
    ArrayList<Tile> nodes = new ArrayList<>();

    int minX = 0;
    int minY = 0;
    int maxX = grid.getWidth() - 1;
    int maxY = grid.getHeight() - 1;

    if (vector2.x > minX) {
      nodes.add(grid.find(vector2.x - 1, vector2.y)); //west
    }

    if (vector2.x < maxX) {
      nodes.add(grid.find(vector2.x + 1, vector2.y)); //east
    }

    if (vector2.y > minY) {
      nodes.add(grid.find(vector2.x, vector2.y - 1)); //north
    }

    if (vector2.y < maxY) {
      nodes.add(grid.find(vector2.x, vector2.y + 1)); //south
    }

    if (vector2.x > minX && vector2.y > minY) {
      nodes.add(grid.find(vector2.x - 1, vector2.y - 1)); //northwest
    }

    if (vector2.x < maxX && vector2.y < maxY) {
      nodes.add(grid.find(vector2.x + 1, vector2.y + 1)); //southeast
    }

    if (vector2.x < maxX && vector2.y > minY) {
      nodes.add(grid.find(vector2.x + 1, vector2.y - 1)); //northeast
    }

    if (vector2.x > minY && vector2.y < maxY) {
      nodes.add(grid.find(vector2.x - 1, vector2.y + 1)); //southwest
    }

    setNeighbours(nodes);
  }

  @Override
  public double heuristic(Tile dest) {
    return distanceTo(dest);
  }

  @Override
  public double distanceTo(Tile dest) {
    return vector2.dst2(dest.vector2);
  }

  @Override
  public String toString() {
    return "[%s, %s]".formatted(vector2, isValid() ? "_" : "x");
  }
}
