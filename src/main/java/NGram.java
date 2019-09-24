
public class NGram {
	private int firstCount;
	private int secondCount;
	
	
	public NGram(int prevCount) {
		this.firstCount = prevCount;
		this.secondCount = 1;
	}
	
	public void incCount(int prevCount) {
		this.secondCount++;
		this.firstCount = prevCount;
	}
	
	public double getFrequency() {
		return (double) (this.secondCount/this.firstCount);
	}
}
