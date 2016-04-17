package com.walmart.ticket_service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/seats")
public class SeatResource {
	private final int[] availableSeats;
	
	public SeatResource(int[] availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getAllAvailable() {
		int total = 0;
		for (int x : availableSeats) { total += x; }
		
		return total;
	}
	
	@GET
	@Path("{level}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAvailableByLevel(@PathParam("level") String levelParam) {
		int level;
		try {
			level = Integer.parseInt(levelParam.toString());
		} catch (NumberFormatException nfe) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		ResponseBuilder builder = (isValidLevel(level) ? Response.ok(getAvailableByLevel(level)) : Response.status(Response.Status.FORBIDDEN));
		return builder.build();
	}
	
	boolean isValidLevel(int level) {
		return ((level > 0) && (level < 5)); 
	}
	
	void decrementSeatsAvailable(int level, int seatCount) {
		availableSeats[level - 1] -= seatCount;
	}
	
	int getAvailableByLevel(int level) { return availableSeats[level - 1]; }
	
	void incrementSeatsAvailable(int level, int seatCount) {
		availableSeats[level - 1] += seatCount;
	}
}
