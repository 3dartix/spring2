Feature: Login

  Scenario Outline: Successful Login to the page and logout after
    Given I get driver
    Given I open web browser
    Then I click button logIn
    Then I click button registration
    And I fill out the form: username = "<username>" and password = "<password>" and firstName = "<firstName>" and lastName = "<lastName>" and phone = "<phone>" and email = "<email>"
    Then name should be "<username>"
    Then I delete test user
#    Then click logout button
#    And I should be told "<username>" and "<password>"
#    Then name should be "<name>"
#    Then I click logout button
#    And I find button logIn

    Examples:
      | username | password | firstName | lastName | phone | email |
      | test    | test    | artem      | pugachev | +985558885 | awdawd@adawdawd.ru |


