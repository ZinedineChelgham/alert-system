package com.zinedine.alertsystem.model;


import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;


public class DatalakeSerieCouple {
  private Pair<Integer, String> dscouple;

  public DatalakeSerieCouple(int datalake, String serie) {
    this.dscouple = Pair.of(datalake, serie);
  }


  public int getDatalake() {
    return dscouple.getLeft();
  }

  public String getSerie() {
    return dscouple.getRight();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    DatalakeSerieCouple that = (DatalakeSerieCouple) o;

    if (getDatalake() != that.getDatalake())
      return false;
    return getSerie().equals(that.getSerie());
  }

  public void setDscouple(Pair<Integer, String> dscouple) {
    this.dscouple = dscouple;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dscouple.getLeft(), dscouple.getRight());
  }
}
