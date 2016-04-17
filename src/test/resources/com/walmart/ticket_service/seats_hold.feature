#Author: Jun Wang

@wip
Feature: Find and hold the best available seats
The feature file "seats_hold.feature" has included the following test cases:
1. Verify the feature -- the find and hold seats on a specific level
	a. Find and hold the 5 seats in Level 1 with a customer 
	b. Verify the number of the available seats are equal to 745 in Level 1 after held the 5 seats
	c. Verify the number of the available seats are equal to 750 in Level 1 after the 5 second holding expired
		 The hold object will be released after a 5 seconds. So the 5 seats need to be return the seats pool in level 1.
		 
2. Verify the feature -- the find and hold seats on level 1, 2, 3 and 4 with the different customers
	a. Find and hold the xx seats in Level 1, 2, 3, 4 with the different customers
	b. Verify the number of the available seats are equal to xx in Level 1, 2, 3, 4 after held the xx seats
	c. Verify the number of the available seats are equal to xx in Level 1, 2, 3, 4 after the 5 second holding expired
		 The hold object will be released after a 5 seconds. So the hold seats need to be return the seats pool in level 1,2 ,3 ,4

3. Verify email format. The email format should be like string@string.string
	a. Verify the email format -- wrong email format: empty email
	b. Verify the email format -- wrong email format: "abc"
	c. Verify the email format -- wrong email format: "abc@"
	d. Verify the email format -- wrong email format: "@abc"


#
# Verify the feature -- the find and hold seats on a specific level 
#
Scenario: Find and hold the 5 seats in Level 1 with a customer
	When The number of the availabel seats are equal or greater than 5 on Level 1
	Then I hold 5 seats on Level 1 for a customer "a@abc.com"
	And The http status code is 200

Scenario: Verify the number of the available seats are equal to 745 in Level 1 after held the 5 seats
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "a@abc.com"
	Then The number of the availabel seats are equal to 745 on Level 1
	And The http status code is 200	
	
Scenario: Verify the number of the available seats are equal to 750 in Level 1 after the 5 second holding expired 
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "a@abc.com"
	And Sleep for 5 seconds
	Then The number of the availabel seats are equal to 750 on Level 1
	And The http status code is 200

#
# Verify the feature -- the find and hold seats on level 1, 2, 3 and 4 with the different customers
#
Scenario Outline: Find and hold 5 seats in Level 1, 2, 3 and 4 with the different customers
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	Then I hold <number_seats> seats on Level <level> for a customer <customer_email>
	And The http status code is 200
	
Examples:
		|level	|number_seats	|customer_email	|
		|1			|5						|"a@abc.com"		|
		|2			|5						|"b@abc.com"		|
		|3			|5						|"c@abc.com"		|
		|4			|5						|"d@abc.com"		|
		
Scenario Outline: Verify the number of the available seats are equal to 45 in Level1, 95 in level2, 145 in level3 and 195 in level4 
									after hold 5 seats in each level
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>								
	Then The number of the availabel seats are equal to <avail_number_seats> on Level <level>
	And The http status code is 200			

Examples:
		|level	|number_seats	|customer_email	|avail_number_seats	|
		|1			|5						|"a@abc.com"		|	745								|
		|2			|5						|"b@abc.com"		|	1995							|
		|3			|5						|"c@abc.com"		|	1495							|
		|4			|5						|"d@abc.com"		|	1495							|

		
Scenario Outline: Verify the number of the available seats are equal to xx in Level x after the 5 second holding expired 
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>		
	And Sleep for 5 seconds
	Then The number of the availabel seats are equal to <avail_number_seats> on Level <level>
	And The http status code is 200
		
Examples:
		|level	|number_seats	|customer_email	|avail_number_seats	|
		|1			|5						|"a@abc.com"		|	750								|
		|2			|5						|"b@abc.com"		|	2000							|
		|3			|5						|"c@abc.com"		|	1500							|
		|4			|5						|"d@abc.com"		|	1500							|

#		
# Verify email format. The email format should be like string@string.string
#
@negative_test
Scenario: Verify the email format -- wrong email format: empty email
	When The number of the availabel seats are equal or greater than 5 on Level 1
	Then I hold 5 seats on Level 1 for a customer " "
	And The http status code is 403	
	
@negative_test	
Scenario: Verify the email format -- wrong email format: "abc"
	When The number of the availabel seats are equal or greater than 5 on Level 1
	Then I hold 5 seats on Level 1 for a customer "abc"
	And The http status code is 403		
	
@negative_test	
Scenario: Verify the email format -- wrong email format: "@abc"
	When The number of the availabel seats are equal or greater than 5 on Level 1
	Then I hold 5 seats on Level 1 for a customer "@abc"
	And The http status code is 403		
		
@negative_test		
Scenario: Verify the email format -- wrong email format: "abc@"
	When The number of the availabel seats are equal or greater than 5 on Level 1
	Then I hold 5 seats on Level 1 for a customer "abc@"
	And The http status code is 403	