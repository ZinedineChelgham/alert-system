package com.zinedine.alertsystem.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;


/**
 * Represents a cache key for storing and retrieving Threshold-related data in a cache.
 */
public class ThresholdCacheKey {
  private Pair<DatalakeSerieCouple, String> cacheKey;


  /**
   * Constructs a ThresholdCacheKey instance with the given DatalakeSerieCouple and threshold ID.
   *
   * @param dsCouple    The DatalakeSerieCouple associated with the cache key.
   * @param thresholdId The threshold ID associated with the cache key.
   */
  public ThresholdCacheKey(DatalakeSerieCouple dsCouple, String thresholdId) {
    this.cacheKey = Pair.of(dsCouple, thresholdId);
  }


  public DatalakeSerieCouple getDsCouple() {
    return cacheKey.getLeft();
  }


  public String getThresholdId() {
    return cacheKey.getRight();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ThresholdCacheKey that = (ThresholdCacheKey) o;

    if (!getDsCouple().equals(that.getDsCouple())) {
      return false;
    }
    return getThresholdId().equals(that.getThresholdId());
  }

  public void setCacheKey(Pair<DatalakeSerieCouple, String> cacheKey) {
    this.cacheKey = cacheKey;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDsCouple(), getThresholdId());
  }
}
