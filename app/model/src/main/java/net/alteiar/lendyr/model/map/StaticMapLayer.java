package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.map.element.MapElement;

import java.util.List;
import java.util.Optional;

@Log4j2
@Value
public class StaticMapLayer {
  float width;
  float height;
  Shape2D shape;
  List<MapElement> mapElements;
  List<MapElement> activations;

  public StaticMapLayer(float width, float height, Shape2D shape, List<MapElement> mapElements, List<MapElement> activations) {
    this.width = width;
    this.height = height;
    this.shape = shape;
    this.mapElements = mapElements;
    this.activations = activations;
  }

  public Rectangle getBounds() {
    return switch (shape) {
      case Rectangle rect -> rect;
      case Polygon poly -> poly.getBoundingRectangle();
      default -> new Rectangle(0, 0, width, height);
    };
  }

  public Optional<MapElement> checkActivation(Rectangle rect) {
    return activations.stream().filter(mapElement -> mapElement.checkCollision(rect)).findFirst();
  }

  public boolean checkCollision(Rectangle rect) {
    if (!isInLayer(rect)) {
      return true;
    }
    return checkCollisionImpl(rect);
  }

  private boolean checkCollisionImpl(Rectangle rectangle) {
    return mapElements.stream().anyMatch(mapElement -> mapElement.checkCollision(rectangle));
  }

  public boolean isInLayer(float x, float y) {
    return shape.contains(x, y);
  }

  public boolean isInLayer(Rectangle rect) {
    return isInLayer(rect.x + 0.01f, rect.y + 0.1f)
        && isInLayer(rect.x + 0.01f, rect.y + rect.height - 0.01f)
        && isInLayer(rect.x + rect.width - 0.01f, rect.y + rect.height - 0.01f)
        && isInLayer(rect.x + rect.width - 0.01f, rect.y + 0.01f);
  }
}
