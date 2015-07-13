import java.util.Comparator;


public class index  implements Comparator<index> {

	int index;
	int count;
	boolean downloaded;
	
	public index(){
		this.index=0;
		this.count=0;
		downloaded=false;
	}
	
	public int compareTo(index arg1) {
		if ((int) this.count < (int) arg1.count)
			return -1;
		if (this.count==(arg1.count))
			return 0;

		return 1;
	}

	@Override
	public int compare(index arg0, index arg1) {
		return arg0.compareTo(arg1);
	}

		
}
