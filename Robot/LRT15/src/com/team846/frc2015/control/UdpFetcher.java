package com.team846.frc2015.control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class UdpFetcher {

	class NetworkFetcher extends Thread {

		DatagramSocket clientSocket;
		InetAddress IpAddress;
		
		final long loopDelta = 1000 / 15;
		final int IpPort;
		
		final byte[] sendData = new byte[1];
		final byte[] receiveData = new byte[128];
		
		final DatagramPacket sendPacket;
		
		final Semaphore responseAvailiability = new Semaphore(1);
		String response = "";

		NetworkFetcher(String targetHost, int port)  {
			try {
				clientSocket = new DatagramSocket();
				IpAddress = InetAddress.getByName(targetHost);
			} catch (UnknownHostException | SocketException e) {
				e.printStackTrace();
			}

			IpPort = port;
			sendPacket = new DatagramPacket(sendData, sendData.length,
					IpAddress, port);
		}

		public void run() {
			while (true) {
				try {
					clientSocket.send(sendPacket);
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				    clientSocket.receive(receivePacket);
				    
				    responseAvailiability.acquire();
				    response = new String(receivePacket.getData());
				    responseAvailiability.release();
				    
				    Thread.sleep(loopDelta);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public String getResponse() throws InterruptedException {
			String newResponse;
			
			responseAvailiability.acquire();
			newResponse = response;
			responseAvailiability.release();
			
			return newResponse;
		}
	}

	private final NetworkFetcher instance;

	public UdpFetcher(String targetHost, int port) {
		instance = new NetworkFetcher(targetHost, port);
		instance.start();
	}
	
	public String getLastResponse() {
		try {
			return instance.getResponse();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "";
		}
	}
}
