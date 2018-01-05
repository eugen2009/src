package BiNetcatClasses;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Actor.Actor;
import ClientClasses.Reader;
import IRCClasses.Parser;
import IRCClasses.User;
import Programms.ConnectionStatus;
import ServerClasses.Receiver;
import Sockets.SocketI;

//		a)  Der ReaderPrinter implementiert das Interface Actor. 
public class ReaderPrinter implements Actor {

	// private Printer print;
	// private Reader reader;
	private Thread thread2;
	private SocketI socket;
	private Transceiver transceiver;
	private Receiver receiver;
	private User user;

	public ReaderPrinter(Transceiver trans, Receiver receiver, SocketI socket, String peer) {
		if (peer == ConnectionStatus.server) {
			// thread2 = new Thread(new Reader(socket, peer));
			// thread2.start();
			this.socket = socket;
			transceiver = trans;
			this.receiver = receiver;
			user = new User(null, null, null, this.socket);

		}

		// if(peer == ConnectionStatus.client) {
		//// c) Der ReaderPrinter liest in einer Schleife jeweils eine Textzeile von der
		// Standardeingabe und
		//// übergibt sie ohne das Zeilenumbruchzeichen an die tell-Methode des
		// Transceivers.
		// thread2 = new Thread(new Reader(socket, peer));
		// thread2.start();
		// transceiver = new Transceiver(socket, peer);
		// }
	}

	// b) Die tell-Methode nimmt einen String vom Transceiver entgegen und schreibt
	// diesen auf die
	// Standardausgabe.
	@Override
	public void tell(String message, Actor sender) {
		List<String> list = Stream.of(message.split(" ")).collect(Collectors.toList());
		String get0 = list.get(0);
		String s = "";

		user.setCurPartnerIP(socket.getPartnerAdress());

		if (list.get(0).equals("PING")) {
			sendMessage("PONG");
		}

		if (get0.equals("NICK")) {

			if (list.size() == 1) {
				sendMessage(":" + socket.getLocalAddress().getHostAddress() + " 431 * :No nickname given");
			}

			if (user.getNick() != null && this.user.getEnable()) {
				user.setNick(list.get(1));
				sendMessage(welcomeMessage());
			}

			if (user.getNick() == null) {
				user.setNick(list.get(1));
			}
		}

		if (get0.equals("USER")) {
			for (int i = 2; i < list.size(); i++) {
				if (list.get(i).indexOf(":") != -1) {
					s = list.get(i).substring(1);
					for (int y = ++i; y < list.size(); y++) {
						s += " " + list.get(y);
					}
				}
			}
			if (list.get(1) == null || s == "") {
				sendMessage(":" + socket.getLocalAddress().getHostName() + " * USER :Not enough parameters");
				return;
			}

			if (user.getUser() != null) {
				sendMessage(":" + socket.getLocalAddress().getHostAddress() + " 462 * " + list.get(1)
						+ " :Unauthorized command (already registered)");
			}
			user.setUser(list.get(1));
			user.setFullName(s);
		}

		if ((!get0.equals("USER") && !get0.equals("NICK") && !get0.equals("PING") && !get0.equals("PONG"))
				&& (user.getUser() == null || user.getNick() == null)) {
			sendMessage(":Unknown command");
			// receiver.receiverShutdown();
			// System.exit(0);
		}

		if (get0.equals("QUIT")) {
			receiver.receiverShutdown();
		}

		if (user.getUser() != null && user.getNick() != null && !user.getEnable()) {
			Parser.check(this, user);
		}

		if (user.getEnable() && (get0.equals("PRIVMSG") || get0.equals("NOTICE"))) {
			User privmsgUser = user.getUser(list.get(1));
			String privmsg;
			
			if (list.size() == 1) {
				sendMessage(":no recipient");
			}

			if (privmsgUser.getNick() == null) {
				sendMessage(":no such nick");
			}else {
				for (int i = 2; i < list.size(); i++) {
					s += " " + list.get(i);
				}
				
				if(get0.equals("PRIVMSG")) {
					sendMessageToUser(":" + this.user.getNick() + "!_@" + socket.getPartnerAdress().getHostAddress() + ""
							+ " PRIVMSG " + privmsgUser.getNick() + " " + s.substring(1), privmsgUser);
				}else {
					sendMessageToUser(":" + this.user.getNick() + "!_@" + socket.getPartnerAdress().getHostAddress() + ""
							+ " NOTICE " + privmsgUser.getNick() + " " + s.substring(1), privmsgUser);
				}
			}
		}
		// PRIVMSG super
		// :super!_@wlan-172-31-59-180.fh-luebeck.de PRIVMSG super
		// NOTICE super kdlsjf
		// :super!_@wlan-172-31-59-180.fh-luebeck.de NOTICE super kdlsjf
	}

	public String welcomeMessage() {
		return ":" + socket.getLocalAddress().getHostAddress() + " 001 " + user.getNick()
				+ " :Welcome to the Internet Relay Network " + user.getNick() + "!" + user.getUser() + "@"
				+ socket.getPartnerAdress().getHostAddress() + "\n:" + socket.getLocalAddress().getHostAddress()
				+ " 002 " + user.getNick() + " : Your host is " + socket.getLocalAddress().getHostAddress()
				+ ", running version 1.0.0\n" + ":" + socket.getLocalAddress().getHostAddress() + " 003 "
				+ user.getNick() + " : This server was created 2018-1-9\n" + ":"
				+ socket.getLocalAddress().getHostAddress() + " 004 " + user.getNick() + " : "
				+ socket.getLocalAddress().getHostAddress() + " 1.0.0 <available user modes><available channel modes>";
	}

	public void tellAfterParse(String nick, String user) {
		if (!this.user.getEnable()) {
			transceiver.tell(welcomeMessage(), null);
		}
	}

	public void sendMessage(String message) {
		transceiver.tell(message, null);
	}
	
	public void sendMessageToUser(String message, User user) {
		
	}

	@Override
	public void shutdown() {
		transceiver.shutdown();
	}
}
