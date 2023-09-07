package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.model.BoundaryStep;
import com.zinedine.alertsystem.model.BoundaryStepCrossInfo;
import com.zinedine.alertsystem.model.CustomDimBoundaryStep;
import com.zinedine.alertsystem.model.Threshold;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A controller class responsible for checking boundary step crossings and handling actions related to it.
 */
public class BoundaryStepCrossController {
  private Threshold threshold;
  private AlertChecker alertChecker;


  /**
   * Constructs a BoundaryStepCrossController with the specified threshold and alert checker.
   *
   * @param threshold    The threshold containing boundary steps to monitor.
   * @param alertChecker The alert checker used to retrieve values for checking.
   */
  public BoundaryStepCrossController(Threshold threshold, AlertChecker alertChecker) {
    this.threshold = threshold;
    this.alertChecker = alertChecker;

  }

  public BoundaryStepCrossController() {
  }


  /**
   * Apply the correct check to the threshold depending on the type of boundary steps it contains.
   * If the provided y values are within excluded ranges,
   * increments cross counters and handles alert conditions.
   *
   * @throws Exception If an error occurs during checking.
   */
  public void checkValue() throws Exception {
    if (threshold.getBoundariesSteps().stream().allMatch(s -> s instanceof CustomDimBoundaryStep)) {
      this.checkForCustomDimBoundaryStep();
    } else if (threshold.getBoundariesSteps().stream().noneMatch(s -> s instanceof CustomDimBoundaryStep)) {
      threshold.getBoundariesSteps().forEach(step -> {
        try {
          this.checkForSimpleBoundaryStep(step);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    } else {
      throw new Exception("All the boundaries of the list should be of same type");
    }
  }


  /**
   * Checks for boundary crossings for simple BoundarySteps.
   *
   * @param step The step to check.
   * @throws Exception If an error occurs retrieving the value to check.
   */
  public void checkForSimpleBoundaryStep(BoundaryStep step) throws Exception {
    // Retrieve the value to check from the alert checker
    Optional<Float> valueToCheck = alertChecker.getValueToCheck(threshold.getyThresholdDataInfo());
    if (!valueToCheck.isPresent()) {
      throw new Exception("The y value should be present");
    }
    float value = valueToCheck.get();

    if (step.getExcludedYRange().contains(value)) {
      step.getBoundaryStepCrossInfo().incrementCrossCounter();
      step.getBoundaryStepCrossInfo().resetReturnToNormalCounter();
      this.handleTimeStampChange(step);
    } else {
      if (step.getBoundaryStepCrossInfo().getIsInAlertState()) {
        step.getBoundaryStepCrossInfo().incrementReturnToNormalCounter();
        if (step.shouldReturnToNormal()) {
          step.getBoundaryStepCrossInfo().returnToNormal();
        }
      } else {
        step.getBoundaryStepCrossInfo().decrementCrossCounter();
        this.handleTimeStampChange(step);
      }
    }
  }

  /**
   * Checks for boundary crossings for CustomDimBoundarySteps.
   * If the provided x and y values are within excluded ranges,
   * increments cross counters and handles alert conditions.
   *
   * @throws Exception If an error occurs retrieving the value to check.
   */
  public void checkForCustomDimBoundaryStep() throws Exception {
    Optional<Float> xValueToCheck = alertChecker.getValueToCheck(threshold.getxThresholdDataInfo());
    Optional<Float> yValueToCheck = alertChecker.getValueToCheck(threshold.getyThresholdDataInfo());
    if (!xValueToCheck.isPresent() || !yValueToCheck.isPresent()) {
      throw new Exception("The x and y values should be present");
    }
    Float xValue = xValueToCheck.get();
    Float yValue = yValueToCheck.get();

    // Check if the x and y values are within the excluded ranges of a step
    Optional<CustomDimBoundaryStep> crossedStep = threshold.getBoundariesSteps()
      .stream()
      .map(s -> (CustomDimBoundaryStep) s)
      .filter(s -> s.getExcludedXRange().contains(xValue) && s.getExcludedYRange().contains(yValue))
      .findFirst();

    CustomDimBoundaryStep step;
    if (crossedStep.isPresent()) {
      step = crossedStep.get();
      this.setChosenForAlertToFalseForAllExceptStep(step); // make sure only one alert is triggered at a time
      step.getBoundaryStepCrossInfo().incrementCrossCounter();
      this.handleTimeStampChange(step);
    } else {
      BoundaryStepCrossInfo sharedBoundaryStepCrossInfo = threshold.getBoundariesSteps().get(0).getBoundaryStepCrossInfo(); // All the customDim steps share the same BoundaryStepCrossInfo
      if (sharedBoundaryStepCrossInfo.isInAlertState()) {
        sharedBoundaryStepCrossInfo.incrementReturnToNormalCounter();
        if (sharedBoundaryStepCrossInfo.shouldReturnToNormal()) {
          sharedBoundaryStepCrossInfo.returnToNormal();
          this.resetChosenForAlertForAll();
        }
      } else {
        sharedBoundaryStepCrossInfo.decrementCrossCounter();
        this.handleTimeStampChange(threshold.getBoundariesSteps().get(0));
      }
    }
  }


  /**
   * Handle the timestamp change of the step depending on the number of times it has been crossed.
   *
   * @param step The step to handle.
   */
  void handleTimeStampChange(BoundaryStep step) {
    switch (step.getBoundaryStepCrossInfo().getCrossCounter()) {
      case 0:
        step.getBoundaryStepCrossInfo().setTimestampOfFirstCrossing("");
        break;
      case 1:
        step.getBoundaryStepCrossInfo().setTimestampOfFirstCrossing(LocalDateTime.now().toString());
        break;
    }
  }


  /**
   * Returns the list of BoundarySteps to alert.
   * Sets the isInAlertState flag to true to avoid re-triggering the alerts before returning to normal
   * @return The list of BoundarySteps to alert.
   */
  public List<BoundaryStep> getBoundariesStepToAlert() {
    List<BoundaryStep> toAlert = threshold.getBoundariesSteps()
      .stream()
      .filter(BoundaryStep::shouldAlert)
      .collect(Collectors.toList());
    toAlert.forEach(step -> step.getBoundaryStepCrossInfo().setIsInAlertState(true));
    return toAlert;
  }

  /**
   * Helper method Sets the chosenForAlert flag to true for the specified step and to false for all the others.
   * @param step The step to set as chosen for alert.
   */
  private void setChosenForAlertToFalseForAllExceptStep(CustomDimBoundaryStep step) {
    threshold.getBoundariesSteps()
      .stream()
      .map(s -> (CustomDimBoundaryStep) s)
      .filter(s -> !s.equals(step))
      .forEach(s -> s.setChosenForAlert(false));
    step.setChosenForAlert(true);
  }


  /**
   * Helper method Sets the chosenForAlert flag to false for all the steps.
   */
  private void resetChosenForAlertForAll() {
    threshold.getBoundariesSteps()
      .stream()
      .map(s -> (CustomDimBoundaryStep) s)
      .forEach(s -> s.setChosenForAlert(false));
  }

  public void setAlertChecker(AlertChecker alertChecker) {
    this.alertChecker = alertChecker;
  }

  public void setThreshold(Threshold threshold) {
    this.threshold = threshold;
  }
}

