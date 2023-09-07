package com.zinedine.alertsystem.controllers;


import com.zinedine.alertsystem.model.ReceivedData;
import com.zinedine.alertsystem.model.SensorDataInfo;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class ReceivedDataParser implements ReceivedData
{

  private final Map<String, String> data;


  public ReceivedDataParser(Map<String, String> data )
  {
    this.data = data;
  }


  @Override
  public Optional<Float> getThresholdRelatedData( SensorDataInfo dataInfo )
  {

    String fieldValue = data.get( dataInfo.getField() );

    if ( Objects.isNull( fieldValue )
      || fieldValue.equalsIgnoreCase("nan") )
    {
      return Optional.empty();
    }
    try
    {
      Float parsedValue = Float.valueOf( fieldValue );
      return Optional.of( parsedValue );
    }
    catch ( NumberFormatException exception )
    {
      return Optional.empty();
    }
  }
}
