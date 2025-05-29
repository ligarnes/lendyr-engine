package net.alteiar.lendyr.ai.combat.geometry;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class GeometryUtilsTest {

  @ParameterizedTest
  @MethodSource("provideVectorsAt1Range")
  void findClosestAt1Range(Vector2 source, Vector2 target, Vector2 expected) {
    // Given
    float maxDistance = 8f;

    // When
    Vector2 dest = GeometryUtils.findClosestAt1Range(source, target, maxDistance);

    // Then
    Assertions.assertEquals(expected, dest);
  }

  private static Stream<Arguments> provideVectorsAt1Range() {
    return Stream.of(
        Arguments.of(new Vector2(0, 0), new Vector2(0, 10), new Vector2(0, 8)),
        Arguments.of(new Vector2(0, 0), new Vector2(10, 0), new Vector2(8, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(0, 5), new Vector2(0, 4)),
        Arguments.of(new Vector2(0, 0), new Vector2(5, 0), new Vector2(4, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(5, 5), new Vector2(4, 5)),
        Arguments.of(new Vector2(0, 5), new Vector2(5, 5), new Vector2(4, 5)),
        Arguments.of(new Vector2(0, 3), new Vector2(5, 5), new Vector2(4, 5))
    );
  }

  @ParameterizedTest
  @MethodSource("provideVectorsAtRange")
  void findClosestAtRange(Vector2 source, Vector2 target, Vector2 expected) {
    // Given
    float maxDistance = 8f;
    float range = 12f;

    // When
    Vector2 dest = GeometryUtils.findClosestAtRange(source, target, maxDistance, range);

    // Then
    Assertions.assertEquals(expected, dest);
  }


  private static Stream<Arguments> provideVectorsAtRange() {
    return Stream.of(
        Arguments.of(new Vector2(0, 0), new Vector2(0, 25), new Vector2(0, 8)),
        Arguments.of(new Vector2(0, 0), new Vector2(25, 0), new Vector2(8, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(0, 14), new Vector2(0, 2)),
        Arguments.of(new Vector2(0, 0), new Vector2(15, 0), new Vector2(3, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(15, 15), new Vector2(5.75f, 5.75f))
    );
  }

  @ParameterizedTest
  @MethodSource("provideVectorsAtRangeFarthest")
  void findFarthestAtRange(Vector2 source, Vector2 target, Vector2 expected) {
    // Given
    float maxDistance = 5f;
    float range = 10f;

    // When
    Vector2 dest = GeometryUtils.findFarthestAtRange(source, target, maxDistance, range);

    // Then
    Assertions.assertEquals(expected, dest);
  }

  private static Stream<Arguments> provideVectorsAtRangeFarthest() {
    return Stream.of(
        Arguments.of(new Vector2(0, 0), new Vector2(0, 6), new Vector2(0, -4)),
        Arguments.of(new Vector2(0, 0), new Vector2(10, 0), new Vector2(0, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(0, 1), new Vector2(0, -5)),
        Arguments.of(new Vector2(0, 0), new Vector2(15, 0), new Vector2(5, 0)),
        Arguments.of(new Vector2(0, 0), new Vector2(15, 15), new Vector2(3.5f, 3.5f))
    );
  }
}