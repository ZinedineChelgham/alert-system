package com.zinedine.alertsystem.model;


import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ReceivedDataImpl
{

  private String uuid;
  private String timestamp;


  public ReceivedDataImpl(String uuid, String timestamp )
  {
    this.uuid = uuid;
    this.timestamp = timestamp;
  }

  public ReceivedDataImpl()
  {
  }


  public String getUuid()
  {
    return uuid;
  }


  public ReceivedDataImpl setUuid(String uuid )
  {
    this.uuid = uuid;
    return this;
  }


  public String getTimestamp()
  {
    return timestamp;
  }


  public ReceivedDataImpl setTimestamp(String timestamp )
  {
    this.timestamp = timestamp;
    return this;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReceivedDataImpl busitData = (ReceivedDataImpl) o;
    return Objects.equals(uuid, busitData.uuid) && Objects.equals(timestamp, busitData.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, timestamp);
  }
}
