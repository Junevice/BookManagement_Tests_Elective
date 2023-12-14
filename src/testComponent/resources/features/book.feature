Feature: the user can create, retrieve the books and reserve a book
  Scenario: user creates two books and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo" and is "false"
    And the user creates the book "L'avare" written by "Molière" and is "false"
    And the user get all books
    Then the list should contains the following books in the same order
      | name | author | reserved |
      | L'avare | Molière | false |
      | Les Misérables | Victor Hugo | false |

#Feature: the user can create a book and reserve it then retrieve it by its title
  Scenario: user creates two books, reserve the first one and retrieve its information
    When the user creates the book "eragon" written by "Christopher Paolini" and is "false"
    And the user creates the book "On the road" written by "Jack Kerouac" and is "false"
    And the user reserve the book titled "eragon"
    And the user retrieve the book titled "eragon"
    Then the book reservation status should be "true"