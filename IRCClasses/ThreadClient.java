package IRCClasses;

import java.net.Socket;

import BiNetcatClasses.Transceiver;
import Programms.ConnectionStatus;
import Sockets.SocketI;
import Sockets.TCPSocket;

public class ThreadClient implements Runnable {
	SocketI socket;
	
	public ThreadClient(SocketI socket) {
		this.socket = socket;
	}
	
	public void clientServer() {
	
	new Transceiver(this.socket, ConnectionStatus.server);
	
	new TCPSocket(ConnectionStatus.port);
	}
	
	@Override
	public void run() {
		clientServer();
	}

}
