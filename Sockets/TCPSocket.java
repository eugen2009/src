package Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JOptionPane;

import IRCClasses.ThreadClient;
import Programms.ConnectionStatus;

public class TCPSocket implements SocketI {
	// private String peer;
	private static ServerSocket serverSocket;
	private String adresse = "";
	private int port = 0;
	private Socket socket; 
	private PrintWriter out;
	private BufferedReader in;
	protected InetAddress curPartnerIP;
	protected int curPartnerPort;

	public TCPSocket(String adresse, int port) {
		this.adresse = adresse;
		this.port = port;
		try {
			socket = new Socket(adresse, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.fillInStackTrace(), "Socket Exception", JOptionPane.OK_OPTION);
			try {
				socket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		ConnectionStatus.connected = true;
	}

	public TCPSocket(int port) {
		this.port = port;
		ConnectionStatus.connected = false;
		try {
			// server erzeugt mit dem angegebenen Port
			if(serverSocket == null) {
			serverSocket = new ServerSocket(port);
			}
			// Server überprüft, ob eine Verbindung eingeht. Client wird erzeugt
			socket = serverSocket.accept();
//			Thread nextClient = 
					new Thread(new ThreadClient(this)).start();
//			nextClient.start();
			curPartnerIP = socket.getInetAddress();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "ServerSocket Exception", JOptionPane.OK_OPTION);
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public int getPort() {
		return port;
	}
	
	public InetAddress getPartnerAdress() {
		return curPartnerIP;
	}

	public String getAddress() {
		return adresse;
	}

	public void connect(SocketAddress endpoint) {
		try {
			socket.connect(endpoint);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {

		ConnectionStatus.connected = false;
	}
	// BufferedReader oder Scanner zum Lesen
	// oder zu einem DataOutputStream oder PrintWriter zum Schreiben

	/**
	 * BufferedReader in = new BufferedReader(new
	 * InputStreamReader(socket.getInputStream())); PrintWriter out = new
	 * PrintWriter(socket.getOutputStream(), true)
	 */

	public void send(String message) {
		try {
			if (this.socket == null)
				throw new Exception("Kein Socket gefunden.");
			
			out.println(message);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void shutdownInput() {
		try {
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdownOutput() {
		try {
			socket.shutdownOutput();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public InetAddress getLocalAddress() {
		return socket.getLocalAddress();
	}
	
	

	public String receive(int maxBytes) {
		String ret = "";
		try {
			if (this.socket == null)
				throw new Exception("Kein Socket gefunden.");

			if (!ConnectionStatus.connected) {
				 ConnectionStatus.connected = true;
				 
			}
			
//			while((ret = in.readLine()) != null){
			ret = in.readLine();
					
			return ret;
//			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
}
