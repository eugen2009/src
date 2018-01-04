package ClientClasses;

import javax.swing.JOptionPane;

import Actor.Actor;
import Sockets.SocketI;

public class Transmitter implements Actor{
	private String peer;
	private SocketI socket;
	
	public Transmitter(SocketI socket, String peer) {
		this.peer = peer;
		this.socket = socket;
	}

	@Override
	public void tell(String message, Actor sender) {
		socket.send(message);
	}

	@Override
	public void shutdown() {
//		socket.send("\u0004");
		socket.shutdownOutput();
		JOptionPane.showMessageDialog(null, "Socket Output wird geschlossen!", peer.toString(), 
				JOptionPane.OK_OPTION);
//		System.exit(0);
	}

}
