package littleTool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import littleTool.bean.NumAndSumParam;
import littleTool.bean.ResultByStop;
import littleTool.bean.ResultNoStop;
import littleTool.bean.ResultProfitStop;
import littleTool.bean.Signal;
import littleTool.bean.SignalPositionMsg;
import littleTool.bean.SignalStop;
import littleTool.bean.SumRegion;
import littleTool.bean.SignalStop.Type;

public class AnalysSignal {
	
	private List<Integer> dataList;
	private String textData;
	private List<SignalPositionMsg> positionMsgList;
	private List<ResultProfitStop> outputDataList;
	
	
	public AnalysSignal(List<Integer> dataList,String textData){
		this.dataList = dataList;
		this.textData = textData;
		positionMsgList = new ArrayList<SignalPositionMsg>();
		outputDataList = new ArrayList<ResultProfitStop>();
	}
	
	public String find(){
		if(Signal.REGEX()==null){
			return null;
		}
		StringBuilder signalBuilder = new StringBuilder();
		Pattern p = Pattern.compile(Signal.REGEX());
		Matcher m = p.matcher(textData);
		int count = 0;
		signalBuilder.append("========找到符合条件的信号量如下========").append("\n");
		while(m.find()){
			count++;
			SignalPositionMsg positionMsg = new SignalPositionMsg();
			positionMsg.setPosition(count);
			positionMsg.setStart(m.start());
			positionMsg.setEnd(m.end());
			positionMsgList.add(positionMsg);
		}
		for(SignalPositionMsg msg:positionMsgList){
			signalBuilder.append("position:"+msg.getPosition()).append("\n");
			signalBuilder.append("start:"+msg.getStart()).append("\n");
			signalBuilder.append("end:"+msg.getEnd()).append("\n");
			signalBuilder.append("===============================").append("\n");
		}
		System.out.println(signalBuilder.toString());
		return signalBuilder.toString();
	}
	
	public String analysStopLost(int startParam,int sumParam,NumAndSumParam numAndSumParam,
			int goal,int negativeProfit,int positiveProfit,SignalStop stop){
		StringBuilder resultBuilder = new StringBuilder();
		int totalColl_totalCount = 0;
		int totalColl_positive = 0;
		int totalColl_negative = 0;
		int totalColl_profit = 0;
		int totalColl_maxPositive = 0;
		int totalColl_minNegative = 0;
		
		for(SignalPositionMsg msg:positionMsgList){
			int sum = 0;
			int max = 0;
			int maxPosition = 0;
			int min = 0;
			int minPosition = 0;
			
			int startPosition = 0;
			int startPositionI = 0;
			if(numAndSumParam.isTrue){
				int sumTemp1 = 0;
				startPosition = msg.getEnd();
				while(true){
					if(startPosition+startPositionI>=dataList.size()){
						break;
					}
					int data = dataList.get(startPosition+startPositionI);
					sum = sum + data;
					if(sum>max){
						max = sum;
						maxPosition = startPositionI+1;
					}
					if(sum<min){
						min = sum;
						minPosition = startPositionI+1;
					}
					sumTemp1 += data;
					
					if(numAndSumParam.isNumIndexProcessSelect()&&startPositionI < startParam -1){//如果偏移量在到达startParam位置之前，求和值大于指定的最大值或小于指定的最小值，则结束
						int processSum =  numAndSumParam.getNumIndexProcessSum();
						if(Math.abs(sumTemp1)>=Math.abs(processSum)){
							startPosition = dataList.size();
							break;
						}
					}
					
					if(startPositionI==(startParam-1)){
						SumRegion region = numAndSumParam.getSumRegion();
						if(region.firstEqualSecond()){
							if(sumTemp1 == region.getFirstNum()){
								resultBuilder.append("找到同时满足==").append("的位置："+(startPosition+startPositionI)).append("\n");
								startPositionI ++;//条件满足后的下一个位置为开始位置
								break;
							}
						}
						
						if(region.onlyFirstNotNull()){
							if(sumTemp1>=region.getFirstNum()){
								resultBuilder.append("找到同时满足>=").append("的位置："+(startPosition+startPositionI)).append("\n");
								startPositionI ++;//条件满足后的下一个位置为开始位置
								break;
							}
						}
						
						if(region.onlySecondNotNull()){
							if(sumTemp1<=region.getSecondNum()){
								resultBuilder.append("找到同时满足<=").append("的位置："+(startPosition+startPositionI)).append("\n");
								startPositionI ++;//条件满足后的下一个位置为开始位置
								break;
							}
						}
						
						if(region.allNotNull()){
							if(sumTemp1>=region.getFirstNum()&&sumTemp1<=region.getSecondNum()){
								resultBuilder.append("找到同时满足>=并且<=").append("的位置："+(startPosition+startPositionI)).append("\n");
								startPositionI ++;//条件满足后的下一个位置为开始位置
								break;
							}
						}
						startPosition = dataList.size();
						break;
					}
					
					if(startPositionI > startParam - 1){
						startPosition = dataList.size();
						break;
					}
					
//					if((sumParam>=0&&sumTemp1>=sumParam)||(sumParam<0&&sumTemp1<=sumParam)){
//						if(startPositionI==(startParam - 1)){
//							startPositionI ++;//条件满足后的下一个位置为开始位置
//							resultBuilder.append("找到同时满足1、2").append("的位置："+(startPositionI+1)).append("\n");
//							break;
//						}
//						
//					}
					startPositionI++;
//					startPosition++;
				}
				
			}else{
				if(startParam>0){
					startPosition = msg.getEnd() + startParam - 1;
				}else{
					int sumTemp = 0;
					startPosition = msg.getEnd();
					while(true){
						if(startPosition+startPositionI>=dataList.size()){
							break;
						}
						int data = dataList.get(startPosition+startPositionI);
						sum = sum + data;
						if(sum>max){
							max = sum;
							maxPosition = /*startPosition+*/startPositionI+1;
						}
						if(sum<min){
							min = sum;
							minPosition = /*startPosition+*/startPositionI+1;
						}
						sumTemp += data;
						if((sumParam>=0&&sumTemp>=sumParam)||(sumParam<0&&sumTemp<=sumParam)){
							resultBuilder.append("找到求和达到").append(sumParam).append("的位置："+(startPositionI+1)).append("\n");
							startPositionI++;
							break;
						}
						startPositionI++;
//						startPosition++;
					}
					
				}
			}
				
			int position = 0;
			
			
			int i= 0;
			int positiveNum = 0;
			int negativeNum = 0;
			int beforeSum = sum;
//			sum = 0;
			startPosition = startPosition + startPositionI;
			while(true){
				position = startPosition + i;
				if(position>=dataList.size()){
					break;
				}
				int data = dataList.get(position);
				sum = sum + data;
				beforeSum = beforeSum + data;
				if(beforeSum>max){
					max = beforeSum;
					maxPosition = startPositionI+i+1;
				}
				if(beforeSum<min){
					min = beforeSum;
					minPosition = startPositionI+i+1;
				}
				if(data>0){
					positiveNum++;
				}
				if(data<0){
					negativeNum--;
				}
				
				if(stop.stopType==Type.SINGLE_STOP){
					
					if(i>=stop.singleStop-1 /*|| (goal>=0&&sum>=goal)||(goal<0&&sum<goal)*/){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====单量止损=====").append("\n");
						resultBuilder.append("正数："+positiveNum).append("\n");
						resultBuilder.append("负数："+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("盈利："+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount +i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								stop.stopType);
						break;
					}
				}
				
				if(stop.stopType==Type.NUMERICAL_STOP){
					if((stop.numericalStop>=0&&sum>=stop.numericalStop )
							||(stop.numericalStop<0&&sum<=stop.numericalStop)
							/*|| (goal>=0&&sum>=goal)||(goal<0&&sum<=goal)*/){
							resultBuilder.append("============================").append("\n");
							resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
							resultBuilder.append("=====数值止损=====").append("\n");
							resultBuilder.append("正数："+positiveNum).append("\n");
							resultBuilder.append("负数："+negativeNum).append("\n");
							int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
							resultBuilder.append("盈利："+profit).append("\n");

							totalColl_totalCount = totalColl_totalCount+i+1;
							totalColl_positive = totalColl_positive + positiveNum;
							totalColl_negative = totalColl_negative + negativeNum;
							totalColl_profit = totalColl_profit + profit;
							totalColl_maxPositive = totalColl_maxPositive + max;
							totalColl_minNegative = totalColl_minNegative + min;
							setResultProfitStop(i+1,positiveNum, negativeNum, profit,
									max,maxPosition,min,minPosition,
									stop.stopType);
							break;
					}
				}
				
				if(stop.stopType==Type.ALL_STOP){
					if(i>=stop.singleStop-1){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====综合止损:单量=====").append("\n");
						resultBuilder.append("正数："+positiveNum).append("\n");
						resultBuilder.append("负数："+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("盈利："+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1, positiveNum, negativeNum, profit,
								max,maxPosition,min,minPosition,
								Type.ALL_STOP_SINGLE_STOP);
						break;
					}
					if((stop.numericalStop>=0&&sum>=stop.numericalStop )
							||(stop.numericalStop<0&&sum<=stop.numericalStop)){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====综合止损:数值=====").append("\n");
						resultBuilder.append("正数："+positiveNum).append("\n");
						resultBuilder.append("负数："+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("盈利："+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit,
								max,maxPosition,min,minPosition,
								Type.ALL_STOP_NUMERICAL_STOP);
						break;
					}
					if(/*(goal>0&&sum>=goal)||(goal<0&&sum<=goal)*/sum==goal){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====综合止损:无止损=====").append("\n");
						resultBuilder.append("正数："+positiveNum).append("\n");
						resultBuilder.append("负数："+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("盈利："+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								stop.stopType);
						break;
					}
				}
				
				if(/*(goal>=0&&sum>=goal)||(goal<0&&sum<=goal)*/sum==goal){
					resultBuilder.append("============================").append("\n");
					resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
					resultBuilder.append("=====无止损=====").append("\n");
					resultBuilder.append("无止损数："+(i+1)).append("\n");
					resultBuilder.append("正数："+positiveNum).append("\n");
					resultBuilder.append("负数："+negativeNum).append("\n");
					int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
					resultBuilder.append("盈利："+profit).append("\n");
					resultBuilder.append("最大正数："+max).append("\n");
					resultBuilder.append("位置："+maxPosition).append("\n");
					resultBuilder.append("最小负数："+min).append("\n");
					resultBuilder.append("位置："+minPosition).append("\n");
					resultBuilder.append("==============================").append("\n");
					
					totalColl_totalCount= totalColl_totalCount+i+1;
					totalColl_positive = totalColl_positive + positiveNum;
					totalColl_negative = totalColl_negative + negativeNum;
					totalColl_profit = totalColl_profit + profit;
					totalColl_maxPositive = totalColl_maxPositive + max;
					totalColl_minNegative = totalColl_minNegative + min;
					
					ResultProfitStop mResultProfitStop = new ResultProfitStop();
					mResultProfitStop.setTotalCount(i+1);
					mResultProfitStop.setPositive(positiveNum);
					mResultProfitStop.setNegative(negativeNum);
					mResultProfitStop.setProfit(profit);
					mResultProfitStop.setMaxPositive(max);
					mResultProfitStop.setPositionPst(maxPosition);
					mResultProfitStop.setMinNegative(min);
					mResultProfitStop.setPositionNgt(minPosition);
					mResultProfitStop.setStopType(Type.NO_STOP);
					outputDataList.add(mResultProfitStop);
					break;
				}
				
				i++;
			}
		}
		ResultProfitStop mResultProfitStop = new ResultProfitStop();
		mResultProfitStop.setTotalCount(totalColl_totalCount);
		mResultProfitStop.setPositive(totalColl_positive);
		mResultProfitStop.setNegative(totalColl_negative);
		mResultProfitStop.setProfit(totalColl_profit);
		mResultProfitStop.setMaxPositive(totalColl_maxPositive);
		mResultProfitStop.setMinNegative(totalColl_minNegative);
		outputDataList.add(mResultProfitStop);
		return resultBuilder.toString();
	}
	
	public List<ResultProfitStop> getOutputDataList(){
		return outputDataList;
	}
	
	private void setResultProfitStop(int totalCount,int positiveNum,int negativeNum,int profit,
			int max,int maxPosition,int min,int minPosition,
			Type type){
		ResultProfitStop mResultProfitStop = new ResultProfitStop();
		mResultProfitStop.setTotalCount(totalCount);
		mResultProfitStop.setPositive(positiveNum);
		mResultProfitStop.setNegative(negativeNum);
		mResultProfitStop.setProfit(profit);
		mResultProfitStop.setMaxPositive(max);
		mResultProfitStop.setPositionPst(maxPosition);
		mResultProfitStop.setMinNegative(min);
		mResultProfitStop.setPositionNgt(minPosition);
		mResultProfitStop.setStopType(type);
		outputDataList.add(mResultProfitStop);
	}
	
}
