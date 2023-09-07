Feature: Threshold return to normal

  Scenario: A user has defined a simple threshold configuration and the data saved exceeds it and then returns to normal
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | return-to-normal |
      | 10          | LOW      | temp    | AT_LEAST             | 1                        | 1                |
    When the following data is saved
      | uuid      | timestamp      | temp |
      | some-uuid | some-timestamp | 12   |
      | some-uuid | some-timestamp | 9    |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | excessValue | value | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 12          | 10    | LOW      | some-timestamp |
    And the threshold returns to normal

  Scenario: A user has defined a simple threshold configuration and the data saved exceeds it and does not return to normal
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | return-to-normal |
      | 10          | LOW      | temp    | AT_LEAST             | 1                        | 2                |
    When the following data is saved
      | uuid      | timestamp      | temp |
      | some-uuid | some-timestamp | 12   |
      | some-uuid | some-timestamp | 9    |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | excessValue | value | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 12          | 10    | LOW      | some-timestamp |
    And the threshold does not return to normal

  Scenario: A user has defined a two floor threshold configuration and the data saved exceeds the first floor 2 times then returns to normal
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | x-threshold | x-threshold-operator | x-field | x-threshold2 | x-threshold-operator2 | y-threshold2 | y-threshold-operator2 | return-to-normal |
      | 30          | LOW      | y       | AT_LEAST             | 2                        | 10          | LESS_THAN            | x       | 20           | AT_LEAST              | 15           | LESS_THAN             | 1                |
    When the following data is saved
      | uuid      | timestamp      | y  | x  |
      | some-uuid | some-timestamp | 30 | 1  |
      | some-uuid | some-timestamp | 36 | 1  |
      | some-uuid | some-timestamp | 12 | 11 |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | y-value | x-value | y-excessValue | x-excessValue | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 30      | 10      | 36            | 1             | LOW      | some-timestamp |
    And the threshold returns to normal

