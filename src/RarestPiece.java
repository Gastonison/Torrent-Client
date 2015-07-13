//Michael Murray, Gaston Gonzalez, Felica Yau


import java.util.ArrayList;
import java.util.Collections;


public class RarestPiece {

	ArrayList<boolean[]> peers;
	int numPieces;
	ArrayList<index> rareList;

	public RarestPiece(ArrayList<boolean[]> peers,int numPieces){

		this.peers=peers;
		this.numPieces=numPieces;
		rareList = new ArrayList<index>();
	}

	/*** Given the peer list which is an ArrayList<boolean[]> 
	 * this method traverses each list and if at a particular index of the boolean[] is true
	 * we increase the count so we can sort the rarest peace.
	 * After we have an int array of rarest pieces we create index objects with fields(index and count)
	 * and sort them in order.
	 */
	
	public void rarestPieceOrdering(){
		int[] tempCounter = new int[numPieces];
		for(int x = 0;x<peers.size();x++){
			for(int y = 0;y<peers.get(x).length;y++){
				if(peers.get(x)[y]==true){
					tempCounter[y]++;
				}
			}
		}


		ArrayList<index> rarest = new ArrayList<index>();

		for(int x = 0;x<tempCounter.length;x++){
			index index = new index();
			index.count=tempCounter[x];
			index.index=x;
			rarest.add(index);
		}

		Collections.sort(rarest,new index());
		rareList=rarest;



	}
	/***
	 * 
	 * @return an ArrayList of<index> the tells us the rarest piece first with the index and count;
	 * randomizes pieces with the same count;
	 */

	public ArrayList<index> pieceToDownload(){
		ArrayList<index> downloadList = new ArrayList<index>();
		ArrayList<index> randomGenerator = new ArrayList<index>();

		for(int x = 0;x<rareList.size();x++){
			randomGenerator.add(rareList.get(x));
			for(int a = x+1;a<rareList.size();a++){
				if(rareList.get(a).count==rareList.get(x).count){
					randomGenerator.add(rareList.get(a));
					x=a;
				}else{

					break;
				}
			}Collections.shuffle(randomGenerator);
			downloadList.addAll(randomGenerator);
			
			randomGenerator.clear();	
		}

		return downloadList;
	}

	/* For testing purposes
	 * 
	public static void main(String[] args){
		boolean[] bitfield = {false,false,false,false,false,false,false};

		boolean[] mike = {true,true,true,true,true,false,true};
		boolean[] mike2 = {true,true,true,true,false,false,true};
		boolean[] mike3 = {true,true,true,false,false,false,true};
		boolean[] mike4 = {true,true,false,false,false,false,true};

		ArrayList<boolean[]> mark = new ArrayList<boolean[]>();
		mark.add(mike);
		mark.add(mike2);
		mark.add(mike3);
		mark.add(mike4);
		RarestPiece tryme = new RarestPiece( mark, bitfield.length);

		tryme.rarestPieceOrdering();
		ArrayList<index> print = tryme.pieceToDownload();
		System.out.println("-------");
		for(int x = 0;x<print.size();x++){
			System.out.println(print.get(x).index+ " index count "+ print.get(x).count  );
		}
	}
	*/

}
