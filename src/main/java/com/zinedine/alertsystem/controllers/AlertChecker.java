package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.model.*;

import java.util.List;
import java.util.Optional;


/**
 * Interface for classes responsible for checking alerts based on given thresholds and data.
 */
public interface AlertChecker {


  /**
   * Creates an instance of AlertChecker for handling alert checks with mean values.
   *
   * @param threshold The threshold configuration.
   * @param dsCouple  The DatalakeSerieCouple associated with the alert.
   * @param retriever The sensor data retriever.
   * @param data      The BusitData associated with the alert.
   * @return An instance of AlertChecker.
   */
  static AlertChecker create(Threshold threshold, DatalakeSerieCouple dsCouple, SensorDataRetriever retriever,
                             ReceivedData data) {
    return new AlertCheckerMeanValues(threshold, dsCouple, data, retriever);
  }

  /**
   * Creates an instance of AlertChecker for handling alert checks with a single value.
   *
   * @param threshold The threshold configuration.
   * @param dsCouple  The DatalakeSerieCouple associated with the alert.
   * @param data      The BusitData associated with the alert.
   * @return An instance of AlertChecker.
   */
  static AlertChecker create(Threshold threshold, DatalakeSerieCouple dsCouple, ReceivedData data) {
    return new AlertCheckerSingleValue(threshold, data, dsCouple);
  }

  /**
   * Returns a list of BoundarySteps that should trigger alerts.
   *
   * @return A list of BoundarySteps to alert.
   */
  List<BoundaryStep> getBoundariesStepsToAlert();


  /**
   * Retrieves the value to check for alert conditions.
   *
   * @param sensorDataInfo The information about the sensor data.
   * @return The value to check, wrapped in an Optional, or an empty Optional if no value is available.
   */
  Optional<Float> getValueToCheck(SensorDataInfo sensorDataInfo);

}
