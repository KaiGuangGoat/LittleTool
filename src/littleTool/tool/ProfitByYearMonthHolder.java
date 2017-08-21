package littleTool.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public enum ProfitByYearMonthHolder {
	INSTANCE;
	private Map<Integer,Integer[]> profitByYearMonths;
	
	public void init(){
		if(profitByYearMonths == null){
			profitByYearMonths = new HashMap<Integer, Integer[]>();
		}
	}

	public Map<Integer, Integer[]> getProfitByYearMonths() {
		init();
		return profitByYearMonths;
	}
	
	public Integer[] getMonthsProfit(int year){
		init();
		return profitByYearMonths.get(year);
	}
	
	public void setMounthsProfit(int year,int mounth,int profit){
		init();
		Integer[] mounthsProfit = profitByYearMonths.get(year);
		if(null == mounthsProfit){
			mounthsProfit = new Integer[12];
			profitByYearMonths.put(year,mounthsProfit);
		}
		mounthsProfit[mounth] = profit;
	}
	
	public void mergeProfitByYearMonths(Map<Integer,Integer[]> profitByYearMonthsMerge){
		mergeProfitByYearMonths(profitByYearMonths, profitByYearMonthsMerge);
	}
	
	public void mergeProfitByYearMonths(Map<Integer,Integer[]> profitByYearMonths,Map<Integer,Integer[]> profitByYearMonthsMerge){
		if(profitByYearMonths == null){
			setProfitByYearMonths(profitByYearMonthsMerge);
			return;
		}
		for(Entry<Integer, Integer[]> entry:profitByYearMonthsMerge.entrySet()){
			Integer key = entry.getKey(); 
			Integer[] value = profitByYearMonths.get(key);
			Integer[] valueMerge = entry.getValue();
			if(value == null){
				profitByYearMonths.put(key, copyIntegerList(valueMerge));
				continue;
			}
			for(int i=0;i<valueMerge.length;i++){
				value[i] = (value[i]==null?0:value[i]) +
						(valueMerge[i]==null?0:valueMerge[i]);
			}
			profitByYearMonths.put(key, value);
		}	
		
	}
	
	private void setProfitByYearMonths(Map<Integer,Integer[]> profitByYearMonths){
		this.profitByYearMonths = new HashMap<Integer, Integer[]>();
		for(Entry<Integer, Integer[]> entry:profitByYearMonths.entrySet()){
			Integer key = entry.getKey(); 
			Integer[] value = entry.getValue();
			this.profitByYearMonths.put(key, copyIntegerList(value));
		}
	}
	
	private Integer[] copyIntegerList(Integer[] integerList){
		if(integerList == null){
			return null;
		} 
		Integer[] intes = new Integer[integerList.length];
		for(int i=0;i<integerList.length;i++){
			intes[i] = integerList[i];
		}
		return intes;
	}
	
	public void reset(){
		profitByYearMonths = new HashMap<Integer, Integer[]>();
	}
	
	public void print(){
		System.out.println(profitByYearMonths.size());
		for(Entry<Integer, Integer[]> entry:profitByYearMonths.entrySet()){
			System.out.println();
			System.out.print(entry.getKey()+":");
			for(Integer profit:entry.getValue()){
				System.out.print(profit+" ");
			}
			System.out.println();
		}
	}

}
