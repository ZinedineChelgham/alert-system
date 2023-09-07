package com.zinedine.alertsystem.handler;

import com.zinedine.alertsystem.model.BoundaryStep;
import com.zinedine.alertsystem.model.Threshold;


/**
 * An interface for sending alerts by a special mean (sms, mail, log, ...) when a boundary step is crossed.
 */
public interface AlertSender {


  /**
   * Sends an alert based on a crossed boundary step.
   *
   * @param threshold           The threshold associated with the alert.
   * @param crossedBoundaryStep The crossed boundary step triggering the alert.
   */
  void sendAlert(Threshold threshold, BoundaryStep crossedBoundaryStep);
}
