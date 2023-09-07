package acceptance.java.com.zinedine.steps.helper;

import com.zinedine.alertsystem.model.*;

import java.util.Collections;
import java.util.Map;

public class SimpleFloorThresholdConfigParams extends ThresholdConfigParams {

  private String xField;
  private float xThreshold;
  private RangeFactory.RangeType xThresholdOperator;
  private BoundaryStepCrossInfo crossInfo;


  public SimpleFloorThresholdConfigParams(Map<String, String> dataTable) {
    super(dataTable);
    crossInfo = new BoundaryStepCrossInfo(super.getReturnToNormal(), super.getToleratedExcess());
    setxField(dataTable.get("x-field"));
    setxThreshold(Float.parseFloat(dataTable.get("x-threshold")));
    setxThresholdOperator(RangeFactory.RangeType.valueOf(dataTable.get("x-threshold-operator")));
  }


  @Override
  public CustomDimBoundaryStep createBoundary() {
    return new CustomDimBoundaryStep(super.createRange(super.getyThresholdOperator(), super.getyThreshold()),
      super.getToleratedExcess(),
      super.getSeverity(),
      super.createRange(this.xThresholdOperator, this.xThreshold),
      crossInfo
    );
  }

  @Override
  public Threshold createThreshold() {
    return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl("", ""))
      .withYthresholdDataInfo(new SensorDataInfo("", "", super.getyField()))
      .withXthresholdDataInfo(new SensorDataInfo("", "", this.xField))
      .withBoundariesStep(Collections.singletonList(this.createBoundary()))
      .withDesiredMeasurements(super.getDesiredMeasurements())
      .build();
  }

  public String getxField() {
    return xField;
  }

  public float getxThreshold() {
    return xThreshold;
  }

  public RangeFactory.RangeType getxThresholdOperator() {
    return xThresholdOperator;
  }

  public void setxField(String xField) {
    this.xField = xField;
  }

  public void setxThreshold(float xThreshold) {
    this.xThreshold = xThreshold;
  }

  public void setxThresholdOperator(RangeFactory.RangeType xThresholdOperator) {
    this.xThresholdOperator = xThresholdOperator;
  }

  public BoundaryStepCrossInfo getCrossInfo() {
    return crossInfo;
  }
}
