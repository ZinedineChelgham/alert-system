package acceptance.java.com.zinedine.steps;


import acceptance.java.com.zinedine.steps.helper.ThresholdConfigParams;
import com.zinedine.alertsystem.log.ILogger;
import com.zinedine.alertsystem.model.DatalakeSerieCouple;
import com.zinedine.alertsystem.model.ReceivedDataImpl;
import com.zinedine.alertsystem.model.Threshold;
import com.zinedine.alertsystem.repositories.InMemoryStore;
import com.zinedine.alertsystem.repositories.Store;
import acceptance.java.com.zinedine.steps.helper.ConfigFactory;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ThresholdConfigurationManagementStepDefinitions
{

  private Threshold threshold;
  private  Store store;
  private DatalakeSerieCouple dsCouple;
  private ThresholdConfigParams thresholdConfigParams;


  @Given("a serie {string} from datalake of id {int}")
  public void aSerieSensorsFromDatalakeOfId(String serie, int id )
  {
    dsCouple = new DatalakeSerieCouple( id, serie );
  }


  @And("a threshold with the following configuration")
  public void aThresholdWithTheFollowingConfiguration()
  {
    ILogger.info( "aThresholdWithTheFollowingConfiguration" );
  }


  @And("I am logged in as “Henry”")
  public void iAmLoggedInAsHenry()
  {
    ILogger.info( "iAmLoggedInAsHenry" );
  }


  @When("I create the following threshold")
  public void iCreateTheFollowingThreshold( List<Map<String, String>> dataTable )
  {
    Map<String, String> params = dataTable.get( 0 );
    ThresholdConfigParams config = ConfigFactory.createThresholdConfigParams( params);
    threshold = config.createThreshold();
    store = new InMemoryStore(
      Collections.singletonMap( dsCouple, Collections.singletonList(threshold) )
    );
  }


  @Then("the previous threshold has been saved")
  public void thePreviousThresholdHasBeenSaved()
  {
    List<Threshold> thresholds = store.findAllAlertConfigFor(dsCouple);
    assertEquals( 1, thresholds.size() );
    assertEquals(threshold, thresholds.get( 0 ) );
  }


  @Given("a threshold of uuid {string}")
  public void aThresholdOfUuidToto( String uuid )
  {
    threshold = new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl(uuid, "timestamp"))
      .build();
    List<Threshold> thresholds = new ArrayList<>(); thresholds.add(threshold);
    store = new InMemoryStore(
      Collections.singletonMap( dsCouple, thresholds)
    );
    assertTrue( store.findAllAlertConfigFor(dsCouple).contains(threshold) );
  }


  @And("I am the user {string}")
  public void iAmTheUserMarine( String user )
  {
    ILogger.info( "iAmTheUserMarine" );
  }


  @When("I delete the threshold of uuid {string}")
  public void iDeleteTheThresholdOfUuidToto( String uuid )
  {
    store.deleteThreshold( uuid );
  }


  @Then("the threshold of uuid {string} has been deleted")
  public void theThresholdOfUuidTotoHasBeenDeleted( String uuid )
  {
    Optional<Threshold> thresholds = store.findThreshold( uuid );
    assertFalse( thresholds.isPresent() );
  }
}

