package java.com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.controllers.*;
import com.zinedine.alertsystem.mock.MockedSensorDataRetriever;
import com.zinedine.alertsystem.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


class AlertCheckerMeanValuesTest
{
  private AlertChecker alertChecker;
  private DatalakeSerieCouple dsCouple;
  private final String field = "testField";
  private final String thresholdUuid = "uuid";
  private final SensorDataInfo sensorDataInfo = new SensorDataInfo(null, null, field);
  private SensorDataRetriever sensorDataRetriever;

  private Threshold.Builder thresholdBase(List<BoundaryStep> boundarySteps) {
    return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl(thresholdUuid, "timestamp"))
      .withYthresholdDataInfo(sensorDataInfo)
      .withDesiredMeasurements(1)
      .withBoundariesStep(boundarySteps);
  }

  @BeforeEach
  void setUp()
  {
    MockitoAnnotations.openMocks( this );
    dsCouple = new DatalakeSerieCouple( 0, "test" );
    sensorDataRetriever = new MockedSensorDataRetriever();
  }

  @Test
  void shouldCalculateAverageForMultipleSensorValues()
  {
    List<Float> sensorValues = Arrays.asList( 10f, 20f, 30f);
    sensorDataRetriever.cache( new ThresholdCacheKey(dsCouple, thresholdUuid), sensorValues );
    alertChecker = new AlertCheckerMeanValues(
      thresholdBase( Collections.emptyList() ).withDesiredMeasurements(4).build(),
      dsCouple,
      ReceivedData.create( Collections.singletonMap( field, "40f" ) ),
      sensorDataRetriever
    );
    Optional<Float> result = alertChecker.getValueToCheck(sensorDataInfo);
    assertEquals( 25f, result.get()); // Allowing a small delta for floating-point precision
  }

  @Test
  void shouldReturnTheActualValue()
  {
    List<Float> sensorValues = Collections.emptyList();
    sensorDataRetriever.cache( new ThresholdCacheKey(dsCouple, thresholdUuid), sensorValues );
    alertChecker = new AlertCheckerMeanValues(
      thresholdBase( Collections.emptyList() ).withDesiredMeasurements(1).build(),
      dsCouple,
      ReceivedData.create( Collections.singletonMap( field, "40f" ) ),
      sensorDataRetriever
    );
    Optional<Float> result = alertChecker.getValueToCheck(sensorDataInfo);
    assertEquals( 40f, result.get()); // Allowing a small delta for floating-point precision
  }

  @Test
  void shouldReturnTheActualValue2()
  {
    List<Float> sensorValues = Collections.emptyList();
    sensorDataRetriever.cache( new ThresholdCacheKey(dsCouple, thresholdUuid), sensorValues );
    alertChecker = new AlertCheckerMeanValues(
      thresholdBase( Collections.emptyList() ).withDesiredMeasurements(2).build(),
      dsCouple,
      ReceivedData.create( Collections.singletonMap( field, "40f" ) ),
      sensorDataRetriever
    );
    Optional<Float> result = alertChecker.getValueToCheck(sensorDataInfo);
    assertEquals( 40f, result.get(), 0.001 ); // Allowing a small delta for floating-point precision
  }


}

