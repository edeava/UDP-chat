package udp_chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements  Runnable {
	public final static int PORT = 2020;
	private final static int BUFFER = 1024;

	private DatagramSocket socket;
	private ArrayList<InetAddress> clientAddresses;
	private ArrayList<Integer> clientPorts;
	private HashSet<String> existingClients;
	private Map<Integer, String> clientNames;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ChatServer() throws IOException {
		socket = new DatagramSocket(PORT);
		System.out.println("Server is running and is listening on port " + PORT);
		clientAddresses = new ArrayList();
		clientPorts = new ArrayList();
		existingClients = new HashSet();
		clientNames = new HashMap<>();
	}

	public void run() {
		byte[] buffer = new byte[BUFFER];
		while (true) {
			try {
				Arrays.fill(buffer, (byte) 0);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				String message = new String(buffer, 0, buffer.length);

				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();
				if(clientNames.get(clientPort) == null) {
					clientNames.put(clientPort, NamePool.getName());
				}

				String id = clientAddress.toString() + "|" + clientPort + "|" + clientNames.get(clientPort);
				if (!existingClients.contains(id)) {
					existingClients.add(id);
					clientPorts.add(clientPort);
					clientAddresses.add(clientAddress);
				}

				System.out.println(clientNames.get(clientPort) + " : " + message);
				byte[] data = (clientNames.get(clientPort) + " : " + message).getBytes();
				for (int i = 0; i < clientAddresses.size(); i++) {
					InetAddress clAddress = clientAddresses.get(i);
					int clPort = clientPorts.get(i);
					packet = new DatagramPacket(data, data.length, clAddress, clPort);
					socket.send(packet);
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	public static void main(String args[]) throws Exception {
		ChatServer server_thread = new ChatServer();
		server_thread.run();
	}
}