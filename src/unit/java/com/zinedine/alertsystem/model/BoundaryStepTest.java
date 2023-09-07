package java.com.zinedine.alertsystem.model;

import com.google.common.collect.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zinedine.alertsystem.model.*;

import static org.junit.jupiter.api.Assertions.*;

class BoundaryStepTest {
  private BoundaryStep boundaryStep;

  @BeforeEach
  void setUp() {
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10);
    int maxCrossLimit = 3;
    BoundaryStep.SeverityType severity = BoundaryStep.SeverityType.HIGH;
    boundaryStep = new BoundaryStep(range, maxCrossLimit, severity);
  }

  @Test
  void shouldIncrementConsecutiveCrossedCount() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertEquals(1, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
  }

  @Test
  void shouldDecrementConsecutiveCrossedCount() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.getBoundaryStepCrossInfo().decrementCrossCounter();
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
  }

  @Test
  void shouldReturnFalseForIsCrossLimitReached() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertFalse(boundaryStep.getBoundaryStepCrossInfo().isCrossLimitReached());
  }

  @Test
  void shouldReturnTrueForIsCrossLimitReached() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertTrue(boundaryStep.getBoundaryStepCrossInfo().isCrossLimitReached());
  }

  @Test
  void shouldNotAlert() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertFalse(boundaryStep.shouldAlert());
  }

  @Test
  void shouldAlert() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertTrue(boundaryStep.shouldAlert());
  }

  @Test
  void shouldReturnToNormal() {
    boundaryStep.getBoundaryStepCrossInfo().incrementReturnToNormalCounter();
    assertEquals(1, boundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
    boundaryStep.getBoundaryStepCrossInfo().returnToNormal();
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
  }


}
