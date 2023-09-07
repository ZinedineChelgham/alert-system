package com.zinedine.alertsystem.controllers;

import com.zinedine.alertsystem.model.ThresholdCacheKey;

import java.util.List;

/**
 * An interface for retrieving sensor data values.
 */
public interface SensorDataRetriever {

  /**
   * Retrieves the latest sensor values for a given threshold cache key.
   *
   * @param key                The threshold cache key associated with the sensor values.
   * @param numberOfLastValues The number of latest values to retrieve.
   * @return A list of the latest sensor values.
   */
  List<Float> getLatestSensorValues(ThresholdCacheKey key, int numberOfLastValues);

  /**
   * Caches a set of sensor values for a given threshold cache key.
   *
   * @param dsCouple      The threshold cache key associated with the sensor values.
   * @param valuesToStore The list of sensor values to store in the cache.
   * @return The updated SensorDataRetriever instance with cached values.
   */
  SensorDataRetriever cache(ThresholdCacheKey dsCouple, List<Float> valuesToStore);
}
