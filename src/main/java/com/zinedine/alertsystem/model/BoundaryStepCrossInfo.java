package com.zinedine.alertsystem.model;


import javax.persistence.*;
import java.util.Objects;

/**
 * A class representing cross information for boundary steps.
 * This class defines counters and conditions related to threshold crossing detection.
 */
@Entity
public class BoundaryStepCrossInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private int crossCounter = 0;
  private int requiredExcessToAlert = 1;
  private String timestampOfFirstCrossing = "";
  private int returnToNormalCounter = 0;
  private int fixedReturnToNormal;
  private boolean isInAlertState = false;

  /**
   * Constructs a BoundaryStepCrossInfo with a fixed return to normal value.
   *
   * @param fixedReturnToNormal The fixed value to trigger return to normal.
   */
  public BoundaryStepCrossInfo(int fixedReturnToNormal) {
    this.fixedReturnToNormal = fixedReturnToNormal;
  }

  /**
   * Constructs a BoundaryStepCrossInfo with a fixed return to normal value and a required excess to trigger an alert.
   *
   * @param fixedReturnToNormal   The fixed value to trigger return to normal.
   * @param requiredExcessToAlert The required excess of crossing to trigger an alert.
   */
  public BoundaryStepCrossInfo(int fixedReturnToNormal, int requiredExcessToAlert) {
    this.fixedReturnToNormal = fixedReturnToNormal;
    this.requiredExcessToAlert = requiredExcessToAlert;
  }

  /**
   * Default constructor for hibernate.
   */
  public BoundaryStepCrossInfo() {

  }

  public void incrementCrossCounter() {
    this.crossCounter++;
  }
  public void incrementReturnToNormalCounter() {
    this.returnToNormalCounter++;
  }

  public void resetCrossedCounter() {
    this.crossCounter = 0;
  }

  public int getCrossCounter() {
    return crossCounter;
  }


  /**
   * Returns to a normal state after being in an alert state.
   */
  public void returnToNormal(){
    this.crossCounter = 0;
    this.returnToNormalCounter = 0;
    this.timestampOfFirstCrossing = "";
    this.isInAlertState = false;
  }

  /**
   * Checks if the threshold should return to normal.
   * @return True if the threshold should return to normal, otherwise false.
   */
  public boolean shouldReturnToNormal(){
    return this.returnToNormalCounter >= this.fixedReturnToNormal;
  }

  /**
   * Checks if the BoundaryStep should alert.
   * @return True if the BoundaryStep should alert, otherwise false.
   */
  public boolean shouldAlert(){
    return this.crossCounter == this.requiredExcessToAlert && !this.isInAlertState;
  }


  @Transient // This annotation is needed for hibernate to ignore this method and not consider it as a column (because of the naming looking like a boolean attribute)
  public boolean isCrossLimitReached() {
    return this.crossCounter >= this.requiredExcessToAlert;
  }

  @Transient
  public boolean isCrossLimitExceeded() {
    return this.crossCounter > this.requiredExcessToAlert;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getRequiredExcessToAlert() {
    return requiredExcessToAlert;
  }

  public String getTimestampOfFirstCrossing() {
    return timestampOfFirstCrossing;
  }

  public void setCrossCounter(int consecutiveCrossedCount) {
    this.crossCounter = consecutiveCrossedCount;
  }

  public void setTimestampOfFirstCrossing(String timestampOfFirstCrossing) {
    this.timestampOfFirstCrossing = timestampOfFirstCrossing;
  }

  public void setRequiredExcessToAlert(int requiredExcessToAlert) {
    this.requiredExcessToAlert = requiredExcessToAlert;
  }
  public void setFixedReturnToNormal(int fixedReturnToNormal) {
    this.fixedReturnToNormal = fixedReturnToNormal;
  }

  public int getFixedReturnToNormal() {
    return fixedReturnToNormal;
  }

  public int getReturnToNormalCounter() {
    return returnToNormalCounter;
  }

  public void setReturnToNormalCounter(int returnToNormalCount) {
    this.returnToNormalCounter = returnToNormalCount;
  }

  public void setIsInAlertState(boolean inAlertState) {
    isInAlertState = inAlertState;
  }

  public boolean getIsInAlertState() {
    return isInAlertState;
  }

  @Transient
  public boolean isInAlertState() {
    return isInAlertState;
  }

  public void setInAlertState(boolean inAlertState) {
    isInAlertState = inAlertState;
  }

  public void resetReturnToNormalCounter() {
    this.returnToNormalCounter = 0;
  }

  public void decrementCrossCounter() {
    if(this.crossCounter > 0){
      this.crossCounter--;
    }
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BoundaryStepCrossInfo that = (BoundaryStepCrossInfo) o;

    if (crossCounter != that.crossCounter) return false;
    if (requiredExcessToAlert != that.requiredExcessToAlert) return false;
    if (returnToNormalCounter != that.returnToNormalCounter) return false;
    if (fixedReturnToNormal != that.fixedReturnToNormal) return false;
    if (isInAlertState != that.isInAlertState) return false;
    return timestampOfFirstCrossing.equals(that.timestampOfFirstCrossing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crossCounter,
      requiredExcessToAlert, timestampOfFirstCrossing, returnToNormalCounter,
      fixedReturnToNormal, isInAlertState);
  }

  @Override
  public String toString() {
    return "BoundaryCrossInfo{" +
      "consecutiveCrossedCount=" + crossCounter +
      ", requiredExcessToAlert=" + requiredExcessToAlert +
      ", timestampOfFirstCrossing='" + timestampOfFirstCrossing + '\'' +
      ", returnToNormal=" + fixedReturnToNormal +
      ", returnToNormalCount=" + returnToNormalCounter +
      ", isInAlertState=" + isInAlertState +
      '}';
  }


}

