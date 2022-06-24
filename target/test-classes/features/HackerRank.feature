Feature: This feature gives result of all bank customers total transactions

  @api
  Scenario: Display the amount summary of all bank customers
    Given a request is prepared to collect debit and credit amount of bank customers transactions
    When a GET call is made to collect debit and credit amount of bank customers transactions
    And the total debit and credit transactions collected from bank customers transactions
    Then the result of bank customer total transaction summary displayed in the console code
