package com.zinedine.alertsystem.controllers;

import com.zinedine.alertsystem.model.ReceivedData;
import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.Threshold;

/**
 * Factory class for creating instances of {@link AlertChecker}.
 */
public class AlertCheckerFactory {

  /**
   * Creates an instance of {@link AlertChecker} based on the given parameters.
   *
   * @param threshold The threshold configuration.
   * @param dsCouple  The DatalakeSerieCouple associated with the alert.
   * @param retriever The sensor data retriever.
   * @param data      The BusitData associated with the alert.
   * @return An instance of AlertChecker.
   */
  public static AlertChecker createAlertChecker(
    Threshold threshold,
    DatalakeSerieCouple dsCouple,
    SensorDataRetriever retriever,
    ReceivedData data) {
    if (threshold.getDesiredMeasurements() > 1) {
      return AlertChecker.create(threshold, dsCouple, retriever, data);
    } else {
      return AlertChecker.create(threshold, dsCouple, data);
    }
  }

}
