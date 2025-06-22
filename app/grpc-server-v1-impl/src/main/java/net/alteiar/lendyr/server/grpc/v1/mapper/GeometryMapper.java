package net.alteiar.lendyr.server.grpc.v1.mapper;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrShape;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrShapePolygon;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrShapeRectangle;
import net.alteiar.lendyr.grpc.model.v1.map.LendyrStaticMapElement;
import net.alteiar.lendyr.model.map.layered.element.MapElement;
import net.alteiar.lendyr.model.map.layered.element.PolygonMapElement;
import net.alteiar.lendyr.model.map.layered.element.RectangleMapElement;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GeometryMapper {
  GeometryMapper INSTANCE = Mappers.getMapper(GeometryMapper.class);

  List<LendyrStaticMapElement> mapElementListToDto(List<MapElement> mapElements);

  default LendyrStaticMapElement mapElementToDto(MapElement mapElement) {
    return switch (mapElement) {
      case PolygonMapElement polygonMapElement -> polygonToDto(polygonMapElement);
      case RectangleMapElement rectangleMapElement -> rectangleToDto(rectangleMapElement);
      default -> null;
    };
  }

  @Mapping(source = "rectangle", target = "shape")
  LendyrStaticMapElement rectangleToDto(RectangleMapElement rectangle);

  @Mapping(source = "polygon", target = "shape")
  LendyrStaticMapElement polygonToDto(PolygonMapElement polygon);

  default LendyrShape shapeToDto(Shape2D shape2D) {
    return switch (shape2D) {
      case Polygon polygon -> shapePolygonToDto(polygon);
      case Rectangle rectangle -> shapeRectangleToDto(rectangle);
      default -> null;
    };
  }

  default LendyrShape shapePolygonToDto(Polygon polygon) {
    if (polygon == null) {
      return null;
    }
    LendyrShapePolygon.Builder builder = LendyrShapePolygon.newBuilder();
    for (float vertice : polygon.getTransformedVertices()) {
      builder.addVertices(vertice);
    }
    return LendyrShape.newBuilder().setPolygon(builder).build();
  }

  default LendyrShape shapeRectangleToDto(Rectangle rectangle) {
    if (rectangle == null) {
      return null;
    }
    return LendyrShape.newBuilder().setRectangle(
            LendyrShapeRectangle.newBuilder()
                .setX(rectangle.getX())
                .setY(rectangle.getY())
                .setWidth(rectangle.getWidth())
                .setHeight(rectangle.getHeight()))
        .build();
  }
}
