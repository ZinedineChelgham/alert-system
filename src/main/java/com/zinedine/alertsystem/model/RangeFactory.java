package com.zinedine.alertsystem.model;

import com.google.common.collect.Range;

/**
 * A utility class for creating different types of Guava Range objects for float values.
 */
public class RangeFactory {

  /**
   * Enumeration representing the various types of ranges that can be created using the factory methods.
   */
  public enum RangeType {
    OPEN,             // (a, b)
    CLOSED,           // [a, b]
    OPEN_CLOSED,      // (a, b]
    CLOSED_OPEN,      // [a, b)
    GREATER_THAN,     // (a, +∞)
    AT_LEAST,         // [a, +∞)
    LESS_THAN,        // (-∞, b)
    AT_MOST,          // (-∞, b]
    ALL               // (-∞, +∞)
  }


  /**
   * Creates a Guava Range object for a single threshold value.
   *
   * @param type      The type of range to create.
   * @param threshold The threshold value.
   * @return A Guava Range object representing the specified range type and threshold.
   */
  public static Range<Float> createRange(RangeType type, float threshold) {
    return createSimpleRange(type, threshold);
  }


  /**
   * Creates a Guava Range object for a range defined by minimum and maximum threshold values.
   *
   * @param type         The type of range to create.
   * @param thresholdMin The minimum threshold value.
   * @param thresholdMax The maximum threshold value.
   * @return A Guava Range object representing the specified range type and bounded by the threshold values.
   */
  public static Range<Float> createRange(RangeType type, float thresholdMin, float thresholdMax) {
    return createBoundedRange(type, thresholdMin, thresholdMax);
  }


  /**
   * (a..b)	{x | a < x < b}	open
   * [a..b]	{x | a <= x <= b}	closed
   * (a..b]	{x | a < x <= b}	openClosed
   * [a..b)	{x | a <= x < b}	closedOpen
   * (-∞..+∞)	{x}	all
   */
  private static Range<Float> createBoundedRange(RangeType rangeType, float min, float max) {
    if (rangeType == RangeType.OPEN) {
      return Range.open(min, max);
    }
    return Range.all();
  }

  /**
   * (a..+∞)	{x | x > a}	greaterThan
   * [a..+∞)	{x | x >= a} atLeast
   * (-∞..b)	{x | x < b}	lessThan
   * (-∞..b]	{x | x <= b} atMost
   */
  private static Range<Float> createSimpleRange(RangeType rangeType, float threshold) {
    switch (rangeType) {
      case GREATER_THAN:
        return Range.greaterThan(threshold);
      case LESS_THAN:
        return Range.lessThan(threshold);
      case AT_MOST:
        return Range.atMost(threshold);
      case AT_LEAST:
        return Range.atLeast(threshold);
      default:
        return Range.all();
    }
  }

}

