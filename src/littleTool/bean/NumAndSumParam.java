package littleTool.bean;

/**
 * 
 * @author Kaiguang
 * @��˵�� ƫ���������ֵͬ�����������Ĳ���
 */
public class NumAndSumParam {
	public boolean isTrue = false;
	private boolean numIndexProcessSelect;
	private int numIndexProcessSum;//�м���̳��ֵ��趨����ֵ������������>=|sum|��<=-|sum|
	private boolean numIProSumEnterOrClose;
	private SumRegion sumRegion;
	public int getNumIndexProcessSum() {
		return numIndexProcessSum;
	}
	public void setNumIndexProcessSum(int numIndexProcessSum) {
		this.numIndexProcessSum = numIndexProcessSum;
	}
	public boolean isNumIProSumEnterOrClose() {
		return numIProSumEnterOrClose;
	}
	public void setNumIProSumEnterOrClose(boolean numIProSumEnterOrClose) {
		this.numIProSumEnterOrClose = numIProSumEnterOrClose;
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
