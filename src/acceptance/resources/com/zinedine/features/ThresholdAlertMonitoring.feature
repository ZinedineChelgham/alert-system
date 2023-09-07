Feature: Alert reset

  Scenario: A user has defined a simple threshold configuration and the data saved exceeds it
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 10          | LOW      | temp    | AT_LEAST             | 1                        |
    When the following data is saved
      | uuid      | timestamp      | temp |
      | some-uuid | some-timestamp | 12   |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | excessValue | value | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 12          | 10    | LOW      | some-timestamp |

  Scenario: A user has defined a multi-excess threshold and the data saved exceeds it
    Given a serie "sensors" from datalake of id 9
    And a threshold with the following configuration linked to "sensors" from datalake of id 9
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 8           | MEDIUM   | yes     | AT_MOST              | 3                        |
    When the following data is saved
      | uuid      | timestamp       | yes |
      | some-uuid | some-timestamp1 | 2   |
      | some-uuid | some-timestamp2 | 3   |
      | some-uuid | some-timestamp3 | 1   |
    Then the following alert is created in "sensors-alerts" from datalake of id 9
      | uuid            | timestamp      | documentUUID | excessValue | value | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 1           | 8     | MEDIUM   | some-timestamp |

  Scenario: A user has defined a simple threshold but the data saved does not exceed it
    Given a serie "sensors" from datalake of id 198
    And a threshold with the following configuration linked to "sensors" from datalake of id 198
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 8           | HIGH     | haribo  | AT_MOST              | 5                        |
    When the following data is saved
      | uuid      | timestamp       | haribo |
      | some-uuid | some-timestamp1 | 2      |
      | some-uuid | some-timestamp2 | 3.5    |
      | some-uuid | some-timestamp3 | 4      |
    Then there are no alerts in “sensors-alerts” from datalake of id 198


  Scenario: A user has defined a simple threshold and the data saved exceeds and keeps exceeding
    Given a serie "weather-station" from datalake of id 22
    And a threshold with the following configuration linked to "weather-station" from datalake of id 22
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 15          | HIGH     | haribo  | GREATER_THAN         | 1                        |
    When the following data is saved
      | uuid      | timestamp       | haribo |
      | some-uuid | some-timestamp1 | 8      |
      | some-uuid | some-timestamp2 | 16     |
      | some-uuid | some-timestamp3 | 16     |
      | some-uuid | some-timestamp4 | 17     |
      | some-uuid | some-timestamp5 | 16     |
    Then the following alert is created in "weather-station-alerts" from datalake of id 22
      | uuid            | timestamp       | documentUUID | excessValue | value | severity | y-threshold-operator | triggerDate     |
      | some-alert-uuid | some-timestamp2 | some-uuid    | 8           | 15    | HIGH     | GREATER_THAN         | some-timestamp2 |


  Scenario: A user has defined a multi-excess threshold, the data saved exceeds twice, goes back down and then exceeds it
    Given a serie "documents" from datalake of id 65
    And a threshold with the following configuration linked to "documents" from datalake of id 65
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 3           | HIGH     | yes     | AT_LEAST             | 3                        |
    When the following data is saved
      | uuid      | timestamp       | yes |
      | some-uuid | some-timestamp1 | 3   |
      | some-uuid | some-timestamp2 | 4   |
      | some-uuid | some-timestamp3 | 2   |
      | some-uuid | some-timestamp4 | 3   |
      | some-uuid | some-timestamp5 | 4   |
      | some-uuid | some-timestamp6 | 5   |
    Then the following alert is created in "documents-alerts" from datalake of id 65
      | uuid            | timestamp       | documentUUID | excessValue | value | severity | y-threshold-operator | triggerDate     |
      | some-alert-uuid | some-timestamp6 | some-uuid    | 3           | 3     | HIGH     | AT_LEAST             | some-timestamp6 |


  Scenario: A user has defined a multi-excess threshold, the data saved exceeds three times, goes back down and exceeds again
    Given a serie "automates" from datalake of id 34
    And a threshold with the following configuration linked to "automates" from datalake of id 34
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert |
      | 3           | HIGH     | yes     | AT_LEAST             | 3                        |
    When the following data is saved
      | uuid      | timestamp       | yes |
      | some-uuid | some-timestamp1 | 3   |
      | some-uuid | some-timestamp2 | 3   |
      | some-uuid | some-timestamp3 | 4   |
      | some-uuid | some-timestamp4 | 2   |
      | some-uuid | some-timestamp5 | 3   |
      | some-uuid | some-timestamp6 | 4   |
      | some-uuid | some-timestamp7 | 5   |
    Then the following alerts are created after the third and seventh cross in "automates-alerts" from datalake of id 34
      | uuid               | timestamp       | documentUUID | excessValue | value | severity | y-threshold-operator | triggerDate     |
      | some-alert-uuid    | some-timestamp3 | some-uuid    | 4           | 3     | HIGH     | AT_LEAST             | some-timestamp3 |
      | another-alert-uuid | some-timestamp7 | some-uuid    | 5           | 3     | HIGH     | AT_LEAST             | some-timestamp5 |

  Scenario: Data saved has a mean value over 4 measurements above the threshold, a new alert is created
    Given a serie "temperature" from datalake of id 42
    And a threshold with the following configuration linked to "temperature" from datalake of id 42
      | y-threshold | desired-measurements | severity | y-field  | y-threshold-operator | required-excess-to-alert |
      | 20          | 4                    | HIGH     | humidity | LESS_THAN            | 1                        |
    When the following data is saved and returned as a list of 4 measurements
      | uuid      | timestamp       | humidity |
      | some-uuid | some-timestamp1 | 14       |
      | some-uuid | some-timestamp2 | 16       |
      | some-uuid | some-timestamp3 | 20       |
      | some-uuid | some-timestamp4 | 19       |
    Then the following alert is created in "temperature-alerts" from datalake of id 42
      | uuid           | timestamp       | documentUUID | excessValue | value | severity | y-threshold-operator | triggerDate     |
      | new-alert-uuid | some-timestamp3 | some-uuid    | 17.25       | 20    | HIGH     | LESS_THAN            | some-timestamp4 |


  Scenario: A user has defined a simple floor threshold configuration and the data saved exceeds it
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | x-threshold | x-threshold-operator | x-field |
      | 10          | LOW      | y       | AT_LEAST             | 1                        | 0           | GREATER_THAN         | x       |
    When the following data is saved
      | uuid      | timestamp      | y  | x |
      | some-uuid | some-timestamp | 12 | 1 |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | y-value | x-value | y-excessValue | x-excessValue | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 10      | 0       | 12            | 1             | LOW      | some-timestamp |

  Scenario: A user has defined a two floor threshold configuration and the data saved exceeds the first floor 2 times then 1 time on the second floor
    Given a serie "toto" from datalake of id 1
    And a threshold with the following configuration linked to "toto" from datalake of id 1
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | x-threshold | x-threshold-operator | x-field | x-threshold2 | x-threshold-operator2 | y-threshold2 | y-threshold-operator2 |
      | 10          | LOW      | y       | AT_LEAST             | 3                        | 8           | LESS_THAN            | x       | 8            | AT_LEAST              | 15           | GREATER_THAN          |
    When the following data is saved
      | uuid      | timestamp      | y  | x |
      | some-uuid | some-timestamp | 12 | 1 |
      | some-uuid | some-timestamp | 12 | 1 |
      | some-uuid | some-timestamp | 16 | 9 |
    Then the following alert is created in "toto-alerts" from datalake of id 1
      | uuid            | timestamp      | documentUUID | y-value | x-value | y-excessValue | x-excessValue | severity | triggerDate    |
      | some-alert-uuid | some-timestamp | some-uuid    | 10      | 8       | 16            | 9             | LOW      | some-timestamp |
