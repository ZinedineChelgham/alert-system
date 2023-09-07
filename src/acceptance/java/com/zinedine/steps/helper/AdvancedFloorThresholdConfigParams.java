package acceptance.java.com.zinedine.steps.helper;

import com.zinedine.alertsystem.model.*;

import java.util.Arrays;
import java.util.Map;

public class AdvancedFloorThresholdConfigParams extends SimpleFloorThresholdConfigParams {

  private float xThreshold2;
  private RangeFactory.RangeType xThresholdOperator2;
  private float yThreshold2;
  private RangeFactory.RangeType yThresholdOperator2;
  public AdvancedFloorThresholdConfigParams(Map<String, String> dataTable) {
    super(dataTable);
    setxThreshold2(Float.parseFloat(dataTable.get("x-threshold2")));
    setxThresholdOperator2(RangeFactory.RangeType.valueOf(dataTable.get("x-threshold-operator2")));
    setyThreshold2(Float.parseFloat(dataTable.get("y-threshold2")));
    setyThresholdOperator2(RangeFactory.RangeType.valueOf(dataTable.get("y-threshold-operator2")));
  }

  public CustomDimBoundaryStep createBoundary(RangeFactory.RangeType operator, float threshold,
                                              RangeFactory.RangeType customDimOperator,
                                              float customDimThreshold)
  {
    return new CustomDimBoundaryStep(super.createRange(operator, threshold),
      super.getToleratedExcess(),
      super.getSeverity(),
      super.createRange(customDimOperator, customDimThreshold),
      super.getCrossInfo()
      );
  }

  @Override
  public Threshold createThreshold() {
     return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl("", ""))
      .withYthresholdDataInfo(new SensorDataInfo("", "", super.getyField()))
      .withXthresholdDataInfo(new SensorDataInfo("", "", super.getxField()))
      .withBoundariesStep(Arrays.asList(
        this.createBoundary(
          super.getyThresholdOperator(), super.getyThreshold(),
          super.getxThresholdOperator(), super.getxThreshold()
        ),
        this.createBoundary(
          this.yThresholdOperator2, this.yThreshold2,
          this.xThresholdOperator2, this.xThreshold2
        )))
      .withDesiredMeasurements(super.getDesiredMeasurements())
      .build();
  }

  public void setxThreshold2(float xThreshold2) {
    this.xThreshold2 = xThreshold2;
  }

  public void setxThresholdOperator2(RangeFactory.RangeType xThresholdOperator2) {
    this.xThresholdOperator2 = xThresholdOperator2;
  }

  public void setyThreshold2(float yThreshold2) {
    this.yThreshold2 = yThreshold2;
  }

  public void setyThresholdOperator2(RangeFactory.RangeType yThresholdOperator2) {
    this.yThresholdOperator2 = yThresholdOperator2;
  }
}
