package java.com.zinedine.alertsystem.model;

import com.google.common.collect.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zinedine.alertsystem.model.*;

import static org.junit.jupiter.api.Assertions.*;

class RangeFactoryTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void shouldCreateBoundedRange() {
    int min = 0; int max = 10;
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.OPEN, min, max);
    assertEquals(min, range.lowerEndpoint());
    assertEquals(max, range.upperEndpoint());
  }

  @Test
  void shouldCreateSimpleGraterThanRange() {
    int threshold = 10;
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, threshold);
    assertEquals(threshold, range.lowerEndpoint());
    assertTrue(range.hasLowerBound());
  }

  @Test
  void shouldCreateSimpleLessThanRange() {
    int threshold = 10;
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.LESS_THAN, threshold);
    assertEquals(threshold, range.upperEndpoint());
    assertTrue(range.hasUpperBound());
  }
}
