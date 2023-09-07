package com.zinedine.alertsystem.repositories;

import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.Threshold;

import java.util.*;

/**
 * An implementation of the Store interface that stores threshold configurations in memory.
 */
public class InMemoryStore implements Store {

  private final Map<DatalakeSerieCouple, List<Threshold>> thresholds;

  /**
   * Constructs an instance of InMemoryStore with an initial set of thresholds.
   *
   * @param thresholds The initial set of thresholds to be stored.
   */
  public InMemoryStore(Map<DatalakeSerieCouple, List<Threshold>> thresholds) {
    this.thresholds = thresholds;
  }

  @Override
  public List<Threshold> findAllAlertConfigFor(DatalakeSerieCouple dsCouple) {
    return thresholds.getOrDefault(dsCouple, new ArrayList<>());
  }

  @Override
  public void saveThresholds(DatalakeSerieCouple dsCouple, List<Threshold> thresholds) {
    this.thresholds.put(dsCouple, thresholds);
  }

  @Override
  public void deleteThreshold(DatalakeSerieCouple dsCouple, String uuid) {
    thresholds.computeIfPresent(dsCouple, (key, value) -> {
      value.removeIf(threshold -> threshold.getBusitData().getUuid().equals(uuid));
      return value;
    });
  }

  @Override
  public Optional<Threshold> findThreshold(DatalakeSerieCouple dsCouple, String uuid) {
    return thresholds.getOrDefault(dsCouple, Collections.emptyList())
      .stream()
      .filter(threshold -> threshold.getBusitData().getUuid().equals(uuid))
      .findFirst();
  }

  @Override
  public void updateThreshold(DatalakeSerieCouple dsCouple, Threshold threshold) {
    List<Threshold> existingThresholds = thresholds.getOrDefault(dsCouple, new ArrayList<>());
    for (int i = 0; i < existingThresholds.size(); i++) {
      if (existingThresholds.get(i).getUuid().equals(threshold.getUuid())) {
        existingThresholds.set(i, threshold);
        break;
      }
    }
  }

  @Override
  public void deleteThreshold(String uuid) {
    thresholds.forEach((key, value) -> value.removeIf(threshold -> threshold.getUuid().equals(uuid)));
  }

  @Override
  public Optional<Threshold> findThreshold(String uuid) {
    return thresholds.values()
      .stream()
      .flatMap(List::stream)
      .filter(threshold -> threshold.getUuid().equals(uuid))
      .findFirst();
  }

  @Override
  public void deleteAllThresholds() {
    thresholds.clear();
  }

 /* @Override
  public void deleteAllThresholdsForSensor(String sensorId) {
    thresholds.values().forEach(list -> list.removeIf(threshold -> threshold.getSensorId().equals(sensorId)));
  }*/

  /*@Override
  public List<BoundaryStep> findAllThresholdsForSensor(String sensorId) {
    List<BoundaryStep> result = new ArrayList<>();
    thresholds.values().forEach(result::addAll);
    result.removeIf(threshold -> !threshold.getSensorId().equals(sensorId));
    return result;
  }*/
}
