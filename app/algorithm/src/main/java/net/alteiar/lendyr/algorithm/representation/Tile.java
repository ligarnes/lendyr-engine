package net.alteiar.lendyr.algorithm.representation;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.algorithm.movement.Node;
import net.alteiar.lendyr.model.persona.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Tile extends Node<Tile> {
  @Getter
  private final Vector2 vector2;

  @Getter
  private final int[] layers;

  @Builder
  public Tile(Vector2 position, int[] layers, boolean valid) {
    super(valid);
    this.vector2 = position.cpy();
    this.layers = layers;
  }

  public void reset() {
    this.setValid(true);
    this.setFunction(0);
    this.setParent(null);
  }

  public boolean isLayer(int requestLayer) {
    for (int layer : layers) {
      if (layer == requestLayer) {
        return true;
      }
    }
    return false;
  }

  public float getX() {
    return vector2.x;
  }

  public float getY() {
    return vector2.y;
  }

  public static List<Vector2> getNeighboursPosition(float x, float y) {
    return List.of(
        new Vector2(x, y + 1),
        new Vector2(x, y - 1),
        new Vector2(x + 1, y),
        new Vector2(x + 1, y + 1),
        new Vector2(x + 1, y - 1),
        new Vector2(x - 1, y),
        new Vector2(x - 1, y + 1),
        new Vector2(x - 1, y - 1)
    );
  }

  public void calculateNeighbours(MultiLayerNetwork grid) {
    List<Tile> nodes = new ArrayList<>();
    for (int layer : layers) {
      Optional.ofNullable(grid.find(vector2.x, vector2.y + 1, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x, vector2.y - 1, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x - 1, vector2.y, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x + 1, vector2.y, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x + 1, vector2.y + 1, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x + 1, vector2.y - 1, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x - 1, vector2.y + 1, layer)).ifPresent(nodes::add);
      Optional.ofNullable(grid.find(vector2.x - 1, vector2.y - 1, layer)).ifPresent(nodes::add);
    }
    setNeighbours(nodes);
  }

  @Override
  public float heuristic(Tile dest) {
    return distanceTo(dest);
  }

  @Override
  public float fleeHeuristic(Tile dest) {
    return -distanceTo(dest);
  }

  @Override
  public float distanceTo(Tile dest) {
    return vector2.dst(dest.vector2);
  }

  public Position toPosition() {
    return new Position(vector2.x, vector2.y, layers[0]);
  }

  @Override
  public String toString() {
    return "{%s, %s}".formatted(vector2, Arrays.toString(layers));
  }
}
