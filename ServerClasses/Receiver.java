package ServerClasses;

import javax.swing.JOptionPane;

import BiNetcatClasses.ReaderPrinter;
import BiNetcatClasses.Transceiver;
import Programms.ConnectionStatus;
import Sockets.SocketI;

public class Receiver implements Runnable {
	private String peer;
	private SocketI socket;
	private ReaderPrinter readerPrinter;
	private Transceiver transceiver;

	public Receiver(Transceiver transceiver, SocketI socket, String peer) {
		this.peer = peer;
		this.socket = socket;
		this.transceiver = transceiver;
		readerPrinter = new ReaderPrinter(transceiver, this, socket, peer);
	}

	public void receive() {
		String s;
		while ((s = socket.receive(1024)) != null) {
			readerPrinter.tell(s, null);
			// s == null ||
		}
	}
	
	public void receiverShutdown() {
		socket.shutdownInput();
		readerPrinter.shutdown();
	}

	@Override
	public void run() {
		try {
			receive();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
