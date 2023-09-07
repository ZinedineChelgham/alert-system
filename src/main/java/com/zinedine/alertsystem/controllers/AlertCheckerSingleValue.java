package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.model.*;

import java.util.List;
import java.util.Optional;

/**
 * A class that implements AlertChecker and performs alert checking based on a single value.
 */
public class AlertCheckerSingleValue implements AlertChecker {

  protected final Threshold threshold;
  protected final DatalakeSerieCouple dsCouple;
  protected final ReceivedData data;


  /**
   * Constructs an instance of AlertCheckerSingleValue.
   *
   * @param threshold The threshold object defining alert criteria.
   * @param data      The BusitData object containing relevant data.
   * @param dsCouple  The data lake series couple associated with the alert.
   */
  public AlertCheckerSingleValue(Threshold threshold,
                                 ReceivedData data,
                                 DatalakeSerieCouple dsCouple) {
    this.threshold = threshold;
    this.dsCouple = dsCouple;
    this.data = data;
  }

  /**
   * Retrieves the boundaries steps that trigger an alert.
   *
   * @return The list of BoundaryStep objects that trigger an alert.
   */
  public List<BoundaryStep> getBoundariesStepsToAlert() {
    BoundaryStepCrossController boundaryController = new BoundaryStepCrossController(threshold, this);
    try {
      boundaryController.checkValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return boundaryController.getBoundariesStepToAlert();
  }

  /**
   * Retrieves the value to check against the threshold.
   *
   * @param sensorDataInfo The sensor data information.
   * @return An Optional containing the value to check for alert triggering.
   */
  @Override
  public Optional<Float> getValueToCheck(SensorDataInfo sensorDataInfo) {
    return data.getThresholdRelatedData(sensorDataInfo);
  }


}
