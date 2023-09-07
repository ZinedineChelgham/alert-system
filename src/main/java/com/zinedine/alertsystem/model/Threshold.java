package com.zinedine.alertsystem.model;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;


/**
 * Entity class representing a threshold definition for sensor data analysis.
 */
@Entity
public class Threshold {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private SensorDataInfo yThresholdDataInfo;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private SensorDataInfo xThresholdDataInfo;

  @Embedded
  private ReceivedDataImpl busitData;


  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<BoundaryStep> boundarySteps;
  private int desiredMeasurements;


  private Threshold(Builder builder) {
    this.busitData = builder.busitData;
    this.yThresholdDataInfo = builder.yThresholdDataInfo;
    this.boundarySteps = builder.boundaries;
    this.desiredMeasurements = builder.desiredMeasurements;
    this.xThresholdDataInfo = builder.xThresholdDataInfo;
  }

  /**
   * Default constructor required for JPA.
   */
  public Threshold() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setyThresholdDataInfo(SensorDataInfo yThresholdDataInfo) {
    this.yThresholdDataInfo = yThresholdDataInfo;
  }

  public void setBusitData(ReceivedDataImpl busitData) {
    this.busitData = busitData;
  }

  public List<BoundaryStep> getBoundarySteps() {
    return boundarySteps;
  }

  public void setBoundarySteps(List<BoundaryStep> boundarySteps) {
    this.boundarySteps = boundarySteps;
  }

  public void setDesiredMeasurements(int desiredMeasurements) {
    this.desiredMeasurements = desiredMeasurements;
  }

  public void setxThresholdDataInfo(SensorDataInfo xThresholdDataInfo) {
    this.xThresholdDataInfo = xThresholdDataInfo;
  }


  @Override
  public String toString() {
    return "BoundaryStep{" +
      "sensorDataInfo=" + yThresholdDataInfo +
      ", busitData=" + busitData +
      ", boundaries=" + boundarySteps +
      ", desiredMeasurements=" + desiredMeasurements +
      '}';
  }


  // Getters for the private fields (you can add setters if necessary)
  public String getIdField() {
    return yThresholdDataInfo.getIdField();
  }


  public String getIdValue() {
    return yThresholdDataInfo.getIdValue();
  }


  public String getField() {
    return yThresholdDataInfo.getField();
  }


  public List<BoundaryStep> getBoundariesSteps() {
    return boundarySteps;
  }


  public int getDesiredMeasurements() {
    return desiredMeasurements;
  }


  public SensorDataInfo getyThresholdDataInfo() {
    return yThresholdDataInfo;
  }


  public ReceivedDataImpl getBusitData() {
    return busitData;
  }


  public String getUuid() {
    return busitData.getUuid();
  }

  public SensorDataInfo getxThresholdDataInfo() {
    return xThresholdDataInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Threshold threshold = (Threshold) o;
    return desiredMeasurements == threshold.desiredMeasurements
      && Objects.equals(id, threshold.id)
      && Objects.equals(yThresholdDataInfo, threshold.yThresholdDataInfo)
      && Objects.equals(xThresholdDataInfo, threshold.xThresholdDataInfo)
      && Objects.equals(busitData, threshold.busitData)
      && Objects.equals(boundarySteps, threshold.boundarySteps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, yThresholdDataInfo, xThresholdDataInfo, busitData, boundarySteps, desiredMeasurements);
  }

  /**
   * Builder class for constructing Threshold instances.
   */
  public static class Builder {
    private ReceivedDataImpl busitData;
    private SensorDataInfo yThresholdDataInfo;
    private List<BoundaryStep> boundaries;
    private int desiredMeasurements = 1;
    private SensorDataInfo xThresholdDataInfo = null; // classic threshold does not have an additional custom dimension


    public Builder withBoundariesStep(List<BoundaryStep> boundaries) {
      this.boundaries = boundaries;
      return this;
    }


    public Builder withYthresholdDataInfo(SensorDataInfo sensorDataInfo) {
      this.yThresholdDataInfo = sensorDataInfo;
      return this;
    }


    public Builder withReceiveData(ReceivedDataImpl busitData) {
      this.busitData = busitData;
      return this;
    }

    public Builder withDesiredMeasurements(int desiredMeasurements) {
      this.desiredMeasurements = desiredMeasurements;
      return this;
    }

    public Builder withXthresholdDataInfo(SensorDataInfo customDimDataInfo) {
      this.xThresholdDataInfo = customDimDataInfo;
      return this;
    }


    // Build method to create a new BoundaryStep instance
    public Threshold build() {
      return new Threshold(this);
    }
  }
}
