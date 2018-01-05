package IRCClasses;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import Sockets.SocketI;

public class User {
	private static List<User> users = new LinkedList<User>();
	
	private String user;
	private String nick;
	private String fullName;
	private InetAddress curPartnerIP;
	private SocketI userSocket;
	private boolean enable = false;


	public User(String user, String nick, String fullName, SocketI userSocket) {
		this.user = user;
		this.nick = nick;
		this.fullName = fullName;
		this.userSocket = userSocket;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getNick() {
		return nick;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public void setCurPartnerIP(InetAddress address) {
		curPartnerIP = address;
	}
	
	public InetAddress getCurPartnerIP() {
		return curPartnerIP;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean getEnable() {
		return enable;
	}


	public static boolean checkUser(User user) {
		return true;
	}
	public static void addUser(User user) {
		synchronized (user) {
			users.add(user);
			System.out.println("user.added: \n" + user.toString());
		}
	}
	
	public static User getUser(String name) {
		User user = new User(null, null, null, null);
		synchronized(name) {
			
			return user;
		}
	}
	
	public String toString() {
		return "Nick: " + nick + "\nUser: " + user + "\nFullName: " + fullName;
	}

}
