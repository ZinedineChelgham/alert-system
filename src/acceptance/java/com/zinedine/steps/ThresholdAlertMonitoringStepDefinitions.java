package acceptance.java.com.zinedine.steps;


import acceptance.java.com.zinedine.steps.helper.ThresholdConfigParams;
import com.zinedine.alertsystem.controllers.AlertsControllerTask;
import com.zinedine.alertsystem.mock.MockedSensorDataRetriever;
import com.zinedine.alertsystem.handler.AlertLogger;
import com.zinedine.alertsystem.controllers.SensorDataRetriever;
import com.zinedine.alertsystem.model.ReceivedData;
import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.Threshold;
import com.zinedine.alertsystem.model.ThresholdCacheKey;
import com.zinedine.alertsystem.repositories.Store;
import com.zinedine.alertsystem.repositories.InMemoryStore;
import acceptance.java.com.zinedine.steps.helper.ConfigFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.stream.Collectors;


public class ThresholdAlertMonitoringStepDefinitions
{

  private Threshold threshold;
  private Store store;
  private final AlertLogger alertLogger = mock( AlertLogger.class );
  private final SensorDataRetriever sensorDataRetriever = new MockedSensorDataRetriever();
  private DatalakeSerieCouple dsCouple;
  private ThresholdConfigParams params;



  @And("a threshold with the following configuration linked to {string} from datalake of id {int}")
  public void aThresholdWithTheFollowingConfigurationLinkedToFromDatalakeOfId(
    String serie, int datalakeId, List<Map<String, String>> dataTable
  )
  {
    dsCouple = new DatalakeSerieCouple( datalakeId, serie );
    params = ConfigFactory.createThresholdConfigParams( dataTable.get( 0 ) );
    threshold = params.createThreshold();
    Map<DatalakeSerieCouple, List<Threshold>> storage = new HashMap<>();
    storage.put( dsCouple, Collections.singletonList(threshold) );
    store = new InMemoryStore( storage );
  }


  @When("the following data is saved")
  public void theFollowingDataIsSaved( List<Map<String, String>> dataTable )
  {
    dataTable.forEach(
      stringStringMap ->
        new AlertsControllerTask(
          store,
          sensorDataRetriever,
          alertLogger,
          dsCouple,
          ReceivedData.create( stringStringMap ) )
          .run()
    );
  }


  @Then("the following alert is created in {string} from datalake of id {int}")
  public void theFollowingAlertIsCreatedInFromDatalakeOfId( String arg0, int arg1, List<Map<String, String>> dataTable )
  {
    verify( alertLogger, times( 1 ) ).sendAlert( eq(threshold), any() );
  }

  @Then("the following alerts are created after the third and seventh cross in {string} from datalake of id {int}")
  public void theFollowingAlertsAreCreatedAfterTheThirdAndSeventhCrossInFromDatalakeOfId( String arg0, int arg1,
                                                                                          List<Map<String, String>> dataTable )
  {
    verify( alertLogger, times( 2 ) ).sendAlert(threshold, threshold.getBoundariesSteps().get(0) );
  }

  @When("the following data is saved and returned as a list of {int} measurements")
  public void theFollowingDataIsSavedAndReturnedAsAListOfMeasurements( int nbMeasurements, List<Map<String, String>> dataTable )
  {
    List<Float> values = dataTable.stream()
      .map( stringStringMap -> Float.parseFloat( stringStringMap.get( threshold.getField() ) ) )
      .collect( Collectors.toList() );
    sensorDataRetriever.cache( new ThresholdCacheKey(dsCouple, ""), values );
    new AlertsControllerTask( store,
      sensorDataRetriever, alertLogger,
      dsCouple, ReceivedData.create( dataTable.get( nbMeasurements-1 ) )) // the last one cuz the cache will return the first 3
      .run();
  }

  @Then("there are no alerts in “sensors-alerts” from datalake of id {int}")
  public void thereAreNoAlertsInSensorsAlertsFromDatalakeOfId(int arg0) {
    verify( alertLogger, times( 0 ) ).sendAlert( any(), any() );
  }

  @And("the threshold returns to normal")
  public void theThresholdReturnsToNormal() {
    threshold.getBoundariesSteps().forEach(step -> {
      assertEquals(0, step.getBoundaryStepCrossInfo().getCrossCounter());
      assertEquals(0, step.getBoundaryStepCrossInfo().getReturnToNormalCounter());
      assertFalse(step.getBoundaryStepCrossInfo().isCrossLimitReached());
      assertTrue(step.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());
    });
  }

    @And("the threshold does not return to normal")
    public void theThresholdDoesNotReturnToNormal() {
        threshold.getBoundariesSteps().forEach(step -> {
            assertTrue(step.getBoundaryStepCrossInfo().isCrossLimitReached());
            assertTrue(step.getBoundaryStepCrossInfo().getReturnToNormalCounter() > 0);
            assertTrue(step.getBoundaryStepCrossInfo().getCrossCounter() > 0);
            assertFalse(step.getBoundaryStepCrossInfo().getTimestampOfFirstCrossing().isEmpty());

        });
  }


}


