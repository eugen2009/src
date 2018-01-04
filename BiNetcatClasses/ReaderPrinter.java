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

//		private Printer print;
//		private Reader reader;
		private Thread thread2;
		private SocketI socket;
		private Transceiver transceiver;
		private Receiver receiver;
		private User user;
		
		public ReaderPrinter(Transceiver trans, Receiver receiver, SocketI socket, String peer) {
			if(peer == ConnectionStatus.server) {
//				thread2 = new Thread(new Reader(socket, peer));
//				thread2.start();
				this.socket = socket;
				transceiver = trans;
				this.receiver = receiver;
				user = new User(null, null, null, this.socket);
				
			}
			
//			if(peer == ConnectionStatus.client) {
////				c)  Der ReaderPrinter liest in einer Schleife jeweils eine Textzeile von der Standardeingabe und 
////				übergibt sie ohne das Zeilenumbruchzeichen an die tell-Methode des Transceivers.
//				thread2 = new Thread(new Reader(socket, peer));
//				thread2.start();
//				transceiver = new Transceiver(socket, peer);
//			}			
		}
		
//		b)  Die tell-Methode nimmt einen String vom Transceiver entgegen und schreibt diesen auf die 
//		Standardausgabe. 
		@Override
		public void tell(String message, Actor sender) {
			List<String> list = Stream.of(message.split(" ")).collect(Collectors.toList());
			String get0 = list.get(0);
			String s = "";
			
			user.setCurPartnerIP(socket.getPartnerAdress());
			
			if(list.get(0).equals("PING")) {
				
			}
			
			if(get0.equals("NICK") && user.getNick() == null) {
				user.setNick(list.get(1));
			}
			
			if(get0.equals("USER") && user.getUser() == null) {
				user.setUser(list.get(1));
				for(int i = 2; i < list.size(); i++) {
					if(list.get(i).indexOf(":") != -1) {
						s = list.get(i).substring(1);
						for(int y = ++i ; y < list.size(); y++) {
							s+= " " + list.get(y);
						}
						user.setFullName(s);
					}
					
				}
			}
			
			if ((!get0.equals("USER") && !get0.equals("NICK")) 
					&& (user.getUser() == null || user.getNick() == null)) {
				receiver.receiverShutdown();
//				System.exit(0);
			}
			
			if(user.getUser() != null && user.getNick() != null && !user.getEnable()) {
				Parser.check(this, user);
			}
			
			
			
		}
		
		public void tellAfterParse(String nick, String user) {
			if(!this.user.getEnable()) {		
			transceiver.tell(":" + socket.getLocalAddress().getHostAddress() + " 001 " + nick +" :Welcome to the Internet Relay Network " + nick + "!" + user + "@" + socket.getPartnerAdress().getHostAddress() , null);
			}
		}
		
		public void sendMessage(String message) {
			transceiver.tell();
		}

		@Override
		public void shutdown() {
			transceiver.shutdown();
		}
}
