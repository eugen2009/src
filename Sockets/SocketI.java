package Sockets;

import java.net.InetAddress;

public interface SocketI {
	
	public int getPort();

	public String getAddress();	
	
	public InetAddress getPartnerAdress();
	
	public void send(String message); 
	
	public String receive(int maxBytes);
	
	public void shutdownInput();
	
	public void shutdownOutput();

	public InetAddress getLocalAddress();
	
}
