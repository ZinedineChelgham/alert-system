package java.com.zinedine.alertsystem.utils;

import com.zinedine.alertsystem.utils.RangeConverter;
import com.google.common.collect.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zinedine.alertsystem.model.*;

import static org.junit.jupiter.api.Assertions.*;

class RangeConverterTest {

  private Range<Float> range;
  private RangeConverter rangeConverter;

  @BeforeEach
  void setUp() {
    range = RangeFactory.createRange(RangeFactory.RangeType.GREATER_THAN, 10);
    rangeConverter = new RangeConverter();
  }

  @Test
  public void shouldConvertToByteAndGoBackToRange() {
    byte[] byteArray = rangeConverter.convertToDatabaseColumn(range);
    Range<Float> convertedRange = rangeConverter.convertToEntityAttribute(byteArray);
    assertEquals(range, convertedRange);
  }

  @Test
  public void shouldHandleNullRange() {
    byte[] nullByteArray = rangeConverter.convertToDatabaseColumn(null);
    Range<Float> nullRange = rangeConverter.convertToEntityAttribute(nullByteArray);
    assertNull(nullRange);
  }


}
