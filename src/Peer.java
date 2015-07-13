//Michael Murray, Gaston Gonzalez, Felica Yau



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Peer{

	DataInputStream input;
	DataOutputStream output; 
	int downloaded;
	boolean[] bitfield;
	PeerInformation peerInfo;
	byte[] bfield;
	int pieces;

	public Peer(boolean [] bitfield, byte[] bfield, PeerInformation peerInfo){

		this.input=null;
		this.output=null;
		this.downloaded = 0;
		this.bitfield = bitfield;
		this.peerInfo = peerInfo;
		this.bfield = bfield;


	}

	//Starts handshake with tracker

	public synchronized static byte[] handshake(Tracker track){

		byte[] handshake = new byte[68];
		int currPosition=1;

		//begins with byte 19
		handshake[0] = 0x13;

		//next add the String "BitTorrent protocol"
		String protocol = "BitTorrent protocol";
		for(int x = 0;x<protocol.length();x++){
			handshake[x+1] = (byte) protocol.charAt(x);
		}

		//adding the 8 empty slots 
		currPosition = protocol.length()+1;
		int a=0;
		for(currPosition=currPosition;a<8;currPosition++,a++){
			handshake[currPosition] = 0;
		}

		//20-byte SHA-1 hash 
		for(int y = 0;y<20;y++){
			handshake[currPosition] = track.info_hash[y];
			currPosition++;
		}

		//next is the peer id. Since ours is just gggg... just hardcoded it but we can change this later
		for(int y=0;y<20;y++){
			handshake[currPosition] = (byte) track.userID.charAt(y);
			currPosition++;
		}
		return handshake;
	}
	



	//Create's a socket to the ip/port of tracker

	public synchronized boolean createSocket(Tracker track,String ip,int port){

		Socket sock = null;
		System.out.println("create");
		try {


			sock = new Socket(ip,port);
			byte[] handshake = handshake(track);
			System.out.println(sock.isConnected());
			input = new DataInputStream(sock.getInputStream());
			System.out.println(input);
			if(	 checkValidHandshake(track.info_hash,input)==false){
				System.out.println("Invalid handshake");
				return false;
			}else{
				System.out.println("Valid Handshake");
			}

			output= new DataOutputStream(sock.getOutputStream());

			output.write(handshake);
			peerInfo.handshake = true;

			output.flush();
			
			return true;
		} catch (IOException e) {
			System.out.print("Couldn't complete handshake");
			return false;
		}
	}

	public byte[] bitfieldFinder(DataInputStream input2, DataOutputStream output, int pieces){
		/*byte [] bf = {0,0,0,0,0};
		Message bfield = new Message(6, (byte)5, bf);
		try {
			Message.encode(bfield, output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		int piecebytes;
		if (pieces%8 != 0) piecebytes = pieces/8 +1;
		else piecebytes = pieces/8;
		//System.out.println("piecebytes: " +piecebytes);
		byte[] correctbitfield = new byte[piecebytes];
		for (int x = 0; x < piecebytes-1; x++){
			correctbitfield[x] = -1;
		}

		correctbitfield[piecebytes-1] = (byte) (-1 - (8-pieces%8));

		Message bitfieldmessage = null;
		try {
			
			bitfieldmessage = Message.decode(input2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bitfield = new byte[bitfieldmessage.length-1];
		bitfield = bitfieldmessage.bigpayload;
		for (int x = 0; x < piecebytes; x++){
			//System.out.print(bitfield[x]);
			//if (correctbitfield[x] != bitfield[x]) return false;
		}
		System.out.println("after bitfield");
		return bitfield;

	}
	//sends interested, checks for unchoke

	public synchronized boolean communicationSeed(Peer peer){

		Message interested = new Message(1,(byte)2);
		try {
			Message.encode(interested, peer.output);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Message unchokemessage = null;
		try {
			unchokemessage = Message.decode(peer.input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (unchokemessage.id == 1){
			peer.peerInfo.they_choking = false;
			return true;
		}

		return false;

	}




	public void download(Tracker track, Peer peer){
		int blocksPerPiece = track.pieceLength/16384;
	
			ArrayList<boolean[]> listRays = new ArrayList<boolean[]>();
			listRays.add(peerInfo.haspiece);


			int testint=0;
			int x = bitfield.length;
			
			int badCount = 0;
			
				
			RarestPiece rarestPiece = new RarestPiece(listRays,x);
			rarestPiece.rarestPieceOrdering();
			ArrayList<index> pieceOrdering = rarestPiece.pieceToDownload();
			for(int a = 0;a<pieceOrdering.size();a++){
				if(bitfield[pieceOrdering.get(a).index]==false){
					try {
						if(getOnePiece(track, track.fileLength%(16384*blocksPerPiece), peer, blocksPerPiece, track.piece_hashes, pieceOrdering.get(a).index)==true){
							bitfield[pieceOrdering.get(a).index]=true;
						}
						else{
							badCount++;
							a--;
							if(badCount==2){
								System.out.println("bad peer");
								peerInfo.badPeer=true;
								break;
							}
						}
					} catch (NoSuchAlgorithmException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
			//getAllPieces(track, track.fileLength%(16384*blocksPerPiece), input, output, blocksPerPiece, track.piece_hashes);
	}

	
	public static boolean checkPiece(Tracker track, Peer peer, int x, byte[] piece){
		MessageDigest digest2 = null;
		try {
			digest2 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		digest2.update(piece);
		byte[] piecehash = digest2.digest();
		String stringpiece = new String(piecehash);
		byte[] hashbyte = peer.peerInfo.track.piece_hashes[x].array();
		String hash = new String(hashbyte);
		int compare = stringpiece.compareTo(hash);
		if (x == track.numPieces-1){
			System.out.println(stringpiece);
			System.out.println(hash);
		}

		
		if (compare == 0) {
			System.out.println("downloaded piece number "+ (x+1) );
			peer.downloaded += piece.length;
			peer.bitfield[x] = true; 
		
		
			try {
				peer.peerInfo.thefile.seek(peer.peerInfo.track.pieceLength*x);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				peer.peerInfo.thefile.write(piece);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public static boolean getOnePiece(Tracker track, int lastsize, Peer peer, int blocksPerPiece, ByteBuffer[] piece_hashes, int x) throws IOException, NoSuchAlgorithmException{
		int varsize = 0;
		byte[] thepiece = null;
		if (x != (track.numPieces-1)) thepiece = new byte[16384*blocksPerPiece];
		else thepiece = new byte[lastsize];
		boolean badmessage = false;

		for (int y = 0; y < blocksPerPiece; y++){
			//figure out requested blocksize
			if (x == (track.numPieces - 1)){
				if (lastsize <= 16384) {varsize = lastsize;
				//System.out.println("firstcase");}
				}
				else if ((lastsize > 16384) && y < blocksPerPiece-1) {varsize = 16384;
				//System.out.println("secondcase");}
				}
				else if ((lastsize > 16384) && y == blocksPerPiece-1) {varsize = lastsize - 16384*(y);
				//System.out.println("thirdcase");}
				}
			}
			else varsize = 16384;

			byte[] theblock = new byte[varsize];

			//send request
			Message requestmessage = new Message(13,(byte)6, x, 16384*y, varsize);
			try {
				Message.encode(requestmessage, peer.output);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Message piecemessage = null;
			try {
				piecemessage = Message.decode(peer.input);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (piecemessage.length!=varsize+9) {
				//System.out.println("bad length");
				badmessage = true;
				break;
			}
			if (piecemessage.id!=7) {
				badmessage = true;
				//System.out.println("bad id");
				break;
			}
			if (piecemessage.smallpayload!=x) {
				badmessage = true;
				//System.out.println("bad piece");
				break;
			}
			if (piecemessage.smallpayload2!=16384*y) {
				badmessage = true;
				//System.out.println("bad offset");
				break;
			}
			theblock = piecemessage.bigpayload;
			System.arraycopy(theblock, 0, thepiece, 16384*y, varsize);
			if ((lastsize <= 16384) && (x == (track.numPieces-1))) y = blocksPerPiece;
		}
		//Converts to SHA-1 hash, that way it can be compared to what has been given by the tracker
		if (!badmessage){
			MessageDigest digest2 = MessageDigest.getInstance("SHA-1");
			digest2.update(thepiece);
			byte[] piecehash = digest2.digest();
			String stringpiece = new String(piecehash);
			byte[] hashbyte = piece_hashes[x].array();
			String hash = new String(hashbyte);
			int compare = stringpiece.compareTo(hash);

			System.out.println("downloaded piece number "+ (x+1) );
			if (compare!=0) return false;
			else {
				peer.downloaded += thepiece.length;
				peer.bitfield[x] = true;
				Message havemessage = new Message(5,(byte)4, x);
				try {
					Message.encode(havemessage, peer.output);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				peer.peerInfo.thefile.seek(16384*x*blocksPerPiece);
				peer.peerInfo.thefile.write(thepiece);
				return true;
			}
		}
		return false;

	}

	//ensures a valid handshake

	public boolean incompleteBitfield(boolean[] bitfield){
		int count=0;
		for(int y = 0;y<bitfield.length;y++){
			if(bitfield[y]==true){
				count++;
			}
		}
		for(int x = 0;x<bitfield.length;x++){
			if(bitfield[x]==false){
				System.out.println(count+ " pieces downloaded");
				return true;
			}

		}
		System.out.println(count+ " pieces downloaded");
		return false;
	}

	public static boolean checkValidHandshake(byte[] info,DataInputStream input) throws IOException{


		byte[] response = new byte[68];
		input.readFully(response);

		for(int x =0;x<20;x++){
			if(info[x]!=response[x+28]){
				return false;

			}
		}return true;
	}

}