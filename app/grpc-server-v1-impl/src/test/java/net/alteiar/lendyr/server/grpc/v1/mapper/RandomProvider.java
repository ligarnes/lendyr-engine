package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import net.alteiar.lendyr.model.map.Bridge;
import net.alteiar.lendyr.model.map.element.MapElement;
import net.alteiar.lendyr.model.map.element.PolygonMapElement;
import net.alteiar.lendyr.model.map.element.RectangleMapElement;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class RandomProvider {
  public static final RandomProvider INSTANCE = new RandomProvider();

  private final PodamFactory factory = new PodamFactoryImpl();

  RandomProvider() {
    factory.getStrategy().addOrReplaceTypeManufacturer(Shape2D.class, (dataProviderStrategy, attributeMetadata, manufacturingContext) -> new Rectangle(1, 2, 3, 4));
    factory.getStrategy().addOrReplaceTypeManufacturer(MapElement.class, (dataProviderStrategy, attributeMetadata, manufacturingContext) -> new RectangleMapElement("name", new Rectangle(1, 2, 3, 4)));
    factory.getStrategy().addOrReplaceTypeManufacturer(Bridge.class, (dataProviderStrategy, attributeMetadata, manufacturingContext) -> new Bridge(1, 2, new PolygonMapElement("name", new Polygon(new float[]{1, 2, 3, 4, 5, 6, 7, 8}))));
  }

  public <E> E nextObject(Class<E> clazz) {
    return factory.manufacturePojoWithFullData(clazz);
  }
}
