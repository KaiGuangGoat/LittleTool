package littleTool.tool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import littleTool.bean.ResultProfitByYearMonth;
import littleTool.utils.DateUtil;

public class AnalyseProfitByDate {
	
	private Map<Integer,Integer[]> profitByYearMonths;
	
	private int currentMonthCount = 0;//当月的订单数
	
	private int positiveProfit;
	private int negativeProfit;
	
	private int positiveNum;
	private int negativeNum;
	
	public AnalyseProfitByDate(int positiveProfit,int negativeProfit){
		this.positiveProfit = positiveProfit;
		this.negativeProfit = negativeProfit;
		profitByYearMonths = new HashMap<Integer, Integer[]>();
	}
	
	public void analyse(Date currentDate,int data,boolean end){
		currentMonthCount++;
		setNum(data);
		
		if(!end){
			analyseLastDayOfMonth(currentDate);
		}
		
		if(end){
//			int year = DateUtil.getYear(currentDate);
//			Map<Integer,Integer[]> map = ProfitByYearMonthHolder.INSTANCE.getProfitByYearMonths();
//			Integer[] months = map.get(year);
//			if(months == null){
//				months = new Integer[12];
//				map.put(year, months);
//			}
//			int month = currentDate.getMonth();
//			System.out.println("month:"+(month+1)+" day:"+currentDate.getDate());
//			months[month] = computeProfit()+(months[month]==null?0:months[month]);
//			map.put(year, months);
			System.out.println("AnalyseProfitByDate negativeNum:"+negativeNum 
					+ " positiveNum:"+positiveNum 
					+ " negativeProfit:"+negativeProfit
					+ " positiveProfit:"+positiveProfit
					+ " currentMonthCount:"+currentMonthCount + "\n");
			System.out.println(profitByYearMonths.size());
			merge(currentDate);
			ProfitByYearMonthHolder.INSTANCE.mergeProfitByYearMonths(profitByYearMonths);
			resetNum();
		}
	}
	
	private void analyseLastDayOfMonth(Date currentDate){
		if(DateUtil.isMonthLastDay(currentDate)){
			merge(currentDate);
		}
	}
	
	private void merge(Date currentDate){
		int year = DateUtil.getYear(currentDate);
		Integer[] months = profitByYearMonths.get(year);
		if(months == null){
			months = new Integer[12];
			profitByYearMonths.put(year, months);
		}
		int month = currentDate.getMonth();
		months[month] = computeProfit()+(months[month]==null?0:months[month]);
		profitByYearMonths.put(year, months);
	}
	
	private int computeProfit(){
//		System.out.println("AnalyseProfitByDate negativeNum:"+negativeNum 
//				+ " positiveNum:"+positiveNum 
//				+ " negativeProfit:"+negativeProfit
//				+ " positiveProfit:"+positiveProfit
//				+ " currentMonthCount:"+currentMonthCount + "\n");
		return negativeNum*negativeProfit-positiveNum*positiveProfit+currentMonthCount;
	}
	
	private void setNum(int data){
		if(data>0){
			positiveNum += data;
		}else if(data<0){
			negativeNum += data;
		}
	}
	
	private void resetNum(){
		profitByYearMonths = new HashMap<Integer, Integer[]>();
		currentMonthCount = 1;
		positiveNum = 0;
		negativeNum = 0;
	}

}
