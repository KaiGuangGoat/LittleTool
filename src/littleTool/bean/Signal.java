package littleTool.bean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import littleTool.AnalysSignal;

public class Signal {
	public static ArrayList<Signal> signalList = new ArrayList<Signal>();
	private int count;//大于或等于的个数
	private boolean positiveNum;//false:-1 or true:1
	private SignalRange range;//大于或等于
	
	public static String REGEX(){
		if(signalList.size()==0){
			return null;
		}
		StringBuilder regular = new StringBuilder();
		for(Signal signal:signalList){
			String num = signal.positiveNum?"√":"x";
			switch(signal.getRange()){
			case EQUALS:
				//1{n}
				regular.append(num).append("{").append(signal.getCount()).append("}");
				break;
			case MORE:
				//1{n,}
				regular.append(num).append("{").append(signal.getCount()).append(",}");
				break;
			}
		}
		return regular.toString();
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isPositiveNum() {
		return positiveNum;
	}
	public void setPositiveNum(boolean positiveNum) {
		this.positiveNum = positiveNum;
	}
	public SignalRange getRange() {
		return range;
	}
	public void setRange(SignalRange range) {
		this.range = range;
	}
	
	public static void testSignal(){
		for (int i = 0; i < 4; i++) {
			Signal signal = new Signal();
			if(i==0){
				signal.setCount(2);
				signal.setPositiveNum(true);
				signal.setRange(SignalRange.MORE);
			}else if(i==1){
				signal.setCount(2);
				signal.setPositiveNum(false);
				signal.setRange(SignalRange.MORE);
			}else if(i==2){
				signal.setCount(1);
				signal.setPositiveNum(true);
				signal.setRange(SignalRange.EQUALS);
			}else{
				signal.setCount(1);
				signal.setPositiveNum(false);
				signal.setRange(SignalRange.EQUALS);
			}
			Signal.signalList.add(signal);
		}
	}
	
	public static void main(String[] args) {
		testSignal();
		String testStr = "xx√x√xxxxxxxxxx√xxx√x√x√xx√√x√√xxx√xxxx√√xx√√xx√x√xxx√x√xxxxxx√√√x√x√√x√x√√xx√√√xxxx√√√x√xx√xxx√x√x√xxxx√xx√x√√xxx√√xxxx√xxxx√√x√√xx√xxx√xxxx√xxx√x√x√xxx√xxx√xx√√xxxx√xxxx√xxxx√xxxxx√x√x√√xxx√xxx√x√xx√x√√√√√√√√√√x√x√xxxx√x√x√√xxxxxxxx√xxxxxxx√xx√xx√√xx√√xxxxxxxxxxxxx√x√x√√xx√x√√√xx√xxx√x√x√x√x√√xx√√√√x√xxx√√√xxxxx√√x√xx√xxxxx√xx√xxxx√x√xxxx√√x√√x√√xxxx√√x√√x√x√xx√xxxxxx√x√√x√√√xxxx√√√x√√xxxx√x√xx√√√x√√xxx√√xxxx√xxx√√√√xxxxxxxxxxxxxx√xxx√√xx√xxxx√xxxxxxx√x√√√xxxxxx√xxxx√√xx√x√xxxxxx√√x√x√x√xx√√xxxxx√xxxx√xx√x√xxx√√x√√xxx√xx√xxxxx√√x√x√xxx√√xxxxx√xxx√x√xxxx√√√xxxx√xxxxx√√x√xx√xx√xxxx√xxxxxxx√√xxx√xx√x√√xx√xx√√√√x√x√xx√xxx√xxxxx√√√x√√xxxxxx√xxxxx√√xxxxxx√√xxx√xx√xx√xx√xxxxxxxxxx√xx√x√xx√√xxx√x√";
		AnalysSignal analysSignal = new AnalysSignal(null, testStr);
		analysSignal.find();
		System.out.println(Signal.REGEX());
//		Pattern p = Pattern.compile(Signal.REGEX());
//		Matcher matcher = p.matcher(testStr);
//		int count = 0;
//		while(matcher.find()){
//			count++;
//			System.out.println(count);
//			System.out.println("start:"+matcher.start());
//			System.out.println("end:"+matcher.end());
//		}
	}
}
