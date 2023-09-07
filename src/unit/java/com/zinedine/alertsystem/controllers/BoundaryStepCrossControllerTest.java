package java.com.zinedine.alertsystem.controllers;

import com.zinedine.alertsystem.controllers.AlertChecker;
import com.zinedine.alertsystem.controllers.BoundaryStepCrossController;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import com.zinedine.alertsystem.model.*;


class BoundaryStepCrossControllerTest {

  @Mock
  private Threshold threshold;
  @Mock
  private AlertChecker alertChecker;
  @Spy
  private BoundaryStepCrossController boundaryStepCrossController = new BoundaryStepCrossController();
  private BoundaryStep boundaryStep;
  private CustomDimBoundaryStep customDimBoundaryStep;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    boundaryStepCrossController.setThreshold(threshold);
    boundaryStepCrossController.setAlertChecker(alertChecker);
    boundaryStep = new BoundaryStep(RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 1));
    customDimBoundaryStep = new CustomDimBoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 1),
      1,
      BoundaryStep.SeverityType.CRITICAL,
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 1),
      new BoundaryStepCrossInfo(1)
    );
  }

  @Test
  void shouldCheckValueForSimpleBoundaries() throws Exception {
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(boundaryStep));
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    boundaryStepCrossController.checkValue();
    verify(boundaryStepCrossController).checkForSimpleBoundaryStep(eq(boundaryStep));
  }

  @Test
  void shouldCheckValueForCustomBoundaries() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(1f));
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    boundaryStepCrossController.checkValue();
    verify(boundaryStepCrossController).checkForCustomDimBoundaryStep();
  }

  @Test
  void shouldThrowExceptionForMixedType() {
    when(threshold.getBoundariesSteps()).thenReturn(Arrays.asList(boundaryStep, customDimBoundaryStep));
    assertThrows(Exception.class, () -> boundaryStepCrossController.checkValue());
  }

  @Test
  void shouldThrowExceptionForEmptyList() {
    when(threshold.getBoundariesSteps()).thenReturn(Collections.emptyList());
    assertThrows(Exception.class, () -> boundaryStepCrossController.checkValue());
  }

  @Test
  void shouldIncrementConsecutiveCrossedCount() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(2f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    assertEquals(1, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertFalse(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldChangeNothingDuringTheCheck() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertTrue(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldDecrementConsecutiveCrossedCount() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(2f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertTrue(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldIncrementReturnToNormalCount() throws Exception {
    boundaryStep.getBoundaryStepCrossInfo().setFixedReturnToNormal(2);
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    boundaryStep.getBoundaryStepCrossInfo().setInAlertState(true);
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    assertEquals(1, boundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
  }

  @Test
  void shouldResetReturnToNormalCount() throws Exception {
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep);
    assertEquals(0, boundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
  }

  @Test
  void shouldThrowExceptionWhenTheValueIsMissing() {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.empty());
    assertThrows(Exception.class, () -> boundaryStepCrossController.checkForSimpleBoundaryStep(boundaryStep));
  }

  ////////////////////////////

  @Test
  void shouldThrowExceptionWhenThe2ValuesAreMissing() {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.empty());
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.empty());
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    assertThrows(Exception.class, () -> boundaryStepCrossController.checkForCustomDimBoundaryStep());
  }

  @Test
  void shouldThrowExceptionWhenOneValueToCheckIsMissing() {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(1f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.empty());
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    assertThrows(Exception.class, () -> boundaryStepCrossController.checkForCustomDimBoundaryStep());
  }

  @Test
  void ShouldIncrementReturnToNormalForACustomDimBoundary() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(0f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(0f));
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    customDimBoundaryStep.getBoundaryStepCrossInfo().setFixedReturnToNormal(2);
    customDimBoundaryStep.getBoundaryStepCrossInfo().setCrossCounter(2);
    customDimBoundaryStep.getBoundaryStepCrossInfo().setInAlertState(true);
    boundaryStepCrossController.checkForCustomDimBoundaryStep();
    assertEquals(1, customDimBoundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
  }

  @Test
  void shouldReturnToNormalForACustomDimBoundary() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(0f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(0f));
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    customDimBoundaryStep.getBoundaryStepCrossInfo().setCrossCounter(2);
    customDimBoundaryStep.getBoundaryStepCrossInfo().setFixedReturnToNormal(2);
    customDimBoundaryStep.getBoundaryStepCrossInfo().setInAlertState(true);
    boundaryStepCrossController.checkForCustomDimBoundaryStep();
    assertEquals(1, customDimBoundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
    assertEquals(2, customDimBoundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    boundaryStepCrossController.checkForCustomDimBoundaryStep();
    assertEquals(0, customDimBoundaryStep.getBoundaryStepCrossInfo().getReturnToNormalCounter());
    assertEquals(0, customDimBoundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
    assertTrue(customDimBoundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldIncrementConsecutiveCrossedCountForACustomDimBoundary() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(2f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(2f));
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    boundaryStepCrossController.checkForCustomDimBoundaryStep();
    assertEquals(1, customDimBoundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
  }

  @Test
  void shouldDecrementConsecutiveCrossedCountForACustomDimBoundary() throws Exception {
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(2f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(1f));
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(customDimBoundaryStep));
    customDimBoundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    boundaryStepCrossController.checkForCustomDimBoundaryStep();
    assertEquals(0, customDimBoundaryStep.getBoundaryStepCrossInfo().getCrossCounter());
  }

  @Test
  void shouldReturnEmptyList() {
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(boundaryStep));
    assertEquals(0, boundaryStepCrossController.getBoundariesStepToAlert().size());
  }

  @Test
  void shouldReturnOneCustomDimBoundaryOutOfTwo() throws Exception {
    BoundaryStepCrossInfo sharedBoundaryStepCrossInfo = customDimBoundaryStep.getBoundaryStepCrossInfo();
    CustomDimBoundaryStep customDimBoundaryStep2 = new CustomDimBoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.LESS_THAN, 0),
      1,
      BoundaryStep.SeverityType.CRITICAL,
      RangeFactory.createRange(RangeFactory.RangeType.LESS_THAN, 0),
      sharedBoundaryStepCrossInfo
    );
    when(threshold.getBoundariesSteps()).thenReturn(Arrays.asList(customDimBoundaryStep, customDimBoundaryStep2));
    when(alertChecker.getValueToCheck(threshold.getyThresholdDataInfo())).thenReturn(Optional.of(2f));
    when(alertChecker.getValueToCheck(threshold.getxThresholdDataInfo())).thenReturn(Optional.of(2f));
    boundaryStepCrossController.checkValue();
    assertTrue(customDimBoundaryStep.getChosenForAlert());
    assertEquals(customDimBoundaryStep, boundaryStepCrossController.getBoundariesStepToAlert().get(0));
  }

  @Test
  void shouldSetTimeStampOfFirstCrossing() {
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    boundaryStepCrossController.handleTimeStampChange(boundaryStep);
    assertFalse(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldResetTimeStampOfFirstCrossing() {
    boundaryStep.getBoundaryStepCrossInfo().setTimestampOfFirstCrossing("test");
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(0);
    boundaryStepCrossController.handleTimeStampChange(boundaryStep);
    assertTrue(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldDoNothing() {
    boundaryStep.getBoundaryStepCrossInfo().setTimestampOfFirstCrossing("test");
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    boundaryStepCrossController.handleTimeStampChange(boundaryStep);
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(2);
    assertFalse(boundaryStep.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
  }

  @Test
  void shouldGet1BoundaryToAlert() {
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    when(threshold.getBoundariesSteps()).thenReturn(Collections.singletonList(boundaryStep));
    assertEquals(1, boundaryStepCrossController.getBoundariesStepToAlert().size());
  }

  @Test
  void shouldGet2BoundariesToAlert() {
    boundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    customDimBoundaryStep.getBoundaryStepCrossInfo().setCrossCounter(1);
    customDimBoundaryStep.setChosenForAlert(true);
    CustomDimBoundaryStep customDimBoundaryStep2 = new CustomDimBoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 0),
      1,
      BoundaryStep.SeverityType.CRITICAL,
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 0),
      customDimBoundaryStep.getBoundaryStepCrossInfo()
    );
    when(threshold.getBoundariesSteps()).thenReturn(Arrays.asList(boundaryStep, customDimBoundaryStep, customDimBoundaryStep2));
    List<BoundaryStep> res = boundaryStepCrossController.getBoundariesStepToAlert();
    assertEquals(2, res.size());
    assertTrue(res.contains(boundaryStep));
    assertTrue(res.contains(customDimBoundaryStep));

  }


}
