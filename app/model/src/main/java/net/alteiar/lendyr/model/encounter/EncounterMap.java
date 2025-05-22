package net.alteiar.lendyr.model.encounter;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncounterMap {
  private String mapPath;
  private int widthInPixel;
  private int heightInPixel;
  private int pixelPerMeter;

  private List<Rectangle> walls;
}
