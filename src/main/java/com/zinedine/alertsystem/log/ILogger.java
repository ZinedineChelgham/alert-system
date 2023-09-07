package com.zinedine.alertsystem.log;

/**
 * An interface for logging information.
 */
public interface ILogger {

  /**
   * Logs an informational message.
   *
   * @param message The message to be logged.
   */
  static void info(String message) {
    System.out.println(message);
  }
}
