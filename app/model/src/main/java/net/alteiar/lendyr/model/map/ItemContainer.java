package net.alteiar.lendyr.model.map;

import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.alteiar.lendyr.model.map.layered.DynamicBlockingObject;
import net.alteiar.lendyr.model.persona.Position;
import net.alteiar.lendyr.model.persona.Size;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemContainer {
  private UUID id;
  private String name;
  private Position position;
  private Size size;
  private List<UUID> items;
  private String icon;
  private String opening;
  private String closing;

  @JsonIgnore
  private DynamicBlockingObject boundingBox;

  public DynamicBlockingObject getBoundingBox() {
    if (boundingBox == null) {
      boundingBox = new DynamicBlockingObject(new Rectangle(position.getX(), position.getY(), size.getWidth(), size.getHeight()), position.getLayer());
    }
    return boundingBox;
  }
}
