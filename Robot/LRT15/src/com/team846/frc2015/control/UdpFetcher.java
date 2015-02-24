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
		
		long loopDelta = 1000 / 15;
		int IpPort;
		
		byte[] sendData = new byte[1];
		byte[] receiveData = new byte[128];
		
		DatagramPacket sendPacket;
		
		Semaphore responseAvailiability = new Semaphore(1);
		String response = new String();

		NetworkFetcher(String targetHost, int port)  {
			try {
				clientSocket = new DatagramSocket();
				IpAddress = InetAddress.getByName(targetHost);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public String getResponse() throws InterruptedException {
			String newResponse;
			
			responseAvailiability.acquire();
			newResponse = new String(response);
			responseAvailiability.release();
			
			return newResponse;
		}
	}
	
	NetworkFetcher instance;

	public UdpFetcher(String targetHost, int port) {
		instance = new NetworkFetcher(targetHost, port);
		instance.start();
	}
	
	public String getLastResponse() {
		try {
			return instance.getResponse();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
