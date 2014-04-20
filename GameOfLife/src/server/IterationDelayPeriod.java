package server;

public class IterationDelayPeriod {
	private int delayVal;
	public IterationDelayPeriod(int val) throws IllegalArgumentException {
		if(val < 0) {
			throw new IllegalArgumentException();
		}
		else {
			setDelayVal(val);
		}
	}
	public int getDelayVal() {
		return delayVal;
	}
	public void setDelayVal(int delayVal) {
		this.delayVal = delayVal;
	}
}
