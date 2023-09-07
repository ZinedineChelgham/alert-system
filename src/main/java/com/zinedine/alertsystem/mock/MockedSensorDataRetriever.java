package com.zinedine.alertsystem.mock;


import com.zinedine.alertsystem.controllers.SensorDataRetriever;
import com.zinedine.alertsystem.model.ThresholdCacheKey;

import java.util.*;


public class MockedSensorDataRetriever implements SensorDataRetriever
{

  private final Map<ThresholdCacheKey, List<Float>> cache;


  public MockedSensorDataRetriever()
  {
    this.cache = new HashMap<>();
  }


  @Override
  public List<Float> getLatestSensorValues( ThresholdCacheKey key, int numberOfLastValues )
  {
    if(cache.get( key ).isEmpty()) { // Calling subList on an empty list throws an exception
      return Collections.emptyList();
    }
    return cache.get( key ).subList( 0, numberOfLastValues );
  }


  @Override
  public SensorDataRetriever cache( ThresholdCacheKey cacheKey, List<Float> valuesToStore)
  {
    List<Float> values = cache.get( cacheKey );
    if ( Objects.isNull( values ) )
    {
      cache.put( cacheKey, valuesToStore);
    }
    //cache.put( cacheKey, values );
    return this;
  }
}
