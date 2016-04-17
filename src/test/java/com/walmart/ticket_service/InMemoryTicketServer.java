package com.walmart.ticket_service;

import java.io.IOException;
import java.net.ServerSocket;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

public class InMemoryTicketServer
	implements AutoCloseable
{
	private static InMemoryTicketServer instance;
	
	private int port;
	
	private TJWSEmbeddedJaxrsServer restServer;
	private ResteasyClient restClient;
	
	private final ReservationResource reservationResource;
	private SeatReclaimer seatReclaimer;
	
	public InMemoryTicketServer() throws IOException {
		port = findFreePort();
		
		restServer = new TJWSEmbeddedJaxrsServer();
		restServer.setPort(port);
		restServer.setBindAddress("localhost");
		
		SeatResource sr = new SeatResource(new int[] {750, 2000, 1500, 1500});
		restServer.getDeployment().getResources().add(sr);
		reservationResource = new ReservationResource(sr);
		restServer.getDeployment().getResources().add(reservationResource);
		
		restServer.start();
		
		restClient = new ResteasyClientBuilder().build();
		
		seatReclaimer = new SeatReclaimer();
		Thread t = new Thread(seatReclaimer);
		t.start();
	}
	
	public String baseUri() {
		return "http://localhost:" + port;
	}

	@Override
	public void close() throws Exception {
		if (seatReclaimer != null) {
			seatReclaimer.stop();
			seatReclaimer = null;
		}
		if (restServer != null) {
			restServer.stop();
			restServer = null;
		}
	}
	
	public ResteasyWebTarget call(String path) {
		return restClient.target(baseUri() + path);
	}
	
	private static int findFreePort() throws IOException {
		ServerSocket server = new ServerSocket(0);
		int port = server.getLocalPort();
		server.close();
		return port;
	}
	
	private class SeatReclaimer implements Runnable {
		private boolean isRunning = true;
		
		@Override
		public void run() {
			while(isRunning) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (isRunning) {
					reservationResource.reclaimTickets();
				}
			}
		}
		
		public void stop() {
			isRunning = false;
		}
	}
}
