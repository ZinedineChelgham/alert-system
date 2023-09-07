package java.com.zinedine.alertsystem.model;

import com.google.common.collect.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zinedine.alertsystem.model.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomDimBoundaryStepTest {

  private CustomDimBoundaryStep boundaryStep;
  private BoundaryStepCrossInfo boundaryStepCrossInfo;



  @BeforeEach
  void setUp() {
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10);
    Range<Float> customDim = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 20);
    int maxCrossLimit = 1;
    BoundaryStep.SeverityType severity = BoundaryStep.SeverityType.HIGH;
    boundaryStepCrossInfo = new BoundaryStepCrossInfo(1);
    boundaryStep = new CustomDimBoundaryStep(range, maxCrossLimit, severity, customDim, boundaryStepCrossInfo);
  }

  @Test
  void shouldAlertIfChosen() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    boundaryStep.setChosenForAlert(true);
    assertTrue(boundaryStep.shouldAlert());
  }

  @Test
  void shouldNotAlertIfNotChosen() {
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertFalse(boundaryStep.shouldAlert());
  }

  @Test
  void shouldNotAlertEvenIfChosen() {
    boundaryStep.setChosenForAlert(true);
    assertFalse(boundaryStep.shouldAlert());
  }


  @Test
  void shouldUpdatedSharedCrossInfosForTwoCustomDimBoundary() {
    CustomDimBoundaryStep boundaryStep2 = new CustomDimBoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 50),
      1,
      BoundaryStep.SeverityType.HIGH,
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 50),
      boundaryStepCrossInfo
    );
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertEquals(0, boundaryStep2.getBoundaryStepCrossInfo().getCrossCounter());
    boundaryStep.getBoundaryStepCrossInfo().incrementCrossCounter();
    assertEquals(1, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertEquals(1, boundaryStep2.getBoundaryStepCrossInfo().getCrossCounter());

  }

}
