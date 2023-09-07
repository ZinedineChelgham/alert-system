package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.model.*;
import java.util.List;
import java.util.Optional;


/**
 * A class that extends the functionality of AlertCheckerSingleValue by calculating
 * the mean value of sensor data for alert checking.
 */
public class AlertCheckerMeanValues extends AlertCheckerSingleValue {

  private final SensorDataRetriever sensorDataRetriever;


  /**
   * Constructs an instance of AlertCheckerMeanValues.
   *
   * @param threshold           The threshold object defining alert criteria.
   * @param dsCouple            The data lake series couple associated with the alert.
   * @param data                The BusitData object containing relevant data.
   * @param sensorDataRetriever The SensorDataRetriever for fetching sensor data.
   */
  public AlertCheckerMeanValues(
    Threshold threshold,
    DatalakeSerieCouple dsCouple,
    ReceivedData data,
    SensorDataRetriever sensorDataRetriever) {
    super(threshold, data, dsCouple);
    this.sensorDataRetriever = sensorDataRetriever;
  }


  /**
   * Calculates the mean value of a fixed number if sensor datas for alert checking.
   *
   * @param sensorDataInfo The sensor data information.
   * @return An Optional containing the calculated mean value for alert checking.
   */
  @Override
  public Optional<Float> getValueToCheck(SensorDataInfo sensorDataInfo) {
    List<Float> sensorValues = sensorDataRetriever.getLatestSensorValues(
      new ThresholdCacheKey(dsCouple, threshold.getUuid()),
      threshold.getDesiredMeasurements() - 1
    );
    Optional<Float> valueOpt = super.getValueToCheck(sensorDataInfo);
    return valueOpt.map(
      value -> (sensorValues.stream().reduce(0f, Float::sum) + value) / (sensorValues.size() + 1)
    );

  }


}
