package BiNetcatClasses;

import javax.swing.JOptionPane;

import Actor.Actor;
import ClientClasses.Transmitter;
import Programms.ConnectionStatus;
import ServerClasses.Receiver;
import Sockets.SocketI;
import Sockets.UDPSocket;

// a) Der Transceiver implementiert das Interface Actor. 
public class Transceiver implements Actor {
	private SocketI socket;
	private String peer;
	private Transmitter transmitter;
	private Thread thread1;
	

	public Transceiver(SocketI socket, String peer) {
		this.peer = peer;
		this.socket = socket;
		if(peer == ConnectionStatus.server) {
		// c) Der Transceiver empfängt pro Datagramm einen String vom Partner-Transceiver und
		// übergibt ihn der tell-Methode des ReaderPrinters.
		thread1 = new Thread(new Receiver(this, socket, peer));
		thread1.start();
//		new ReaderPrinter(this, socket, peer);
		}
	}

	// b) Die tell-Methode sendet den Parameter-String in einem Datagramm an den
	// PartnerTransceiver.
	@Override
	public void tell(String message, Actor sender) {
		if(ConnectionStatus.connected == true) {
			if(transmitter == null) {
			transmitter = new Transmitter(socket, peer);
			}
			transmitter.tell(message, sender);
		}else {
			JOptionPane.showMessageDialog(null, "Partneradresse unbekannt", "Fehler", JOptionPane.OK_OPTION);
		}
	}

	@Override
	public void shutdown() {
		transmitter.shutdown();
	}

}
