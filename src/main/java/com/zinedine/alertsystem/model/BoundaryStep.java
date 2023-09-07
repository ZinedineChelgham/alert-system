package com.zinedine.alertsystem.model;


import com.zinedine.alertsystem.utils.RangeConverter;
import com.google.common.collect.Range;

import javax.persistence.*;
import java.util.Objects;


/**
 * A class representing a boundary step for threshold monitoring.
 * This class defines a step within a threshold that defines a range
 * of values, cross information, and a severity level.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BoundaryStep {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Convert(converter = RangeConverter.class)
  private Range<Float> excludedYRange;

  @OneToOne(cascade = CascadeType.ALL)
  private BoundaryStepCrossInfo boundaryStepCrossInfo;
  @Enumerated(EnumType.ORDINAL)
  private SeverityType severity;

  /**
   * Enumeration representing different severity levels for boundary steps.
   */
  public enum SeverityType {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
  }

  /**
   * Constructs a boundary step with an excluded Y range.
   *
   * @param excludedYRange The range of values that are excluded from crossing detection.
   */
  public BoundaryStep(Range<Float> excludedYRange) {
    this.excludedYRange = excludedYRange;
    this.boundaryStepCrossInfo = new BoundaryStepCrossInfo(1);
  }

  /**
   * Constructs a boundary step with an excluded Y range and a required excess to trigger an alert.
   *
   * @param excludedYRange        The range of values that are excluded from crossing detection.
   * @param requiredExcessToAlert The required excess of crossing to trigger an alert.
   */
  public BoundaryStep(Range<Float> excludedYRange, int requiredExcessToAlert) {
    this(excludedYRange);
    this.boundaryStepCrossInfo.setRequiredExcessToAlert(requiredExcessToAlert);
  }

  /**
   * Constructs a boundary step with an excluded Y range, a required excess to trigger an alert, and a severity level.
   *
   * @param excludedYRange        The range of values that are excluded from crossing detection.
   * @param requiredExcessToAlert The required excess of crossing to trigger an alert.
   * @param severity              The severity level associated with the boundary step.
   */
  public BoundaryStep(Range<Float> excludedYRange, int requiredExcessToAlert, SeverityType severity) {
    this(excludedYRange, requiredExcessToAlert);
    this.boundaryStepCrossInfo.setRequiredExcessToAlert(requiredExcessToAlert);
    this.severity = severity;
  }

  /**
   * Constructs a boundary step with an excluded Y range, a required excess to trigger an alert,
   * a severity level, and a fixed return to normal value.
   *
   * @param excludedYRange        The range of values that are excluded from crossing detection.
   * @param requiredExcessToAlert The required excess of crossing to trigger an alert.
   * @param severity              The severity level associated with the boundary step.
   * @param returnToNormal        The fixed value to trigger return to normal.
   */
  public BoundaryStep(Range<Float> excludedYRange, int requiredExcessToAlert, SeverityType severity, int returnToNormal) {
    this(excludedYRange, requiredExcessToAlert, severity);
    this.boundaryStepCrossInfo.setFixedReturnToNormal(returnToNormal);
  }

  /**
   * Default constructor for hibernate.
   */
  public BoundaryStep() {
  }


  protected void setThresholdCrossInfo(BoundaryStepCrossInfo boundaryStepCrossInfo) {
    this.boundaryStepCrossInfo = boundaryStepCrossInfo;
  }

  /**
   * Checks if an alert should be triggered based on the boundary step's cross information.
   *
   * @return True if an alert should be triggered, otherwise false.
   */
  public boolean shouldAlert() {
    return this.boundaryStepCrossInfo.shouldAlert();
  }

  /**
   * Checks if a return to normal should be triggered based on the boundary step's cross information.
   *
   * @return True if a return to normal should be triggered, otherwise false.
   */
  public boolean shouldReturnToNormal() {
    return this.boundaryStepCrossInfo.shouldReturnToNormal();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Range<Float> getExcludedYRange() {
    return excludedYRange;
  }

  public BoundaryStepCrossInfo getBoundaryStepCrossInfo() {
    return boundaryStepCrossInfo;
  }

  public SeverityType getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityType severity) {
    this.severity = severity;
  }


  public void setExcludedYRange(Range<Float> excludedYRange) {
    this.excludedYRange = excludedYRange;
  }

  public void setBoundaryStepCrossInfo(BoundaryStepCrossInfo boundaryStepCrossInfo) {
    this.boundaryStepCrossInfo = boundaryStepCrossInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BoundaryStep that = (BoundaryStep) o;
    if (!excludedYRange.equals(that.excludedYRange)) return false;
    if (!boundaryStepCrossInfo.equals(that.boundaryStepCrossInfo)) return false;
    return severity == that.severity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(excludedYRange, boundaryStepCrossInfo, severity);
  }

  @Override
  public String toString() {
    return "BoundaryStep{" +
      "excludedYRange=" + excludedYRange +
      ", boundaryStepCrossInfo=" + boundaryStepCrossInfo +
      ", severity=" + severity +
      '}';
  }
}
