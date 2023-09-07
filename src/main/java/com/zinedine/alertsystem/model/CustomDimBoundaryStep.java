package com.zinedine.alertsystem.model;

import com.zinedine.alertsystem.utils.RangeConverter;
import com.google.common.collect.Range;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * A class representing a custom dimension boundary step for threshold monitoring.
 * This class extends the base BoundaryStep class and includes additional properties specific to custom dimensions
 * for a special case of threshold monitoring.
 */
@Entity
public class CustomDimBoundaryStep extends BoundaryStep {

  @Convert(converter = RangeConverter.class)
  private Range<Float> excludedXRange;
  private boolean chosenForAlert = false; // Since the CustomDimBoundary shares the same BoundaryStepCrossInfo we need to add this attribute to trigger only one alert at a time

  /**
   * Constructs a CustomDimBoundaryStep with specified parameters.
   *
   * @param excludedYRange        The range of values that are excluded from crossing detection in the Y dimension.
   * @param requiredExcessToAlert The required excess of crossing to trigger an alert.
   * @param severity              The severity level associated with the boundary step.
   * @param excludedXRange        The range of values that are excluded from crossing detection in the X dimension.
   * @param boundaryStepCrossInfo The cross information associated with the boundary step.
   */
  public CustomDimBoundaryStep(
    Range<Float> excludedYRange,
    int requiredExcessToAlert,
    SeverityType severity,
    Range<Float> excludedXRange,
    BoundaryStepCrossInfo boundaryStepCrossInfo
  ) {
    super(excludedYRange, requiredExcessToAlert, severity);
    this.excludedXRange = excludedXRange;
    super.setThresholdCrossInfo(boundaryStepCrossInfo);
  }

  public CustomDimBoundaryStep() {
    super();
  }

  @Override
  public Range<Float> getExcludedYRange() {
    return super.getExcludedYRange();
  }

  @Override
  public boolean shouldAlert() {
    return super.shouldAlert() && chosenForAlert;
  }

  public Range<Float> getExcludedXRange() {
    return excludedXRange;
  }

  public void setChosenForAlert(boolean chosenForAlert) {
    this.chosenForAlert = chosenForAlert;
  }

  public boolean getChosenForAlert() {
    return chosenForAlert;
  }

  public void setExcludedXRange(Range<Float> excludedXRange) {
    this.excludedXRange = excludedXRange;
  }

  @Transient
  public boolean isChosenForAlert() {
    return chosenForAlert;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CustomDimBoundaryStep)) return false;
    if (!super.equals(o)) return false;

    CustomDimBoundaryStep that = (CustomDimBoundaryStep) o;

    if (chosenForAlert != that.chosenForAlert) return false;
    return excludedXRange.equals(that.excludedXRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), excludedXRange, chosenForAlert);
  }

  @Override
  public String toString() {
    return "CustomDimBoundaryStep{" + "excludedXRange=" + excludedXRange + ", " + super.toString() + '}';
  }
}
