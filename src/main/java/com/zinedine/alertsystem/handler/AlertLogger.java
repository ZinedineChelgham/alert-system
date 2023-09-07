package com.zinedine.alertsystem.handler;

import com.zinedine.alertsystem.log.ILogger;
import com.zinedine.alertsystem.model.BoundaryStep;
import com.zinedine.alertsystem.model.Threshold;

/**
 * A class that implements AlertSender to log alerts.
 */
public class AlertLogger implements AlertSender {

  private final boolean silent;

  /**
   * Constructs an instance of AlertLogger with an option to enable or disable silent mode.
   *
   * @param silent Whether the logger should operate in silent mode (no output).
   */
  public AlertLogger(boolean silent) {
    this.silent = silent;
  }

  /**
   * Constructs an instance of AlertLogger with silent mode disabled (output enabled).
   */
  public AlertLogger() {
    this.silent = false;
  }

  /**
   * Sends an alert by logging it, unless in silent mode.
   *
   * @param threshold           The threshold associated with the alert.
   * @param crossedBoundaryStep The crossed boundary step triggering the alert.
   */
  @Override
  public void sendAlert(Threshold threshold, BoundaryStep crossedBoundaryStep) {
    if (!silent) {
      ILogger.info("Triggering alert:\n"
        + threshold + "\n"
        + crossedBoundaryStep);
    }
  }
}
