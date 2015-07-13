//Michael Murray, Gaston Gonzalez, Felica Yau



import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



// sets up Tracker/Port/IP/Connection/Requests

public class Tracker {

	TorrentInfo TorrentInfo;
	
	

	int Port;
	int Interval;
	byte[] info_hash;
	int fileLength;
	int pieceLength;
	URL Url;
	int numPieces;
	ByteBuffer[] piece_hashes;
	static String userID;
	int downloaded;
	int uploaded;
    
	public Tracker(String torrentName){
		try {
			Path path = Paths.get(torrentName);
			byte[] data = Files.readAllBytes(path);
			TorrentInfo = new TorrentInfo(data);
		} catch (BencodingException | IOException e) {
			System.out.println("Invalid Torrent File");
		}
          
		Url = TorrentInfo.announce_url;
		info_hash=TorrentInfo.info_hash.array();
		fileLength = TorrentInfo.file_length;
		pieceLength = TorrentInfo.piece_length;
		if (fileLength%pieceLength != 0) numPieces = fileLength/pieceLength + 1;
		else numPieces = fileLength/pieceLength;
		piece_hashes = TorrentInfo.piece_hashes;
	
	}

	public static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F'};


	//------------------------------------------------------------------------------------
	//-------------Create the URL and return some stuff for other methods-----------------
	//------------------------------------------------------------------------------------

    // Connects to tracker, sends GET request
	public Map<ByteBuffer, Object> connectTracker(String event){

		int downloaded, left;
		if (event.compareTo("&event=started") == 0) downloaded = 0;
		else downloaded = TorrentInfo.file_length;
		left = TorrentInfo.file_length - downloaded;
		URL newURL = null;
        
        // sets up the GET request to http
        
		String newUrlString = TorrentInfo.announce_url 
				+ "?info_hash=" + toHexString(TorrentInfo.info_hash.array())
				+ "&peer_id=" + toHexString((userID).getBytes()) 
				+ "&port=" + getPort()
				+ "&uploaded=" + 0 
				+ "&downloaded=" + downloaded  
				+ "&left=" + left;

		try {
			newURL = new URL(newUrlString+event);

		} catch (MalformedURLException e) {
			System.out.println("Couldn't create the URL");
		}
        
		int port = newURL.getPort();
		HttpURLConnection connect = null;
		String host = newURL.getHost();
		HashMap decodedResponse = null;
        
        // opens connection to host
		try {
			connect = (HttpURLConnection)newURL.openConnection();
		} catch (IOException e) {
			System.out.println("Error trying to connect");
		}

		connect.getAllowUserInteraction();
		try {
			connect.getPermission();
		} catch (IOException e) {
			System.out.println("couldn't connect");		}
		try {
			newURL.openStream();
		} catch (IOException e) {
			System.out.println("couldn't open stream");
		}
		DataInputStream dataInputStream=null;
		try {
			dataInputStream = new DataInputStream(connect.getInputStream());
		} catch (IOException e) {
			System.out.println("Couldn't connect");
		}

		int dataSize = connect.getContentLength();

		//returned from the tracker. it's a dictionary of the interval and peer list.
		byte[] retArray = new byte[dataSize];

		try {
			dataInputStream.readFully(retArray);
		} catch (IOException e) {
			System.out.println("Couldn't read");
		}
		try {
			dataInputStream.close();
		} catch (IOException e) {
			System.out.println("Couldn't close");
		}
		Map<ByteBuffer, Object> trackArray = null;
		try {
			trackArray = (Map<ByteBuffer,Object>) Bencoder2.decode(retArray);
		} catch (BencodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connect.disconnect();
		return trackArray;
	}

	//------------------------------------------------------------------------------------
	//----------------------------Get the IP from response--------------------------------
	//-----------------------------------------------------------------------------------
	public static String trackerResponseip(ArrayList<Map<ByteBuffer,Object>> peerList,int curr){
		String ipAddress = null;
		
		
		
			ByteBuffer peerID = ByteBuffer.wrap(new byte[]{ 'p', 'e', 'e', 'r',' ','i','d'});
			

			byte[] peerid = ((ByteBuffer) peerList.get(curr).get(peerID)).array();
			String peerString = new String(peerid);
			//System.out.println(peerString);
				byte[] peerip = ((ByteBuffer) peerList.get(curr).get(ByteBuffer.wrap(new byte[] {'i','p'}))).array();
				String ipstring = new String(peerip);
				ipAddress = ipstring;
			
				//System.out.println("IP: "+ipAddress);

			
			return ipAddress;
		
	}
		
	public static String trackerResponseIP(ArrayList<Map<ByteBuffer,Object>> peerList){

		String ipAddress = null;
		for(int i = 0;i<peerList.size();i++){
			ByteBuffer peerID = ByteBuffer.wrap(new byte[]{ 'p', 'e', 'e', 'r',' ','i','d'});
			

			byte[] peerid = ((ByteBuffer) peerList.get(i).get(peerID)).array();
			String peerString = new String(peerid);
			//System.out.println(peerString);
			if(peerString.charAt(1)=='R'&&peerString.charAt(2)=='U'&&peerString.charAt(3)=='1'&&peerString.charAt(4)=='1'&&peerString.charAt(5)=='0'&&peerString.charAt(6)=='3'){
				byte[] peerip = ((ByteBuffer) peerList.get(i).get(ByteBuffer.wrap(new byte[] {'i','p'}))).array();
				String ipstring = new String(peerip);
				ipAddress = ipstring;
				System.out.println("peers name is "+ peerString);
				//System.out.println("IP: "+ipAddress);

			}
		}

		return ipAddress;
	}

	//------------------------------------------------------------------------------------
	//----------------------------Get the Port from response------------------------------
	//------------------------------------------------------------------------------------

	public static int trackPort(ArrayList<Map<ByteBuffer,Object>> peerList,int curr){
		int portNumber = 0;
			ByteBuffer peerID = ByteBuffer.wrap(new byte[]{ 'p', 'e', 'e', 'r',' ','i','d'});

			byte[] peerid = ((ByteBuffer) peerList.get(curr).get(peerID)).array();
			String peerString = new String(peerid);
			
				int peerport = (int) peerList.get(curr).get(ByteBuffer.wrap(new byte[] {'p','o','r','t'}));
				int portString = (peerport);
				portNumber = portString;

				System.out.println("Port number: "+peerport);
			
		

		return portNumber;
	}
	
	
	public static int trackerResponsePort(ArrayList<Map<ByteBuffer,Object>> peerList){

		int portNumber = 0;
		for(int i = 0;i<peerList.size();i++){
			ByteBuffer peerID = ByteBuffer.wrap(new byte[]{ 'p', 'e', 'e', 'r',' ','i','d'});

			byte[] peerid = ((ByteBuffer) peerList.get(i).get(peerID)).array();
			String peerString = new String(peerid);
			if(peerString.charAt(1)=='R'&&peerString.charAt(2)=='U'&&peerString.charAt(3)=='1'&&peerString.charAt(4)=='1'&&peerString.charAt(5)=='0'&&peerString.charAt(6)=='3'){
				int peerport = (int) peerList.get(i).get(ByteBuffer.wrap(new byte[] {'p','o','r','t'}));
				int portString = (peerport);
				portNumber = portString;

				System.out.println("Port number: "+peerport);
			}
		}

		return portNumber;
	}

	//------------------------------------------------------------------------------------
	//----------------------------Get the Interval ---------------------------------------
	//------------------------------------------------------------------------------------

	public int getInterval(Map<ByteBuffer, Object> trackerInfo){
		int interval = (Integer)(trackerInfo.get(ByteBuffer.wrap(new byte[] {'i','n','t','e','r','v','a','l'})));
		return interval;
	}

	//------------------------------------------------------------------------------------
	//----------------------------Get the Peer List from response-------------------------
	//------------------------------------------------------------------------------------


	public ArrayList<Map<ByteBuffer,Object>> getPeerList(Map<ByteBuffer,Object> trackerInfo){
		ArrayList<Map<ByteBuffer, Object>> peerList = (ArrayList<Map<ByteBuffer, Object>>)(trackerInfo.get(ByteBuffer.wrap(new byte[] {'p','e','e','r','s'})));
		return peerList;
	}


// tries to connect to tracker at port # 6881

	public static int getPort(){
		ServerSocket serverSocket;
		int max =8;
		int currentPort = 6881;
		for(int x = 0;x<max;x++){
			try {
				serverSocket = new ServerSocket(currentPort);
				System.out.println("Connected to tracker on port "+ currentPort);
				serverSocket.close();
				return currentPort;
			} catch (IOException e) {
				System.out.println("trying a different Port " + (currentPort+1));
				currentPort++;
			}
		}
		System.out.println("Couldn't connect");
		return 0;
	}

    //converts strings to hexstrings


	public static String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		if (bytes.length == 0) {
			return "";
		}

		StringBuilder hex = new StringBuilder(bytes.length * 3);

		for (byte b : bytes) {
			byte hi = (byte) ((b >> 4) & 0x0f);
			byte lo = (byte) (b & 0x0f);

			hex.append('%').append(HEX_CHARS[hi]).append(HEX_CHARS[lo]);
		}
		return hex.toString();
	}
	
    //Creates a random user ID to help ensure no overlapping
    
	public static String randomString(){
		String user="";
		
		for(int x =0;x<20;x++){
			int R = (int) ((Math.random() * (97 - 122)) + 122);
			user=user+ ((char)R);
		}
		userID = user;
		return user;
	}

}
