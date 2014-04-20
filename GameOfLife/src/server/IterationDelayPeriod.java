package server;

import java.io.Serializable;

public class IterationDelayPeriod implements Serializable {
	private static final long serialVersionUID = 288598332296734577L;
	
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
