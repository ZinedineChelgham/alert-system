package java.com.zinedine.alertsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.zinedine.alertsystem.model.*;

public class BoundaryStepCrossInfoTest {
  private BoundaryStepCrossInfo boundaryCrossInfo;

  @BeforeEach
  public void setUp() {
    boundaryCrossInfo = new BoundaryStepCrossInfo(3); // Example value for fixedReturnToNormal
  }

  @Test
  public void shouldIncrementConsecutiveCrossedCount() {
    boundaryCrossInfo.incrementCrossCounter();
    assertEquals(1, boundaryCrossInfo.getCrossCounter());
  }

  @Test
  public void shouldIncrementReturnToNormalCount() {
    boundaryCrossInfo.incrementReturnToNormalCounter();
    assertEquals(1, boundaryCrossInfo.getReturnToNormalCounter());
  }

  @Test
  public void shouldResetConsecutiveCrossedCount() {
    boundaryCrossInfo.incrementCrossCounter();
    boundaryCrossInfo.resetCrossedCounter();
    assertEquals(0, boundaryCrossInfo.getCrossCounter());
  }

  @Test
  public void shouldReturnToNormal() {
    boundaryCrossInfo.incrementReturnToNormalCounter();
    boundaryCrossInfo.incrementCrossCounter();
    boundaryCrossInfo.returnToNormal();
    assertEquals(0, boundaryCrossInfo.getReturnToNormalCounter());
    assertEquals(0, boundaryCrossInfo.getCrossCounter());
    assertEquals("", boundaryCrossInfo.getTimestampOfFirstCrossing());
  }

  @Test
  public void shouldReturnToNormalAfter3rdIncrement() {
    boundaryCrossInfo.incrementReturnToNormalCounter();
    assertFalse(boundaryCrossInfo.shouldReturnToNormal());

    boundaryCrossInfo.incrementReturnToNormalCounter();
    boundaryCrossInfo.incrementReturnToNormalCounter();
    assertTrue(boundaryCrossInfo.shouldReturnToNormal());
  }

  @Test
  public void shouldAlert() {
    assertFalse(boundaryCrossInfo.shouldAlert());

    boundaryCrossInfo.incrementCrossCounter();
    assertTrue(boundaryCrossInfo.shouldAlert());
  }

  @Test
  public void shouldReturnTrueIsCrossLimitExceeded() {
    assertFalse(boundaryCrossInfo.isCrossLimitExceeded());

    boundaryCrossInfo.incrementCrossCounter();
    boundaryCrossInfo.incrementCrossCounter();
    assertTrue(boundaryCrossInfo.isCrossLimitExceeded());
  }

  @Test
  public void shouldReturnFalseIsCrossLimitExceeded() {
    assertFalse(boundaryCrossInfo.isCrossLimitExceeded());

    boundaryCrossInfo.incrementCrossCounter();
    assertFalse(boundaryCrossInfo.isCrossLimitExceeded());
  }

  @Test
  public void shouldResetReturnToNormalCount() {
    boundaryCrossInfo.incrementReturnToNormalCounter();
    boundaryCrossInfo.resetReturnToNormalCounter();
    assertEquals(0, boundaryCrossInfo.getReturnToNormalCounter());
  }

  @Test
  public void shouldDecrementConsecutiveCrossedCount() {
    boundaryCrossInfo.incrementCrossCounter();
    boundaryCrossInfo.decrementCrossCounter();
    assertEquals(0, boundaryCrossInfo.getCrossCounter());

    // Ensure count doesn't go below 0
    boundaryCrossInfo.decrementCrossCounter();
    assertEquals(0, boundaryCrossInfo.getCrossCounter());
  }
}
