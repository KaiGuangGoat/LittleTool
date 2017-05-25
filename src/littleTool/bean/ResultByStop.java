package littleTool.bean;

import java.io.Serializable;

public class ResultByStop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -999700083254005772L;
	private int positiveCount;
	private int negativeCount;
	private int profit;
	public int getPositiveCount() {
		return positiveCount;
	}
	public void setPositiveCount(int positiveCount) {
		this.positiveCount = positiveCount;
	}
	public int getNegativeCount() {
		return negativeCount;
	}
	public void setNegativeCount(int negativeCount) {
		this.negativeCount = negativeCount;
	}
	public int getProfit() {
		return profit;
	}
	public void setProfit(int profit) {
		this.profit = profit;
	}
}
