package com.walmart.ticket_service;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

public class SeatsStepDefs {	
	private InMemoryTicketServer server;
	
	@Before
	public void beforeScenario() throws IOException {
		server = new InMemoryTicketServer();
	}
	
	@After
	public void afterScenario() throws Exception {
		if (server != null) { server.close(); }
	}
	
	private int responseCode;
	
	private Integer getAvailableSeats() {
		Response response = server.call("/seats").request().buildGet().invoke();
		
		responseCode = response.getStatus();
		try {
			return response.readEntity(Integer.class);
		} catch(ProcessingException e) {
			return -1; // failed to deserialize
		} finally {
			response.close();
		}
	}
	
	private Integer getAvailableSeats(int level) {
		Response response = server.call("/seats/" + level).request().buildGet().invoke();
		
		responseCode = response.getStatus();
		try {
			return response.readEntity(Integer.class);
		} catch(ProcessingException e) {
			return -1; // failed to deserialize
		} finally {
			response.close();
		}
	}
	
	private SeatHold seatHold;
	private void holdSeats(int num_seats, int level, String email) {
		Response response = server.call("/reservations")
				.queryParam("numSeats", num_seats)
				.queryParam("minLevel", level)
				.queryParam("maxLevel", level)
				.queryParam("customerEmail", email)
				.request()
				.buildPost(null)
				.invoke();
		
		responseCode = response.getStatus();
		try {
			seatHold = response.readEntity(SeatHold.class);
		} catch(ProcessingException e) {
			return; // failed to deserialize
		} finally {
			response.close();
		}
	}
	
	private void reserveSeats(String email) {
		Response response = server.call("/reservations/" + seatHold.getId())
				.queryParam("customerEmail", email)
				.request()
				.buildPut(null)
				.invoke();
		
		responseCode = response.getStatus();
		try {
			seatHold = response.readEntity(SeatHold.class);
		} catch(ProcessingException e) {
			return; // failed to deserialize
		} finally {
			response.close();
		}
	}

	/**
	 * Verify the number of the total available seats are equal to the expected seat number.
	 * @param num_seats: expected seats number
	 * @return boolean: True: equal to the expected seat number
	 * 					False: not equal to the expected seat number
	 */
	@When("^The number of the total availabel seats are equal to (\\d+)$")
	public void the_number_of_the_total_availabel_seats_are_equal_to(int num_seats) throws Throwable {
		assertThat(getAvailableSeats(), is(num_seats));
	}

	/**
	 * Verify the number of the total available seats are not equal to the expected seat number.
	 * @param num_seats: expected seats number
	 * @return boolean: True: not equal to the expected seat number
	 * 					False: equal to the expected seat number
	 */
	@When("^The number of the total availabel seats are not equal to (\\d+)$")
	public void the_number_of_the_total_availabel_seats_are_not_equal_to(int num_seats) throws Throwable {
		assertThat(getAvailableSeats(), not(num_seats));
	}

	/**
	 * Verify the number of the total available seats are equal or greater than the expected seat number.
	 * @param num_seats: expected seats number
	 * @return boolean: True: not equal to the expected seat number
	 * 					False: equal to the expected seat number
	 */
	@When("^The number of the total availabel seats are equal or greater than (\\d+)$")
	public void the_number_of_the_total_availabel_seats_are_equal_or_greater_than(int num_seats) throws Throwable {
		assertTrue(getAvailableSeats() >= num_seats);
	}

	/**
	 * Verify the number of the total available seats are equal to the expected seat number on level x
	 * @param num_seats: expected seats number
	 * @return boolean: True: equal to the expected seat number
	 * 					False: not equal to the expected seat number
	 */
	@When("^The number of the availabel seats are equal to (\\d+) on Level (\\d+)$")
	public void the_number_of_the_availabel_seats_are_equal_to_on_Level(int num_seats, int level) throws Throwable {
		assertThat(getAvailableSeats(level), is(num_seats));
	}
	
	/**
	 * Verify the number of the total available seats are not equal to the expected seat number on level x
	 * @param num_seats: expected seats number
	 * @return boolean: True: not equal to the expected seat number
	 * 					False: equal to the expected seat number
	 */
	@When("^The number of the availabel seats are not equal to (\\d+) on Level (\\d+)$")
	public void the_number_of_the_availabel_seats_are_not_equal_to_on_Level(int num_seats, int level) throws Throwable {
		assertThat(getAvailableSeats(level), not(num_seats));
	}
	
	/**
	 * Verify the number of the total available seats are equal or greater than the expected seat number.
	 * @param num_seats: expected seats number
	 * @return boolean: True: not equal to the expected seat number
	 * 					False: equal to the expected seat number
	 */
	@When("^The number of the availabel seats are equal or greater than (\\d+) on Level (\\d+)$")
	public void the_number_of_the_availabel_seats_are_equal_or_greater_than_on_Level(int num_seats, int level) throws Throwable {
		assertTrue(getAvailableSeats(level) >= num_seats);
	}
	
	/**
	 * Retrieve the number of the available seats on level x
	 * @param level: level number
	 * @return: no return
	 */
	@When("^I get the number of the available seats in Level (\\d+)$")
	public void i_get_the_number_of_available_seats_on_Level(int level) throws Throwable {
		getAvailableSeats(level);
	}

	/**
	 * Hold the number of the available seats on level x for a customer
	 * @param num_seats: expected seat number to hold
	 * @param level: level number
	 * @param email: customer email 
	 * @return: no return
	 */
	@When("^I hold (\\d+) seats on Level (\\d+) for a customer \"(.*?)\"$")
	public void i_hold_seats_on_Level_for_a_customer(int num_seats, int level, String email) throws Throwable {
		holdSeats(num_seats, level, email);
	}
	
	/**
	 * Committed the hold seats for the customer
	 * @param email: customer email 
	 * @return: no return
	 */
	@When("^Reserved the held seats for customer \"(.*?)\"$")
	public void reserved_the_held_seats_for_customer(String email) throws Throwable {
		this.reserveSeats(email);
	}
	
	/**
	 * Delay a specific time, such xx seconds
	 * @param timeout: delay a period
	 * @return: no return
	 */
	@When("^Sleep for (\\d+) seconds$")
	public void sleep_for_seconds(int timeout) throws Throwable {
		Thread.sleep(timeout * 1000);
	}
	
	
	/**
	 * Verify the HTTP status code
	 * @param httpStCode: expected HTTP status code
	 * @return boolean: True: not equal to the expected seat number
	 * 					False: equal to the expected seat number
	 */
	@Then("^The http status code is (\\d+)$")
	public void the_http_status_code_is(int httpStCode) throws Throwable {
		assertThat(responseCode, is(httpStCode));
	}
}
