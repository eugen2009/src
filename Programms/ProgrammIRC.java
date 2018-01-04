package Programms;

import javax.swing.JOptionPane;
import BiNetcatClasses.Transceiver;
import IRCClasses.ThreadClient;
import Programms.ConnectionStatus;
import Sockets.SocketI;
import Sockets.TCPSocket;

public class ProgrammIRC {

	public static void main(String[] args) {

		if (args.length < 2 || args.length > 2) {
			System.exit(0);
		}
		if (args[0].equals("-l")) {
			JOptionPane.showMessageDialog(null, "IRCServer wurde gestartet!", "IRCServer", JOptionPane.OK_OPTION);
			ConnectionStatus.port = Integer.valueOf(args[1]);
			new TCPSocket(Integer.valueOf(args[1]));
//			new Transceiver(socket, ConnectionStatus.server);
		}
		System.out.flush();
	}
}
