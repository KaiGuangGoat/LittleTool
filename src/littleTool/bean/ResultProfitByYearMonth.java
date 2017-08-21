package littleTool.bean;

public class ResultProfitByYearMonth {
	private int year;
	private int[] months = new int[12];
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int[] getMonths() {
		return months;
	}
	public void setMonthsProfit(int index,int profit) {
		this.months[index] = profit;
	}
}
