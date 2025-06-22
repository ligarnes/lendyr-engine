package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrShape;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrStaticMapElement;
import net.alteiar.lendyr.model.map.layered.element.PolygonMapElement;
import net.alteiar.lendyr.model.map.layered.element.RectangleMapElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GeometryMapperTest {

  @Test
  void mapElementRectangleToDto() {
    RectangleMapElement rectangle = new RectangleMapElement("someName", new Rectangle(1, 2, 3, 4));

    // When
    LendyrStaticMapElement dto = GeometryMapper.INSTANCE.mapElementToDto(rectangle);

    // Then
    Assertions.assertEquals(GeometryMapper.INSTANCE.rectangleToDto(rectangle), dto);
  }

  @Test
  void mapElementPolygonToDto() {
    PolygonMapElement polygonMapElement = new PolygonMapElement("someName", new Polygon(new float[]{1, 2, 3, 4, 5, 6, 7, 8}));
    // When
    LendyrStaticMapElement dto = GeometryMapper.INSTANCE.mapElementToDto(polygonMapElement);

    // Then
    Assertions.assertEquals(GeometryMapper.INSTANCE.polygonToDto(polygonMapElement), dto);
  }

  @Test
  void rectangleToDto() {
    RectangleMapElement rectangle = new RectangleMapElement("someName", new Rectangle(1, 2, 3, 4));

    // When
    LendyrStaticMapElement dto = GeometryMapper.INSTANCE.rectangleToDto(rectangle);

    // Then
    Assertions.assertEquals(rectangle.getName(), dto.getName());
    Assertions.assertEquals(GeometryMapper.INSTANCE.shapeRectangleToDto(rectangle.getRectangle()), dto.getShape());
  }

  @Test
  void polygonToDto() {
    PolygonMapElement polygonMapElement = new PolygonMapElement("someName", new Polygon(new float[]{1, 2, 3, 4, 5, 6, 7, 8}));

    // When
    LendyrStaticMapElement dto = GeometryMapper.INSTANCE.polygonToDto(polygonMapElement);

    // Then
    Assertions.assertEquals(polygonMapElement.getName(), dto.getName());
    Assertions.assertEquals(GeometryMapper.INSTANCE.shapePolygonToDto(polygonMapElement.getPolygon()), dto.getShape());
  }

  @Test
  void shapeRectangleToDto() {
    Rectangle rectangle = new Rectangle(1, 2, 3, 4);

    // When
    LendyrShape dto = GeometryMapper.INSTANCE.shapeRectangleToDto(rectangle);

    // Then
    Assertions.assertEquals(rectangle.getX(), dto.getRectangle().getX());
    Assertions.assertEquals(rectangle.getY(), dto.getRectangle().getY());
    Assertions.assertEquals(rectangle.getWidth(), dto.getRectangle().getWidth());
    Assertions.assertEquals(rectangle.getHeight(), dto.getRectangle().getHeight());
  }

  @Test
  void shapePolygonToDto() {
    Polygon polygon = new Polygon(new float[]{1, 2, 3, 4, 5, 6, 7, 8});
    polygon.setPosition(4, 4);

    // When
    LendyrShape dto = GeometryMapper.INSTANCE.shapePolygonToDto(polygon);

    // Then
    Assertions.assertEquals(polygon.getTransformedVertices().length, dto.getPolygon().getVerticesCount());
    for (int i = 0; i < polygon.getTransformedVertices().length; i++) {
      Assertions.assertEquals(polygon.getTransformedVertices()[i], dto.getPolygon().getVertices(i));
      Assertions.assertNotEquals(polygon.getVertices()[i], dto.getPolygon().getVertices(i));
    }
  }
}