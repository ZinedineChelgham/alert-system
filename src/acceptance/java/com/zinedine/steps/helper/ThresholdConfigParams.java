package acceptance.java.com.zinedine.steps.helper;

import com.google.common.collect.Range;
import com.zinedine.alertsystem.model.*;

import java.util.Collections;
import java.util.Map;

public class ThresholdConfigParams {
  private String yField;
  private float yThreshold;
  private RangeFactory.RangeType yThresholdOperator;
  private BoundaryStep.SeverityType severity;
  private int toleratedExcess;
  private int desiredMeasurements;
  private int returnToNormal;

  public ThresholdConfigParams(Map<String, String> dataTable) {
    setyField(dataTable.get("y-field"));
    setyThreshold(Float.parseFloat(dataTable.get("y-threshold")));
    setyThresholdOperator(RangeFactory.RangeType.valueOf(dataTable.get("y-threshold-operator")));
    setSeverity(BoundaryStep.SeverityType.valueOf(dataTable.get("severity")));
    setToleratedExcess(Integer.parseInt(dataTable.get("required-excess-to-alert")));
    String desiredMeasurementsStr = dataTable.get("desired-measurements");
    setDesiredMeasurements(
      Integer.parseInt(desiredMeasurementsStr == null ? "1" : desiredMeasurementsStr)
    );
    String returnToNormalStr = dataTable.get("return-to-normal");
    setReturnToNormal(returnToNormalStr == null ? 1 : Integer.parseInt(returnToNormalStr));
  }

  public Range<Float> createRange(RangeFactory.RangeType operator, float threshold) {
    return RangeFactory.createRange(operator, threshold);
  }

  public BoundaryStep createBoundary() {
    return new BoundaryStep(createRange(this.yThresholdOperator, this.yThreshold), toleratedExcess, severity, returnToNormal);
  }


  public Threshold createThreshold() {
    return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl("", ""))
      .withYthresholdDataInfo(new SensorDataInfo("", "", this.yField))
      .withBoundariesStep(Collections.singletonList(createBoundary()))
      .withDesiredMeasurements(desiredMeasurements)
      .build();
  }

  public void setReturnToNormal(int returnToNormal) {
    this.returnToNormal = returnToNormal;
  }

  public String getyField() {
    return yField;
  }

  public float getyThreshold() {
    return yThreshold;
  }

  public BoundaryStep.SeverityType getSeverity() {
    return severity;
  }

  public int getToleratedExcess() {
    return toleratedExcess;
  }

  public int getReturnToNormal() {
    return returnToNormal;
  }

  public int getDesiredMeasurements() {
    return desiredMeasurements;
  }

  public RangeFactory.RangeType getyThresholdOperator() {
    return yThresholdOperator;
  }

  public void setyThreshold(float yThreshold) {
    this.yThreshold = yThreshold;
  }

  public void setSeverity(BoundaryStep.SeverityType severity) {
    this.severity = severity;
  }

  public void setyField(String yField) {
    this.yField = yField;
  }

  public void setyThresholdOperator(RangeFactory.RangeType yThresholdOperator) {
    this.yThresholdOperator = yThresholdOperator;
  }

  public void setToleratedExcess(int toleratedExcess) {
    this.toleratedExcess = toleratedExcess;
  }

  public void setDesiredMeasurements(int desiredMeasurements) {
    this.desiredMeasurements = desiredMeasurements;
  }


}

