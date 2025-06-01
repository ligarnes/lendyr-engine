package net.alteiar.lendyr.model.map.tiled.object;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiledPolyline {
  private String points;

  public List<Vector2> getPolyline() {
    float[] pointsStr = PropertyUtils.readPoints(points);
    List<Vector2> points = new ArrayList<>(pointsStr.length - 1);
    for (int i = 0; i < pointsStr.length - 1; i++) {
      points.add(new Vector2(pointsStr[i], pointsStr[i + 1]));
    }

    return points;
  }
}
