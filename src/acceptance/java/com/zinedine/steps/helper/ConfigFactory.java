package acceptance.java.com.zinedine.steps.helper;

import acceptance.java.com.zinedine.steps.helper.ThresholdConfigParams;

import java.util.Map;

public class ConfigFactory {

  public static ThresholdConfigParams createThresholdConfigParams(Map<String, String> dataTable) {
    if(dataTable.containsKey("x-threshold2")) {
      return new AdvancedFloorThresholdConfigParams(dataTable);
    } else if (dataTable.containsKey("x-threshold")) {
      return new SimpleFloorThresholdConfigParams(dataTable);
    } else {
      return new ThresholdConfigParams(dataTable);
    }
  }
}
