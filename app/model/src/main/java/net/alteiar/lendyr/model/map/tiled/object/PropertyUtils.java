package net.alteiar.lendyr.model.map.tiled.object;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertyUtils {

  public static float[] readPoints(String points) {
    String[] pointsStr = points.split(" ");
    float[] values = new float[pointsStr.length * 2];
    for (int i = 0; i < pointsStr.length; i++) {
      String[] localValue = pointsStr[i].split(",");
      values[i * 2] = Float.parseFloat(localValue[0]);
      values[i * 2 + 1] = Float.parseFloat(localValue[1]);
    }
    return values;
  }
}
