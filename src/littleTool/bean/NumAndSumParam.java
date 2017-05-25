package littleTool.bean;

/**
 * 
 * @author Kaiguang
 * @类说明 偏移量和求和值同事满足的情况的参数
 */
public class NumAndSumParam {
	public boolean isTrue = false;
	private boolean numIndexProcessSelect;
	private int numIndexProcessSum;//中间过程出现的设定的数值，结束条件，>=|sum|或<=-|sum|
	private SumRegion sumRegion;
	public int getNumIndexProcessSum() {
		return numIndexProcessSum;
	}
	public void setNumIndexProcessSum(int numIndexProcessSum) {
		this.numIndexProcessSum = numIndexProcessSum;
	}
	public SumRegion getSumRegion() {
		return sumRegion;
	}
	public void setSumRegion(SumRegion sumRegion) {
		this.sumRegion = sumRegion;
	}
	public boolean isNumIndexProcessSelect() {
		return numIndexProcessSelect;
	}
	public void setNumIndexProcessSelect(boolean numIndexProcessSelect) {
		this.numIndexProcessSelect = numIndexProcessSelect;
	}
}
