package com.zinedine.alertsystem.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * An embeddable class representing information about sensor data.
 * This class is meant to be embedded within the Threshold entity.
 */
@Entity
public class SensorDataInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String idField;
  private String idValue;
  private String field;


  /**
   * Constructs a new instance of SensorDataInfo with provided values.
   *
   * @param idField The field associated with the sensor data's unique identifier.
   * @param idValue The value of the unique identifier.
   * @param field   The name of the sensor data field.
   */
  public SensorDataInfo(String idField, String idValue, String field) {
    this.idField = idField;
    this.idValue = idValue;
    this.field = field;
  }

  /**
   * Default constructor required for hibernate.
   */
  public SensorDataInfo() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdField() {
    return idField;
  }

  public void setIdField(String idField) {
    this.idField = idField;
  }

  public String getIdValue() {
    return idValue;
  }

  public void setIdValue(String idValue) {
    this.idValue = idValue;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SensorDataInfo that = (SensorDataInfo) o;
    return Objects.equals(idField, that.idField) && Objects.equals(idValue, that.idValue) && Objects.equals(field, that.field);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idField, idValue, field);
  }

  @Override
  public String toString() {
    return "SensorDataInfo{" +
      "idField='" + idField + '\'' +
      ", idValue='" + idValue + '\'' +
      ", field='" + field + '\'' +
      '}';
  }
}
