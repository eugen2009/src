package ServerClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import Actor.Actor;
import IRCClasses.Parser;
import IRCClasses.User;

public class Printer implements Actor{
	private String peer;
	private Receiver receiver;
	private static int count = 0;
	private User user;
	
	public Printer(Receiver receiver, String peer) {
		this.receiver = receiver;
		this.peer = peer;
		user = new User(null, null, null);
	}
	
	@Override
	public void tell(String message, Actor sender) {
		List<String> list = Stream.of(message.split(" ")).collect(Collectors.toList());
		String s = "";
		
		if(list.get(0) == "NICK" && user.getNick() == null) {
			user.setNick(list.get(1));
		}
		
		if(list.get(0) == "USER" && user.getUser() == null) {
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
		
		if ((list.get(0) != "USER" || list.get(0) != "NICK") 
				&& (user.getUser() == null || user.getNick() == null)) {
			receiver.receiverShutdown();
			System.exit(0);
		}
		
		if(user.getUser() == null && user.getNick() == null) {
//			Parser.check(user);
		}
		
	}

	@Override
	public void shutdown() {
		JOptionPane.showMessageDialog(null, "Socket Input wird geschlossen!", peer.toString(),
				JOptionPane.OK_OPTION); 
//		System.exit(0);
	}

}
