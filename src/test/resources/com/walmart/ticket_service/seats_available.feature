#Author: Jun Wang

@wip
Feature: Find the number of the available seats
The feature file "seats_available.feature" includes the following test cases:
1. Verify the total number of the available seats are equal to xx 
2. Verify the total number of the available seats are not equal to xx 
3. Verify the total number of the available seats are equal or greater than xx 
4. Verify the number of the available seats are equal to xx in Level x
5. Verify the number of the available seats are not equal to xx in Level x
6. Verify the number of the available seats are equal or greater than xx in Level x
7. Verify the number of the available seats are equal to xx in the different Level x ( group the level(1 to 4) together to test )
8. Verify the number of the available seats are not equal to xx in the different Level x ( group the level(1 to 4) together to test )
9. Verify the number of the available seats are equal or greater than xx in the different Level x ( group the level(1 to 4) together to test )
10. Verify the range of the level -- level = 1: in the level range
11. Verify the range of the level -- level = 0: out of the level range
12. Verify the range of the level -- level = 5: out of the level range

Scenario: Verify the total number of the available seats are equal to xx  
	When The number of the total availabel seats are equal to 5750
	Then The http status code is 200

Scenario: Verify the total number of the available seats are not equal to xx
	When The number of the total availabel seats are not equal to 2000
	Then The http status code is 200

Scenario: Verify the total number of the available seats are equal or greater than xx 
	When The number of the total availabel seats are equal or greater than 5000
	Then The http status code is 200
				
Scenario: Verify the number of the available seats are equal to xx in Level x
	When The number of the availabel seats are equal to 750 on Level 1
	Then The http status code is 200

Scenario: Verify the number of the available seats are not equal to xx in Level x
	When The number of the availabel seats are not equal to 500 on Level 1
	Then The http status code is 200
	
Scenario: Verify the number of the available seats are equal or greater than xx in Level x
	When The number of the availabel seats are equal or greater than 50 on Level 1
	Then The http status code is 200
	
Scenario Outline: Verify the number of the available seats are equal to xx in the different Level x
	When The number of the availabel seats are equal to <number> on Level <level>
	Then The http status code is 200
	
Examples:
		|level	|number	|	
		|1			|750		|
		|2			|2000		|
		|3			|1500		|
		|4			|1500		|
		
Scenario Outline: Verify the number of the available seats are not equal to xx in the different Level x
	When The number of the availabel seats are not equal to <number> on Level <level>
	Then The http status code is 200
	
Examples:
		|level	|number	|	
		|1			|500		|
		|2			|500		|
		|3			|500		|
		|4			|500		|
		
Scenario Outline: Verify the number of the available seats are not equal to xx in the different Level x
	When The number of the availabel seats are equal or greater than <number> on Level <level>
	Then The http status code is 200
	
Examples:
		|level	|number	|	
		|1			|50			|
		|2			|50			|
		|3			|50			|
		|4			|50			|
			
#
# The range of the level is 1 - 4		
#
@positive_test
Scenario: Verify the range of the level -- level = 1: in the level range
	When I get the number of the available seats in Level 1
	Then The http status code is 200
	
@negative_test
Scenario: Verify the range of the level -- level = 0: out of the level range
	When I get the number of the available seats in Level 0
	Then The http status code is 403
	
@negative_test
Scenario: Verify the range of the level -- level = 5: out of the level range
	When I get the number of the available seats in Level 5
	Then The http status code is 403
	