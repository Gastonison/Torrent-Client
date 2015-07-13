
//Michael Murray, Gaston Gonzalez, Felica Yau

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class Download extends Thread {


	Thread ContactTrackerThread;
	Peer peer2;
	Tracker track;
	ArrayList<Peer> plist;
	GUI gui;
	Download(ArrayList<Peer> plist, Tracker track, int x,GUI gui){
		peer2=plist.get(x);
		this.track = track;
		this.plist = plist;
		this.gui=gui;

	}

	public void Start(){
		
		ContactTrackerThread = new Thread( new Runnable(){

			public void run(){

				//Peer peer2 = new Peer();
				boolean sockethandshake = peer2.createSocket(track, peer2.peerInfo.ip, peer2.peerInfo.port);
			
				if (sockethandshake) {
					try {
						Message.encode(new Message(peer2.bfield.length+1, (byte)5, peer2.bfield), peer2.output);
					} catch (IOException e2) {
						System.out.println("unable to complete ");
					}
				}
				//peer2.peerInfo.bitfield = peer2.bitfieldFinder(peer2.input, peer2.output, track.numPieces);
				peer2.peerInfo.haspiece = new boolean[track.numPieces];
				
				int blocksPerPiece = track.pieceLength/16384;

				ArrayList<boolean[]> listRays = new ArrayList<boolean[]>();
				for (int i = 0; i < plist.size(); i++){
					if (plist.get(i).peerInfo.haspiece != null) listRays.add(plist.get(i).peerInfo.haspiece);
				}

				int x = peer2.bitfield.length;
				int varsize=0;
				int lastsize = track.fileLength%track.pieceLength;
				byte[] piece = null;

				RarestPiece rarestPiece = new RarestPiece(listRays,x);
				rarestPiece.rarestPieceOrdering();
				ArrayList<index> pieceOrdering = rarestPiece.pieceToDownload();
				int b = 0;
				
				
				while ((peer2.peerInfo.bad == false) && (sockethandshake ==true)){
					gui.invalidate();
				
					Message sendmessage = null;
					Message themessage = null;

					if (peer2.peerInfo.they_choking == false){
						if (peer2.incompleteBitfield(peer2.bitfield) == false){
							System.out.println("done!");
							themessage = new Message(1, (byte)3);
							try {
								Message.encode(themessage, peer2.output);
							} catch (IOException e) {
								System.out.println("could not encode, Please don't try again");
							}
						}
						//if (1==2);
						//most important line of code above here
						else {
							
							for(int a=0; a<pieceOrdering.size();){
								int badcount = 0;
								while(peer2.bitfield[pieceOrdering.get(a).index]==true) {
									a++;
									if (a==track.numPieces) break;
								}
								if (a == track.numPieces) break;
								b = pieceOrdering.get(a).index;

								if (b != (track.numPieces-1)) piece = new byte[16384*blocksPerPiece];
								else piece = new byte[lastsize];

								for (int y = 0; y < blocksPerPiece; y++){
									//figure out requested blocksize
									if (b == (track.numPieces - 1)){
										if (lastsize <= 16384) varsize = lastsize;

										else if ((lastsize > 16384) && y < blocksPerPiece-1) varsize = 16384;

										else if ((lastsize > 16384) && y == blocksPerPiece-1) varsize = lastsize - 16384*(y);

									}
									else varsize = 16384;

									//send request
									Message requestmessage = new Message(13,(byte)6, b, 16384*y, varsize);
									try {
										Message.encode(requestmessage, peer2.output);
									} catch (IOException e1) {
								System.out.println("unable to encode");
									}
									
//										Thread.sleep(1);
										try {
											themessage = Message.decode(peer2.input);
										} catch (IOException e) {
										System.out.println("unable to decode");
										}
										if (themessage != null){
											if (themessage.id == 7) {
												System.out.println("in here");
												byte[] block = Message.blocksaver(themessage, track);
												System.arraycopy(block, 0, piece, 16384*y, varsize);
											}
										}
										else break;
										
									if (themessage != null) sendmessage = Message.parse(themessage, peer2);
									if ((lastsize <= 16384) && (b == (track.numPieces-1))) y = blocksPerPiece;
								}
								if (Peer.checkPiece(track, peer2, b, piece) == true) {
									System.out.println("yay");
									
									if (b < track.numPieces-1) track.downloaded += track.pieceLength;
									else track.downloaded += track.fileLength%track.pieceLength;
									try {
										Message.encode(sendmessage, peer2.output);
									} catch (IOException e) {
										System.out.println("unable to encode");
									}
								}
								else badcount++;
								if (badcount == 1) {
									peer2.peerInfo.bad = true;
									System.out.println("bad");
									peer2.peerInfo.haspiece[b] = false;
						
								}
			

								break;
							}
						}
						
					}	

					try {
						if (peer2.input.available() > 0) themessage = Message.decode(peer2.input);
						if (themessage != null) {
							sendmessage = Message.parse(themessage, peer2);
							if (sendmessage != null) Message.encode(sendmessage, peer2.output);
						}

					} catch (IOException e) {
						System.out.println();
					}

			
				}

				

			}
			



		});

	}
	

}
