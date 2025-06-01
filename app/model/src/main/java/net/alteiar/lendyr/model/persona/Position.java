package net.alteiar.lendyr.model.persona;

import com.badlogic.gdx.math.Vector2;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Position {
  private float x;
  private float y;
  private int layer;

  public Position(Vector2 position, int layer) {
    this.x = position.x;
    this.y = position.y;
    this.layer = layer;
  }

  public Position cpy() {
    return new Position(x, y, layer);
  }

  public Vector2 toVector() {
    return new Vector2(x, y);
  }

  public float dst(Position target) {
    return Vector2.dst(this.x, this.y, target.x, target.y);
  }

  public String toString() {
    return "(%s,%s,%s)".formatted(this.x, this.y, this.layer);
  }
}
