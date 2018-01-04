package ClientClasses;

import java.util.Scanner;

import Actor.Actor;
import Sockets.SocketI;
import Sockets.TCPSocket;
import Sockets.UDPSocket;

public class Reader implements Runnable {

	private SocketI socket;
	private Actor act;
//	private Transmitter trans;
	private String peer;

	public Reader(SocketI socket, String peer) {
		this.peer = peer;
		this.socket = socket;
		act = new Transmitter(socket, peer);
	}
	
//	public Reader(UDPSocket socket, String peer) {
//		this.peer = peer;
//		this.socket = socket;
//		trans = new Transmitter(socket, peer);
//	}

	public void read() {

		Scanner s = new Scanner(System.in);
		while (s.hasNextLine()) {
			
				act.tell(s.nextLine(), null);
		}
		act.shutdown();
		s.close();
	}

	@Override
	public void run() {
		try {
			read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
