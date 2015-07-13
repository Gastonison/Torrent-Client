//Michael Murray, Gaston Gonzalez, Felica Yau


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;


public class RUBTClient {
	

	//Takes arguments, opens sockets/connections to peers, starts downloads, closes connections

	public static void main(String[] args) throws IOException, InterruptedException{

		System.out.println(Tracker.randomString());

		if(args.length!=2){
			System.out.println("Invalid args");
		}
		// takes arguments
		String torrentName = args[0];
		String fileName = args[1];

		Tracker track = new Tracker(torrentName);

		Map<ByteBuffer,Object> tracking = track.connectTracker("&event=started");	
		
		ArrayList<Map<ByteBuffer,Object>> list = track.getPeerList(tracking);

		int listSize = list.size();

		ArrayList<byte[]> bitfields = new ArrayList<>();
		RandomAccessFile thefile=null;
		try {
            thefile = new RandomAccessFile(fileName, "rw");
            thefile.setLength(track.fileLength);
       } catch (Exception e) {
            System.err.println(e);
       }
		System.out.println(track.numPieces);
		boolean[] bitfield = new boolean[track.numPieces];
		for (int x = 0; x < track.numPieces; x++) {
			byte[] piece = null;
			if (x == track.numPieces -1) piece = new byte[track.fileLength%track.pieceLength];
			else piece = new byte[track.pieceLength];
			thefile.readFully(piece);
			MessageDigest digest2 = null;
			try {
				digest2 = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e1) {
				System.out.println("couldnt create the digest");
			}
			digest2.update(piece);
			byte[] piecehash = digest2.digest();
			String stringpiece = new String(piecehash);
			byte[] hashbyte = track.piece_hashes[x].array();
			String hash = new String(hashbyte);
			int compare = stringpiece.compareTo(hash);
		    if (compare == 0) {
		        bitfield[x] = true;
		        if (x != track.numPieces - 1) track.downloaded += track.pieceLength;
		        else if(track.downloaded==track.fileLength) break;
		        else track.downloaded += track.fileLength%track.pieceLength;
		      }
			else bitfield[x] = false;
		}
		int fieldsize = 0;
		int piecemax = track.numPieces;
		while (piecemax > 0){
			fieldsize++;
			piecemax -= 8;
		}
		byte [] bfield = new byte[fieldsize];
		int [] bits = new int[8];
		byte b = 0;
		for (int x = 0; x < bfield.length; x++){
			b=0;
			for (int y = 0; y < 8; y++){
				if (x*8+y < track.numPieces){
					if (bitfield[x*8+y] == true) bits[y] = 1;
					else bits[y] = 0;
				}
				else bits[y] = 0;
				b += (bits[y]<<(7-y));
			}
			bfield[x]=b;
			System.out.println(b);
		}
		int count=0;
		ArrayList<Peer> plist= new ArrayList<Peer>();
		GUI gui = new GUI(plist,thefile);
		gui.setVisible(true);
		gui.setName(args[0]);
		gui.setDownload(track.downloaded);
		gui.setUpload(track.uploaded);
		gui.totalPiece(track.numPieces);
		gui.fileSize(track.fileLength);
		for (int x = 0; x < list.size(); x++) {
			
			String ip = Tracker.trackerResponseip(list, x);
			count++;
			int port = Tracker.trackPort(list, x);
			PeerInformation currentPeer = new PeerInformation("",port,ip, thefile, track);
			plist.add(new Peer(bitfield, bfield, currentPeer));
			gui.addPeer(plist.get(x).peerInfo, x);
			plist.get(x).peerInfo.indexInTable=x;
		}
		
	
		for(int x = 0;x<plist.size();x++){
			
			plist.get(x);
			Download o = new Download(plist, track, x,gui);
			o.Start();
			o.ContactTrackerThread.start();
			Thread.sleep(1000);
		}
		
		gui.setName(args[0]);
		gui.setDownload(track.downloaded);
		while(true){
			if(track.downloaded<=track.fileLength){
			gui.setDownload(track.downloaded);
			gui.increaseActionBar(track.downloaded, track.fileLength);
			
			}
			for(int x = 0;x<plist.size();x++){
				gui.addPeer(plist.get(x).peerInfo, x);
				plist.get(x).peerInfo.indexInTable=x;
			}
			
			
	
		}
		
	}

	//Saves file! (and gives great recommendation in case of failure)!

	public static void save(ArrayList<byte[]> listPieces, String fileName) {

		try {
			BufferedOutputStream awesomeOutput = new BufferedOutputStream(new FileOutputStream(fileName));
			for (byte[] piece : listPieces) {
				awesomeOutput.write(piece);
			}
			awesomeOutput.flush();
			awesomeOutput.close();
		} catch (IOException e) {
			System.out.println("Couldn't save file. Go get uTorrent");
		}
	}
	

}
