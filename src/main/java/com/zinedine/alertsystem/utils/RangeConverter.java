package com.zinedine.alertsystem.utils;

import com.google.common.collect.Range;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;

/**
 * A converter for converting a Guava Range object to a byte array and vice versa for database storage.
 */
@Converter
public class RangeConverter implements AttributeConverter<Range<Float>, byte[]> {

  /**
   * Converts a Guava Range object to a byte array for database storage.
   *
   * @param floatRange The Guava Range object to be converted.
   * @return The byte array representation of the Guava Range.
   */
  public byte[] convertToDatabaseColumn(Range<Float> floatRange) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
      objectOutputStream.writeObject(floatRange);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      // Handle exception
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Converts a byte array back to a Guava Range object during entity attribute retrieval.
   *
   * @param bytes The byte array representation of the Guava Range.
   * @return The Guava Range object extracted from the byte array.
   */
  @Override
  public Range<Float> convertToEntityAttribute(byte[] bytes) {
    if (bytes == null) {
      return null;
    }

    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
         ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
      return (Range<Float>) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      // Handle exceptions
      e.printStackTrace();
      return null;
    }
  }
}
