package littleTool.bean;

import java.io.Serializable;

public class ResultNoStop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7739054872870551784L;
	private int totalCount;
	private int positive;
	private int negative;
	private int profit;
	private int maxPositive;
	private int positionPst;
	private int minNegative;
	private int positionNgt;
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPositive() {
		return positive;
	}
	public void setPositive(int positive) {
		this.positive = positive;
	}
	public int getNegative() {
		return negative;
	}
	public void setNegative(int negative) {
		this.negative = negative;
	}
	public int getProfit() {
		return profit;
	}
	public void setProfit(int profit) {
		this.profit = profit;
	}
	public int getMaxPositive() {
		return maxPositive;
	}
	public void setMaxPositive(int maxPositive) {
		this.maxPositive = maxPositive;
	}
	public int getPositionPst() {
		return positionPst;
	}
	public void setPositionPst(int positionPst) {
		this.positionPst = positionPst;
	}
	public int getMinNegative() {
		return minNegative;
	}
	public void setMinNegative(int minNegative) {
		this.minNegative = minNegative;
	}
	public int getPositionNgt() {
		return positionNgt;
	}
	public void setPositionNgt(int positionNgt) {
		this.positionNgt = positionNgt;
	}
}
