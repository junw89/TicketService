#Author: Jun Wang

@wip
Feature: Reserve and commit a held seats for a customer

1. Verify the feature -- the reserve and commit a held seats on level 1
	a. Verify the Reserve and commit function - Reserve and commit the held seats for a customer on level 1
	b. Verify the number of the available seats are equal to 45 in Level 1 after the reserve 5 seats on level 1
	c. Verify the number of the available seats are equal to 45 in Level 1 after the reserve 5 seats on level 1 
			and after the 5 second holding expired
	
2. Verify the feature -- the reserve and commit a held seats on level 1 after the held seats has expired with the customer
	This will verify the reservation can not be submitted after held seats has expired with the customer
	
3. Verify the feature --  the reserve and commit a held seats on level 1, 2, 3 and 4
	a. Verify the Reserve and commit function - Reserve and commit the held seats for a customer on level 1, 2, 3, 4
	b. Verify the number of the available seats are equal to xx in Level 1, 2, 3, 4 after the reserve 5 seats on level 1, 2, 3, 4
	c. Verify the number of the available seats are equal to xx in Level 1, 2, 3, 4 after the reserve 5 seats on level 1, 2, 3, 4
			and after the 5 second holding expired 
	
4. Verify the reserve and commit a held seats on level 1, 2, 3 and 4 that the held seats has expired
	a. Verify the return code is 403 after Reserve and commit the held seats for the different customers when the holds exp
	
#
# Verify the feature -- the reserve and commit a held seats on level 1
#
Scenario: Reserve and commit the held seats for a customer
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "abc@abc.com"
	Then Reserved the held seats for customer "abc@abc.com"
	And The http status code is 200	
	
Scenario: Verify the number of the available seats are equal to 745 in Level 1 after the reservation
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "abc@abc.com"
	And Reserved the held seats for customer "abc@abc.com"
	Then The number of the availabel seats are equal to 745 on Level 1
	And The http status code is 200
	
Scenario: Verify the number of the available seats are equal to 745 in Level 1 after the 5 second holding expired
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "abc@abc.com"
	And Reserved the held seats for customer "abc@abc.com"
	And Sleep for 5 seconds
	Then The number of the availabel seats are equal to 745 on Level 1
	And The http status code is 200
	
#
# Verify the feature -- the reserve and commit a held seats on level 1 that the held seats has expired
#
Scenario: Verify the number of the available seats are equal to 750 in Level 1 after the 5 second holding expired
	When The number of the availabel seats are equal or greater than 5 on Level 1
	And I hold 5 seats on Level 1 for a customer "abc@abc.com"
	And Sleep for 5 seconds
	And Reserved the held seats for customer "abc@abc.com"
	And The http status code is 403
				
#	
# Verify the feature --  the reserve and commit a held seats on level 1, 2, 3 and 4
#
Scenario Outline: Reserve and commit the held seats for the diferent customers
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>
	Then Reserved the held seats for customer <customer_email>
	And The http status code is 200
	
Examples:
		|level	|number_seats	|customer_email	|
		|1			|5						|"a@abc.com"		|
		|2			|5						|"b@abc.com"		|
		|3			|5						|"c@abc.com"		|
		|4			|5						|"d@abc.com"		|
		
Scenario Outline: Verify the number of the available seats are equal to 45 in Level1, 95 in level2, 145 in level3 and 195 in level4 
									after reserved 5 seats in each level
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>
	And Reserved the held seats for customer <customer_email>								
	Then The number of the availabel seats are equal to <avail_number_seats> on Level <level>
	And The http status code is 200			

Examples:
		|level	|number_seats	|customer_email	|avail_number_seats	|
		|1			|5						|"a@abc.com"		|	745								|
		|2			|5						|"b@abc.com"		|	1995							|
		|3			|5						|"c@abc.com"		|	1495							|
		|4			|5						|"d@abc.com"		|	1495							|
									
Scenario Outline: Verify the number of the available seats are equal to 45 in Level1, 95 in level2, 145 in level3 and 195 in level4 
									after the 5 second holding expired 
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>
	And Reserved the held seats for customer <customer_email>		
	And Sleep for 5 seconds
	Then The number of the availabel seats are equal to <avail_number_seats> on Level <level>	
	And The http status code is 200
		
Examples:
		|level	|number_seats	|customer_email	|avail_number_seats	|
		|1			|5						|"a@abc.com"		|	745								|
		|2			|5						|"b@abc.com"		|	1995							|
		|3			|5						|"c@abc.com"		|	1495							|
		|4			|5						|"d@abc.com"		|	1495							|
		
#	
# Verify the reserve and commit a held seats on level 1, 2, 3 and 4 that the held seats has expired
#
Scenario Outline: Verify the return code is 403 after Reserve and commit the held seats for the different customers when the holds exp
	When The number of the availabel seats are equal or greater than <number_seats> on Level <level>
	And I hold <number_seats> seats on Level <level> for a customer <customer_email>
	And Sleep for 5 seconds
	And Reserved the held seats for customer <customer_email>	
	And The http status code is 403
		
Examples:
		|level	|number_seats	|customer_email	|
		|1			|5						|"a@abc.com"		|
		|2			|5						|"b@abc.com"		|
		|3			|5						|"c@abc.com"		|
		|4			|5						|"d@abc.com"		|
