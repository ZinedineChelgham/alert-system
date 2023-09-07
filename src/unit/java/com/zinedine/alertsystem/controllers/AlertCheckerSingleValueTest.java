package java.com.zinedine.alertsystem.controllers;

import com.zinedine.alertsystem.controllers.AlertChecker;
import com.zinedine.alertsystem.controllers.AlertCheckerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import com.zinedine.alertsystem.model.*;

class AlertCheckerSingleValueTest {

  private AlertChecker alertChecker;
  private BoundaryStep between10and50;
  private BoundaryStep greaterThan10;
  private DatalakeSerieCouple dsCouple;
  private ReceivedData data;
  private final String field = "testField";
  private final SensorDataInfo sensorDataInfo = new SensorDataInfo(null, null, field);

  private Threshold.Builder thresholdBase(List<BoundaryStep> boundarySteps) {
    return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl("uuid", "timestamp"))
      .withYthresholdDataInfo(sensorDataInfo)
      .withDesiredMeasurements(1)
      .withBoundariesStep(boundarySteps);
  }



  @BeforeEach
  void setUp() {
    between10and50 = new BoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.OPEN, 10, 50),
      1,
      BoundaryStep.SeverityType.LOW
    );
    greaterThan10 = new BoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10),
      2,
      BoundaryStep.SeverityType.HIGH
    );
    dsCouple = new DatalakeSerieCouple(0, "testSerie");
  }


  @Test
  void shouldHave0BoundariesToAlert() {
    Threshold thresh = thresholdBase(
      Arrays.asList(between10and50, greaterThan10)
    ).build();
    data = ReceivedData.create(Collections.singletonMap(field, "5f"));
    alertChecker =  AlertCheckerFactory.createAlertChecker(
      thresh,
      dsCouple,
      null, // Not needed for this class
      data
    );

    List<BoundaryStep> toAlert = alertChecker.getBoundariesStepsToAlert();
    assertEquals(0, toAlert.size());

  }


  @Test
  void shouldHave1BoundaryToAlert() {
    Threshold thresh = thresholdBase(
      Arrays.asList(between10and50, greaterThan10)
    ).build();
    data = ReceivedData.create(Collections.singletonMap(field, "15f"));
    alertChecker = AlertCheckerFactory.createAlertChecker(
      thresh,
      dsCouple,
      null, // Not needed for this class
      data
    );

    List<BoundaryStep> toAlert = alertChecker.getBoundariesStepsToAlert();
    assertEquals(1, toAlert.size());
  }


  @Test
  void shouldHave2BoundariesToAlert() {
    BoundaryStep greaterThan10WithMaxCrossLimit1 = new BoundaryStep(
      RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10),
      1
    );
    Threshold thresh = thresholdBase(
      Arrays.asList(between10and50, greaterThan10WithMaxCrossLimit1)
    ).build();

    data = ReceivedData.create(Collections.singletonMap(field, "20f"));
    alertChecker = AlertCheckerFactory.createAlertChecker(
      thresh,
      dsCouple,
      null, // Not needed for this class
      data
    );
    List<BoundaryStep> toAlert = alertChecker.getBoundariesStepsToAlert();
    assertEquals(2, toAlert.size());
  }


  @Test
  void shouldReturnNaNWhenNoSensorValuesToCheck() {
    Threshold thresh = thresholdBase(
      Arrays.asList(between10and50, greaterThan10)
    ).build();
    data = ReceivedData.create(Collections.singletonMap(field, "NaN"));
    alertChecker = AlertCheckerFactory.createAlertChecker(
      thresh,
      dsCouple,
      null, // Not needed for this class
      data
    );
    Optional<Float> res = alertChecker.getValueToCheck(sensorDataInfo);
    assertFalse(res.isPresent());
  }

  @Test
  void shouldReturn34ToCheck() {
    Threshold thresh = thresholdBase(
      Arrays.asList(between10and50, greaterThan10)
    ).build();
    data = ReceivedData.create(Collections.singletonMap(field, "34f"));
    alertChecker = AlertCheckerFactory.createAlertChecker(
      thresh,
      dsCouple,
      null, // Not needed for this class
      data
    );
    Optional<Float> res = alertChecker.getValueToCheck(sensorDataInfo);
    assertTrue(res.isPresent());
    assertEquals(34f, res.get());
  }



}
