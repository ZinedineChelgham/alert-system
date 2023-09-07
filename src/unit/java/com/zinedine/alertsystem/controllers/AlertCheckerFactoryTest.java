package java.com.zinedine.alertsystem.controllers;

import com.zinedine.alertsystem.controllers.AlertChecker;
import com.zinedine.alertsystem.controllers.AlertCheckerFactory;
import com.zinedine.alertsystem.controllers.AlertCheckerMeanValues;
import com.zinedine.alertsystem.controllers.AlertCheckerSingleValue;
import com.zinedine.alertsystem.model.Threshold;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertCheckerFactoryTest {

  private AlertChecker alertChecker;

  @Test
  void shouldCreateAlertCheckerSingleValue() {
    alertChecker = AlertCheckerFactory.createAlertChecker(
      new Threshold.Builder()
        .withDesiredMeasurements(1)
        .build(),
      null,
      null,
      null
    );
    assertTrue(alertChecker instanceof AlertCheckerSingleValue);
  }

  @Test
  void shouldCreateAlertCheckerMeanValue() {
    alertChecker = AlertCheckerFactory.createAlertChecker(
      new Threshold.Builder()
        .withDesiredMeasurements(2)
        .build(),
      null,
      null,
      null
    );
    assertTrue(alertChecker instanceof AlertCheckerMeanValues);
  }
}
