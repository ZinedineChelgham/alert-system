package java.com.zinedine.alertsystem.controllers;



import com.zinedine.alertsystem.controllers.AlertsControllerTask;
import com.zinedine.alertsystem.handler.AlertSender;
import com.zinedine.alertsystem.model.*;
import com.zinedine.alertsystem.repositories.InMemoryStore;
import com.zinedine.alertsystem.repositories.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class AlertsControllerTaskTest
{

  private AlertsControllerTask alertsControllerTask;
  private  DatalakeSerieCouple dsCouple;
  private Threshold threshold;
  private Store store;
  @Mock
  private AlertSender mockAlertSender;

  private Threshold.Builder thresholdBase(List<BoundaryStep> boundarySteps) {
    return new Threshold.Builder()
      .withReceiveData(new ReceivedDataImpl("uuid", "timestamp"))
      .withYthresholdDataInfo(new SensorDataInfo(null, null, "testField"))
      .withDesiredMeasurements(1)
      .withBoundariesStep(boundarySteps);
  }

  @BeforeEach
  public void setup()
  {
    MockitoAnnotations.openMocks( this );
    dsCouple = new DatalakeSerieCouple( 0, "timestamp" );

  }


  @Test
  public void shouldSends1Alert()
  {
    BoundaryStep boundaryStep = new BoundaryStep(
      RangeFactory.createRange( RangeFactory.RangeType.GREATER_THAN, 10 ),
      1
    );
    List<BoundaryStep> mockBoundarySteps = Collections.singletonList(boundaryStep);
    threshold = thresholdBase(mockBoundarySteps).build();
    store = new InMemoryStore(
      Collections.singletonMap(dsCouple, Collections.singletonList(threshold))
    );
    alertsControllerTask = new AlertsControllerTask(
      store,
      null, // I dont like this
      mockAlertSender,
      dsCouple,
      ReceivedData.create( Collections.singletonMap( "testField", "11f" ))
    );
    alertsControllerTask.run();
    verify( mockAlertSender, times( 1 ) )
      .sendAlert( eq(threshold), eq(boundaryStep) );

  }


  @Test
  public void shouldSends2Alert()
  {
    BoundaryStep boundaryStep1 = new BoundaryStep(
      RangeFactory.createRange( RangeFactory.RangeType.GREATER_THAN, 10 ),
      1
    );
    BoundaryStep boundaryStep2 = new BoundaryStep(
      RangeFactory.createRange( RangeFactory.RangeType.GREATER_THAN, 20 ),
      1
    );
    List<BoundaryStep> mockBoundarySteps = Arrays.asList(boundaryStep1, boundaryStep2);
    threshold = thresholdBase(mockBoundarySteps).build();
    store = new InMemoryStore(
      Collections.singletonMap(dsCouple, Collections.singletonList(threshold))
    );
    alertsControllerTask = new AlertsControllerTask(
      store,
      null,
      mockAlertSender,
      dsCouple,
      ReceivedData.create( Collections.singletonMap( "testField", "50f" ))
    );
    alertsControllerTask.run();
    mockBoundarySteps.forEach(b -> {
      verify( mockAlertSender, times( 1 ) ).sendAlert( eq(threshold), eq( b ) );
    } );
  }
}
