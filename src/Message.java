//Michael Murray, Gaston Gonzalez, Felica Yau


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;


public class Message {

	public final static byte aliveID = -1;

	public final static byte chokeID = 0;

	public final static byte unchokeID = 1;

	public final static byte interestedID = 2;

	public final static byte uninterestedID = 3;

	public final static byte haveID = 4;

	public final static byte bitfieldID = 5;

	public final static byte requestID = 6;

	public final static byte pieceID = 7;

	public final static byte cancelID = 8;

	public final byte id;

	public final int length;

	public final int smallpayload;

	public final int smallpayload2;

	public final int smallpayload3;

	public final byte[] bigpayload;

	// used for alive, choke, unchoke, interested, and uninterested messages
	public Message(final int length, final byte id) {
		this.id = id;
		this.length = length;
		bigpayload = null;
		smallpayload = -1;
		smallpayload2 = -1;
		smallpayload3 = -1;
	}
	// used for piece messages
	public Message(final int length, final byte id, byte[] bigpayload) {
		this.id = id;
		this.length = length;
		this.bigpayload = bigpayload;
		smallpayload = -1;
		smallpayload2 = -1;
		smallpayload3 = -1;
	}
	// used for have messages, smallpayload is piece number
	public Message(final int length, final byte id, int smallpayload) {
		this.id = id;
		this.length = length;
		bigpayload = null;
		this.smallpayload = smallpayload;
		smallpayload2 = -1;
		smallpayload3 = -1;
	}
	// used for piece messages
	public Message(final int length, final byte id, int smallpayload, int smallpayload2, byte[] bigpayload) {
		this.id = id;
		this.length = length;
		this.bigpayload = bigpayload;
		this.smallpayload = smallpayload;
		this.smallpayload2 = smallpayload2;
		smallpayload3 = -1;
	}
	// used for request and cancel messages
	public Message(final int length, final byte id, int smallpayload, int smallpayload2, int smallpayload3) {
		this.id = id;
		this.length = length;
		bigpayload = null;
		this.smallpayload = smallpayload;
		this.smallpayload2 = smallpayload2;
		this.smallpayload3 = smallpayload3;
	}


	/***
	 * 
	 * @param input input stream to read from
	 * @return a message depeneding upon what we read in. 
	 * @throws IOException
	 */

	public static Message decode(DataInputStream input)
			throws IOException {
		int length = input.readInt();

		if (length == 0) {
			System.out.println("keep alive");
			Message keepalive = new Message(0, aliveID);
			return keepalive;
		}

		byte id = input.readByte();
		//System.out.println("id: " + id);
		switch (id) {
		case (chokeID):
			return new Message(length, chokeID);
		case (unchokeID):
			System.out.println("UNCHOKE ME");
			return new Message(length, unchokeID);
		case (interestedID):
			return new Message(length, interestedID);
		case (uninterestedID):			
			return new Message(length, uninterestedID);
		case (haveID):
			return new Message(length, haveID, input.readInt());
		case (bitfieldID):
			byte[] bitfield = new byte[length - 1];
		input.readFully(bitfield);
		return new Message(length, bitfieldID, bitfield);
		case (pieceID):
			//System.out.println(length+ " length");
			int index = input.readInt();
		int start = input.readInt();
		byte[] block = new byte[length - 9];
		input.readFully(block);
		return new Message(length, pieceID, index, start, block);
		case (requestID):
			int index2 = input.readInt();
		int start2 = input.readInt();
		int length2 = input.readInt();
		return new Message(length, requestID, index2, start2, length2);
		case (cancelID):
			int index3 = input.readInt();
		int start3 = input.readInt();
		int length3 = input.readInt();
		return new Message(length, cancelID, index3, start3, length3);
		}

		return null;
	}
	
	/***
	 * encodes are message. simplifies the process of using the peer messages
	 * @param message Message that we are passing in the be encoded
	 * @param output the outputstream we use read from the peer
	 * @throws IOException
	 */

	static void encode(final Message message, DataOutputStream output)	throws IOException {

		if (message != null) {
			{
				ByteBuffer sending = ByteBuffer.allocate(4+message.length);
				sending.putInt(message.length);
				System.out.println(message.id + " " + message.smallpayload + " "+ message.smallpayload2+ " "+ message.bigpayload+ " "+message.smallpayload3);
				if (message.length > 0) {
					sending.put(message.id);
					if (message.smallpayload != -1) sending.putInt(message.smallpayload);
					if (message.smallpayload2 != -1) sending.putInt(message.smallpayload2);
					if (message.bigpayload != null) sending.put(message.bigpayload);
					if (message.smallpayload3 != -1) sending.putInt(message.smallpayload3);
				}
				output.write(sending.array());
				output.flush();

			}
		}
	}
	/***
	 * 
	 * @param messagepayload the payload(size) of the information
	 * @param output the output stream used
	 * @throws IOException
	 */
	
	static void encodewitharray(ByteBuffer messagepayload, DataOutputStream output)	throws IOException {
				output.write(messagepayload.array());
				output.flush();
	}
	
	/***
	 * 
	 * @param message the message we are going to be parsing
	 * @param peer the current peer we use to check if certain things are going on
	 * @return a message based upon choking,unchoking, etc
	 */
	public static Message parse(Message message, Peer peer) {
		byte id;

		if (message.length == 0) id = aliveID;
		else id = message.id;

		switch (id) {
		case (aliveID):
			return new Message(0, aliveID);
		case (chokeID):
			System.out.println("choke!!");
			peer.peerInfo.they_choking = true;
		return null;
		case (unchokeID):
			peer.peerInfo.they_choking = false;
		//todo: maybe send request?
		//peer.download(peer.peerInfo.track, peer);
		return null;
		case (interestedID):
			peer.peerInfo.they_interested = true;
		return new Message(1, unchokeID);
		case (uninterestedID):
			peer.peerInfo.they_interested = false;
		return null;
		case (haveID):
			peer.peerInfo.haspiece[message.smallpayload] = true;
		return null;
		case (bitfieldID):
			peer.peerInfo.bitfield = message.bigpayload;
		peer.peerInfo.haspiece = new boolean[peer.peerInfo.track.numPieces];
		for (int i = 0; i < peer.peerInfo.track.numPieces; i++){
			int j = i;
			int k = 0;
			byte b = 0;
			while (j>=8) {
				j-=8;
				k++;
			}
			b = (byte) (peer.peerInfo.bitfield[k] << j);


			if (b < 0) peer.peerInfo.haspiece[i] = true;
			else peer.peerInfo.haspiece[i] = false;
		}
		if (peer.incompleteBitfield(peer.bitfield) == true) return new Message(1, interestedID);
		else return new Message(1, uninterestedID);
		case (pieceID):
			if (message.smallpayload2 > 0) {

				return new Message(5, haveID, message.smallpayload);
			}

			else return null;
		case (requestID):
			System.out.println("got request:" + message.smallpayload);
			try {
				peer.peerInfo.thefile.seek(peer.peerInfo.track.pieceLength*message.smallpayload+message.smallpayload2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		byte[] piece = new byte[message.smallpayload3];
		try {
			peer.peerInfo.thefile.readFully(piece);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (message.smallpayload != peer.peerInfo.track.numPieces - 1) {
		        peer.peerInfo.track.uploaded += peer.peerInfo.track.pieceLength;
		        peer.peerInfo.downloaded += peer.peerInfo.track.pieceLength;
		    }
		    else {
		        peer.peerInfo.track.uploaded += peer.peerInfo.track.fileLength%peer.peerInfo.track.pieceLength;
		        peer.peerInfo.downloaded += peer.peerInfo.track.fileLength%peer.peerInfo.track.pieceLength;
		    }
		return new Message(message.smallpayload3+9, pieceID, message.smallpayload, message.smallpayload2, piece);
		case (cancelID):
			//todo?: stop sending piece
			return null;
		}
		return null;

	}
	
	public static byte[] blocksaver(Message piecemessage, Tracker track){
		byte[] theblock = new byte[piecemessage.length-9];
		theblock = piecemessage.bigpayload;
		return theblock;
	}

}