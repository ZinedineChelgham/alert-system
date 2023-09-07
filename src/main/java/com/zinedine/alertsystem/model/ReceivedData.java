package com.zinedine.alertsystem.model;


import com.zinedine.alertsystem.controllers.ReceivedDataParser;

import java.util.Map;
import java.util.Optional;


public interface ReceivedData
{

  static ReceivedData create(Map<String, String> obj )
  {
    return new ReceivedDataParser( obj );
  }

  Optional<Float> getThresholdRelatedData( SensorDataInfo dataInfo );

}
