package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.handler.AlertSender;
import com.zinedine.alertsystem.model.ReceivedData;
import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.Threshold;
import com.zinedine.alertsystem.repositories.Store;

import java.util.List;

/**
 * Represents a task that checks for alerts based on predefined thresholds and sends alerts.
 */
public class AlertsControllerTask implements Runnable
{

  private final Store store;
  private final SensorDataRetriever dataRetriever;
  private final AlertSender alertSender;
  private final DatalakeSerieCouple dsCouple;
  private final ReceivedData data;


  /**
   * Constructs an instance of AlertsControllerTask.
   *
   * @param store         The store used to retrieve alert configurations.
   * @param dataRetriever The retriever used to get sensor data.
   * @param sender   The sender used to send alerts.
   * @param dsCouple      The DatalakeSerieCouple associated with the task.
   * @param data          The BusitData associated with the task.
   */
  public AlertsControllerTask( Store store,
    SensorDataRetriever dataRetriever,
    AlertSender sender,
    DatalakeSerieCouple dsCouple,
    ReceivedData data )
  {
    this.store = store;
    this.dataRetriever = dataRetriever;
    this.alertSender = sender;
    this.dsCouple = dsCouple;
    this.data = data;
  }



  /**
   * Checks for alerts based on predefined thresholds and sends alerts.
   */
  @Override
  public void run()
  {
    List<Threshold> thresholds = store.findAllAlertConfigFor( dsCouple );
    thresholds.forEach(threshold -> {
      AlertChecker checker = AlertCheckerFactory.createAlertChecker(
        threshold, dsCouple, dataRetriever, data
      );
      checker.getBoundariesStepsToAlert().forEach(step -> {
          alertSender.sendAlert( threshold, step );
        });
    } );
  }

}
