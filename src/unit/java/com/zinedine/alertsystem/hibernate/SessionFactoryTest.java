package java.com.zinedine.alertsystem.hibernate;

import com.zinedine.alertsystem.utils.HibernateUtil;
import com.zinedine.alertsystem.model.*;
import com.google.common.collect.Range;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Disabled // TODO: Need docker initialization setup to run this test
public class SessionFactoryTest {
  private static SessionFactory sessionFactory;
  private static Session session;

  @BeforeAll
  static void setUpBeforeClass() {
    sessionFactory = HibernateUtil.getSessionFactory();
  }

  @BeforeEach
  void setup() {
    session = sessionFactory.getCurrentSession();
    session.beginTransaction();
  }

  @AfterEach
  void tearDown() {
    session.getTransaction().rollback();
  }

  @AfterAll
  static void afterAll() {
    sessionFactory.close();

  }

  @Test
  public void shouldSaveEntity() throws HibernateException {
    try {
      Threshold threshold = new Threshold.Builder()
        .withBoundariesStep(Collections.singletonList(
          new BoundaryStep(
            RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 1),
            1, BoundaryStep.SeverityType.LOW)
        ))
        .withDesiredMeasurements(1)
        .withReceiveData(new ReceivedDataImpl("uuid", "tt"))
        .withYthresholdDataInfo(new SensorDataInfo("yidfield", "yidvalue", "yfield"))
        .withXthresholdDataInfo(new SensorDataInfo("xidfield", "xidvalue", "xfield"))
        .build();

      session.persist(threshold);

      Threshold retrievedThreshold = getThresholdFromDB();

      assertNotNull(retrievedThreshold);
      assertEquals(1, retrievedThreshold.getBoundariesSteps().size());
      assertEquals(1, retrievedThreshold.getDesiredMeasurements());
      assertEquals("uuid", retrievedThreshold.getUuid());
      assertEquals(
        new BoundaryStep(
          RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 1), 1, BoundaryStep.SeverityType.LOW),
        retrievedThreshold.getBoundariesSteps().get(0));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void shouldSaveCustomDimBoundaryStep() {
    Range<Float> range = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10);
    Range<Float> customDim = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 20);
    int maxCrossLimit = 1;
    BoundaryStep.SeverityType severity = BoundaryStep.SeverityType.HIGH;
    BoundaryStepCrossInfo boundaryStepCrossInfo = new BoundaryStepCrossInfo(1);
    CustomDimBoundaryStep boundaryStep = new CustomDimBoundaryStep(range, maxCrossLimit, severity, customDim, boundaryStepCrossInfo);
    Threshold threshold = new Threshold.Builder()
      .withBoundariesStep(Collections.singletonList(boundaryStep))
      .withDesiredMeasurements(1)
      .withReceiveData(new ReceivedDataImpl("uuidddggg", "ttttgggg"))
      .withYthresholdDataInfo(new SensorDataInfo("yidfielddddg", "yidvalueeeeg", "yfielddddg"))
      .withXthresholdDataInfo(new SensorDataInfo("xidfielddddg", "xidvalueeeeeg", "xfielddddg"))
      .build();

    session.persist(threshold);

    Threshold retrievedThreshold = getThresholdFromDB();
    assertNotNull(retrievedThreshold);
    assertEquals(1, retrievedThreshold.getBoundariesSteps().size());

  }

  // Retrieve the first threshold from the database
  private static Threshold getThresholdFromDB() {
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<Threshold> criteriaQuery = criteriaBuilder.createQuery(Threshold.class);
    Root<Threshold> root = criteriaQuery.from(Threshold.class);

    criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get("id")));
    List<Threshold> resultList = session.createQuery(criteriaQuery).setMaxResults(1).getResultList();
    return resultList.get(0);
  }


}
