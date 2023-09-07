Feature: Manage the thresholds

  Scenario: A user creates a new threshold configuration
    Given a serie "sensors" from datalake of id 198
    And a threshold with the following configuration
    And I am logged in as “Henry”
    When I create the following threshold
      | y-threshold | severity | y-field | y-threshold-operator | required-excess-to-alert | desired-measurements | mode |
      | 8           | HIGH     | haribo  | LESS_THAN            | 3                        | 1                    | mean |
    Then the previous threshold has been saved


  Scenario: A user deletes a threshold configuration
    Given a threshold of uuid "toto"
    And I am the user "Marine"
    When I delete the threshold of uuid "toto"
    Then the threshold of uuid "toto" has been deleted
