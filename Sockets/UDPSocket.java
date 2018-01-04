package Sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.swing.JOptionPane;

import Programms.ConnectionStatus;

public class UDPSocket implements SocketI{
	// private String peer;
	private String adresse = "";
	private int port = 0;
	private DatagramSocket socket;
	protected InetAddress curPartnerIP;
	protected int curPartnerPort;

	public UDPSocket(String adresse, int port) {
		this.adresse = adresse;
		this.port = port;
		try {
			// peer = "client";
			socket = new DatagramSocket(port);
			JOptionPane.showMessageDialog(null, "Client wurde gestartet!", "Client", JOptionPane.OK_OPTION);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Client Exception", JOptionPane.OK_OPTION);
			socket.close();
		}
		ConnectionStatus.connected = true;
	}

	public UDPSocket(int port) {
		this.port = port;
		ConnectionStatus.connected = false;
		try {
			// peer = "server";
			socket = new DatagramSocket(port);
			JOptionPane.showMessageDialog(null, "Server wurde gestartet!", "Server", JOptionPane.OK_OPTION);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Server Exception", JOptionPane.OK_OPTION);
			socket.close();
		}
	}

	public int getPort() {
		return port;
	}

	public String getAddress() {
		return adresse;
	}

	public void connect(InetAddress address, int port) {
		socket.connect(address, port);
	}

	public void disconnect() {
		socket.disconnect();
		ConnectionStatus.connected = false;
	}

	public void send(String message) {
		try {
			if (this.socket == null)
				throw new Exception("Kein Socket gefunden.");
			InetSocketAddress adr = new InetSocketAddress(adresse, port);
			DatagramPacket packetOut = new DatagramPacket(message.getBytes(), message.getBytes().length, adr);
			this.socket.send(packetOut);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String receive(int maxBytes) {
		String ret = "";
		try {
			if (this.socket == null)
				throw new Exception("Kein Socket gefunden.");
			DatagramPacket packetIn = new DatagramPacket(new byte[maxBytes], maxBytes);

			this.socket.receive(packetIn);

			if (!ConnectionStatus.connected) {
				connect(packetIn.getAddress(), packetIn.getPort());
				ConnectionStatus.connected = true;
				this.adresse = packetIn.getAddress().getHostAddress();
				this.port = packetIn.getPort();
			}
			ret = new String(packetIn.getData(), 0, packetIn.getLength());
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	@Override
	public void shutdownInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdownOutput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InetAddress getPartnerAdress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InetAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
