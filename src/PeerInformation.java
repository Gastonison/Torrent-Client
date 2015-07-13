//Michael Murray, Gaston Gonzalez, Felica Yau


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;


public class PeerInformation {
	
	String name;
	int port;
	String ip;
	boolean we_choking;
	boolean they_choking;
	boolean we_interested;
	boolean they_interested;
	boolean bad;
	byte[] bitfield;
	boolean[] haspiece;
	Socket userSocket;
	DataInputStream input;
	DataOutputStream output;
	boolean handshake;
	int indexInTable;
	boolean badPeer;
	RandomAccessFile thefile;
	Tracker track;

	public int downloaded;
	int uploaded;
	
	PeerInformation(String name, int port, String ip, RandomAccessFile thefile, Tracker track){
		
		this.name=name;
		this.port=port;
		this.ip=ip;
		this.bad = false;
		we_choking=true;
		they_choking=true;
		we_interested=false;
		they_interested=false;
		userSocket=null;
		input=null;
		output=null;
		handshake=false;
		indexInTable=-1;
		badPeer=false;
		this.track = track;
		this.thefile = thefile;
		downloaded=0;
		uploaded=0;
	
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isWe_choking() {
		return we_choking;
	}

	public void setWe_choking(boolean we_choking) {
		this.we_choking = we_choking;
	}

	public boolean isThey_choking() {
		return they_choking;
	}

	public void setThey_choking(boolean they_choking) {
		this.they_choking = they_choking;
	}

	public boolean isWe_interested() {
		return we_interested;
	}

	public void setWe_interested(boolean we_interested) {
		this.we_interested = we_interested;
	}

	public boolean isThey_interested() {
		return they_interested;
	}

	public void setThey_interested(boolean they_interested) {
		this.they_interested = they_interested;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void setBitfield(byte[] bitfield) {
		this.bitfield = bitfield;
	}

	public boolean[] getHaspiece() {
		return haspiece;
	}

	public void setHaspiece(boolean[] haspiece) {
		this.haspiece = haspiece;
	}

	public Socket getUserSocket() {
		return userSocket;
	}

	public void setUserSocket(Socket userSocket) {
		this.userSocket = userSocket;
	}

	public DataInputStream getInput() {
		return input;
	}

	public void setInput(DataInputStream input) {
		this.input = input;
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public void setOutput(DataOutputStream output) {
		this.output = output;
	}

	public boolean isHandshake() {
		return handshake;
	}

	public void setHandshake(boolean handshake) {
		this.handshake = handshake;
	}

	public int getIndexInTable() {
		return indexInTable;
	}

	public void setIndexInTable(int indexInTable) {
		this.indexInTable = indexInTable;
	}
	

	

}
