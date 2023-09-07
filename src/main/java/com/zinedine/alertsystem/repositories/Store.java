package com.zinedine.alertsystem.repositories;


import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.Threshold;

import java.util.List;
import java.util.Optional;


public interface Store {

  /**
   * Retrieves all alert configurations for a given data lake series couple.
   *
   * @param dsCouple The data lake series couple to retrieve alert configurations for.
   * @return A list of Threshold objects representing the alert configurations.
   */
  List<Threshold> findAllAlertConfigFor(DatalakeSerieCouple dsCouple);

  /**
   * Saves a list of threshold configurations for a given data lake series couple.
   *
   * @param dsCouple   The data lake series couple associated with the thresholds.
   * @param thresholds The list of Threshold objects representing the thresholds to save.
   */
  void saveThresholds(DatalakeSerieCouple dsCouple, List<Threshold> thresholds);

  /**
   * Deletes a threshold configuration for a given data lake series couple and UUID.
   *
   * @param dsCouple The data lake series couple associated with the threshold to delete.
   * @param uuid     The UUID of the threshold to delete.
   */
  void deleteThreshold(DatalakeSerieCouple dsCouple, String uuid);

  /**
   * Retrieves a specific threshold configuration for a given data lake series couple and UUID.
   *
   * @param dsCouple The data lake series couple associated with the threshold.
   * @param uuid     The UUID of the threshold to retrieve.
   * @return An Optional containing the retrieved Threshold object, if found.
   */
  Optional<Threshold> findThreshold(DatalakeSerieCouple dsCouple, String uuid);

  /**
   * Updates a threshold configuration for a given data lake series couple.
   *
   * @param dsCouple  The data lake series couple associated with the threshold to update.
   * @param threshold The updated Threshold object to save.
   */
  void updateThreshold(DatalakeSerieCouple dsCouple, Threshold threshold);

  /**
   * Deletes a threshold configuration based on its UUID.
   *
   * @param uuid The UUID of the threshold to delete.
   */
  void deleteThreshold(String uuid);

  /**
   * Retrieves a specific threshold configuration based on its UUID.
   *
   * @param uuid The UUID of the threshold to retrieve.
   * @return An Optional containing the retrieved Threshold object, if found.
   */
  Optional<Threshold> findThreshold(String uuid);

  /**
   * Deletes all threshold configurations.
   */
  void deleteAllThresholds();

}
