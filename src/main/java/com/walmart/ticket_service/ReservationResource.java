
package com.walmart.ticket_service;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.validator.routines.EmailValidator;

@Path("/reservations")
public class ReservationResource {
	private static final long HOLD_TIMEOUT = 4 * 1000; // 4 seconds
	
	private SeatResource seatResource;
	
	private Map<Integer, SeatHold> holds = new HashMap<Integer, SeatHold>();
	private Queue<SeatHold> expirationQueue = new ArrayDeque<>();
	
	private int lastHoldId = 0;
	
	public ReservationResource(SeatResource seatResource) {
		this.seatResource = seatResource;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response holdSeats(@QueryParam("numSeats") String numSeatsParam,
			@DefaultValue("1") @QueryParam("minLevel") String minLevelParam,
			@DefaultValue("4") @QueryParam("maxLevel") String maxLevelParam,
			@QueryParam("customerEmail") String customerEmail)
	{
		int numSeats, minLevel, maxLevel;
		
		try {
			numSeats = Integer.parseInt(numSeatsParam);
			minLevel = Integer.parseInt(minLevelParam);
			maxLevel = Integer.parseInt(maxLevelParam);
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		if (!seatResource.isValidLevel(minLevel) || !seatResource.isValidLevel(maxLevel)) { return Response.status(Response.Status.FORBIDDEN).build(); }
		
		EmailValidator emailValidator = EmailValidator.getInstance();
		if (!emailValidator.isValid(customerEmail)) { return Response.status(Response.Status.FORBIDDEN).build(); }
		
		int level = 0;
		for (int i = minLevel; i <= maxLevel; ++i) {
			if (seatResource.getAvailableByLevel(i) > numSeats) { level = i; break; }
		}
		
		if (level == 0) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		seatResource.decrementSeatsAvailable(level, numSeats);
		
		SeatHold hold = new SeatHold(lastHoldId++, numSeats, level, customerEmail);
		holds.put(hold.getId(), hold);
		synchronized(expirationQueue) { expirationQueue.add(hold); }
		return Response.ok(hold).build();
	}
	
	@PUT
	@Path("{holdId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response reserveSeats(@PathParam("holdId") String holdIdParam,
			@QueryParam("customerEmail") String customerEmail)
	{
		int holdId;
		try {
			holdId = Integer.parseInt(holdIdParam.toString());
		} catch(NumberFormatException nfe) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		
		EmailValidator emailValidator = EmailValidator.getInstance();
		if (!emailValidator.isValid(customerEmail)) { return Response.status(Response.Status.FORBIDDEN).build(); }
		
		if (!holds.containsKey(holdId)) { return Response.status(Response.Status.FORBIDDEN).build(); }
		
		SeatHold hold = holds.get(holdId);
		if (hold == null) { return Response.status(Response.Status.FORBIDDEN).build(); }
		if (!hold.getCustomerEmail().equals(customerEmail)) { return Response.status(Response.Status.FORBIDDEN).build(); }
		
		holds.remove(holdId);
		
		return Response.ok(hold).build();
	}
	
	void reclaimTickets() {
		SeatHold hold;
		
		synchronized(expirationQueue) {
			while ((hold = expirationQueue.peek()) != null) {
				if (hold.getCreatedOn() + HOLD_TIMEOUT < System.currentTimeMillis()) {
					hold = expirationQueue.remove();
					if (holds.containsKey(hold.getId())) {
						seatResource.incrementSeatsAvailable(hold.getLevel(), hold.getNumSeats());
						holds.remove(hold.getId());
					}
				} else {
					break;
				}
			}
		}
	}
}
