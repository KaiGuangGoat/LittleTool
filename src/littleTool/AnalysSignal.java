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
import littleTool.bean.SignalPositionUnEntrance;
import littleTool.bean.SignalStop;
import littleTool.bean.SumRegion;
import littleTool.bean.SignalStop.Type;

public class AnalysSignal {
	
	private List<Integer> dataList;
	private String textData;
	private List<SignalPositionMsg> positionMsgList;
	private List<ResultProfitStop> outputDataList;
	private List<SignalPositionUnEntrance> unEntrancdList;
//	private List<SignalPositionUnEntrance> unProfitStopList;
	
	
	public AnalysSignal(List<Integer> dataList,String textData){
		this.dataList = dataList;
		this.textData = textData;
		positionMsgList = new ArrayList<SignalPositionMsg>();
		outputDataList = new ArrayList<ResultProfitStop>();
		unEntrancdList = new ArrayList<SignalPositionUnEntrance>();
//		unProfitStopList = new ArrayList<SignalPositionUnEntrance>();
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
			int index = 0;//从原始位置开始算起
			int sum = 0;
			int max = 0;
			int maxPosition = 0;
			int min = 0;
			int minPosition = 0;
			
			int startPosition = 0;
			int startPositionI = 0;
			
			int positiveCountUn = 0;
			int negativeCountUn = 0;
			if(numAndSumParam.isTrue){
				int sumTemp1 = 0;
				startPosition = msg.getEnd();
				while(true){
					if(startPosition+startPositionI>=dataList.size()){
						setUnEntranceList(msg, positiveCountUn, negativeCountUn);
						break;
					}
					int data = dataList.get(startPosition+startPositionI);
					if(data>0){
						positiveCountUn++;
					}
					if(data<0){
						negativeCountUn++;
					}
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
					//startParam -1:到达目标位置
					if(numAndSumParam.isNumIndexProcessSelect()&&startPositionI < startParam -1){//如果偏移量在到达startParam位置之前，求和值大于指定的最大值或小于指定的最小值，则结束
						int processSum =  numAndSumParam.getNumIndexProcessSum();
						//Math.abs(sumTemp1)>=Math.abs(processSum)
						if(processSum>=0&&sumTemp1>=processSum || processSum<0&&sumTemp1<=processSum){
							startPositionI = numAndSumParam.isNumIProSumEnterOrClose()?(startPositionI+1):dataList.size();
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
						
//						if(region.allNull()){
//							startPositionI = dataList.size();
//							break;
//						}
						
						
						
					}
					
//					if(startPositionI > startParam - 1){
//						startPositionI = dataList.size();
//						break;
//					}
					
					startPositionI++;
					index++;
				}
				
			}else{
				if(startParam>0){
					startPosition = msg.getEnd() + startParam - 1;
					index = startParam;
					if(startPosition>=dataList.size()){
						int startPositionTemp = msg.getEnd();
						while(true){
							if(startPositionTemp>=dataList.size()){
								setUnEntranceList(msg, positiveCountUn, negativeCountUn);
								break;
							}
							int data = dataList.get(startPositionTemp);
							if(data>0) positiveCountUn++;
							if(data<0) negativeCountUn++;
							startPositionTemp++;
						}
					}
				}else{
					int sumTemp = 0;
					startPosition = msg.getEnd();
					while(true){
						if(startPosition+startPositionI>=dataList.size()){
							setUnEntranceList(msg, positiveCountUn, negativeCountUn);
							break;
						}
						int data = dataList.get(startPosition+startPositionI);
						if(data>0){
							positiveCountUn++;
						}
						if(data<0){
							negativeCountUn++;
						}
						sum = sum + data;
						if(sum>max){
							max = sum;
							maxPosition = startPositionI+1;
						}
						if(sum<min){
							min = sum;
							minPosition = startPositionI+1;
						}
						sumTemp += data;
						if((sumParam>=0&&sumTemp>=sumParam)||(sumParam<0&&sumTemp<=sumParam)){
							resultBuilder.append("找到求和达到").append(sumParam).append("的位置："+(startPositionI+1)).append("\n");
							startPositionI++;
							break;
						}
						startPositionI++;
						index++;
					}
					
				}
			}
				
			int position = 0;
			
			
			int i= 0;
			int positiveNum = 0;
			int negativeNum = 0;
			int beforeSum = sum;
			
			int conditionSum = 0;
			int conditionMax = 0;
			int conditionMaxPosition = 0;
			int conditionMin = 0;
			int conditionMinPosition = 0;
			
			startPosition = startPosition + startPositionI;
			while(true){
				position = startPosition + i;
				if(position>=dataList.size()){
					setUnProfitStopList(msg, positiveNum, negativeNum*-1, positiveCountUn, negativeCountUn);
					break;
				}
				int data = dataList.get(position);
				sum = sum + data;
				beforeSum = beforeSum + data;
				conditionSum = conditionSum + data;
				if(beforeSum>max){
					max = beforeSum;
					maxPosition = startPositionI+i+1;
				}
				if(beforeSum<min){
					min = beforeSum;
					minPosition = startPositionI+i+1;
				}
				if(conditionSum>conditionMax){
					conditionMax = conditionSum;
					conditionMaxPosition = startPositionI+i+1;
				}
				if(conditionSum<conditionMin){
					conditionMin = conditionSum;
					conditionMinPosition = startPositionI+i+1;
				}
				if(data>0){
					positiveNum++;
					positiveCountUn++;
				}
				if(data<0){
					negativeNum--;
					negativeCountUn++;
				}
				
				if(stop.stopType==Type.SINGLE_STOP){
					
					if(stop.singleStop!=null&&i>=stop.singleStop-1 /*|| (goal>=0&&sum>=goal)||(goal<0&&sum<goal)*/){
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
					int sumTemp = sum;
					int maxTemp = max;
					int maxPositionTemp = maxPosition;
					int minTemp = min;
					int minPositionTemp = minPosition;
					if(!stop.numericalStopStarBegin){
						sumTemp = conditionSum;
						maxTemp = conditionMax;
						maxPositionTemp = conditionMaxPosition;
						minTemp = conditionMin;
						minPositionTemp = conditionMinPosition;
					}
					if(stop.numericalStop!=null&&((stop.numericalStop>=0&&sumTemp>=stop.numericalStop )
							||(stop.numericalStop<0&&sumTemp<=stop.numericalStop))
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
							totalColl_maxPositive = totalColl_maxPositive + maxTemp;
							totalColl_minNegative = totalColl_minNegative + minTemp;
							setResultProfitStop(i+1,positiveNum, negativeNum, profit,
									maxTemp,maxPositionTemp,minTemp,minPositionTemp,
									stop.stopType);
							break;
					}
				}
				
				if(stop.joinFixedInAllStopOrNot&&stop.stopType==Type.FIXED_STOP){
					if(stop.fixedStop!=null&&index>=stop.fixedStop ){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====固定止损=====").append("\n");
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
				
				if(stop.stopType==Type.ALL_STOP){
					if(stop.singleStop!=null&&i>=stop.singleStop-1){
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
								Type.ALL_STOP_NO_STOP);
						break;
					}
					
					if(stop.joinFixedInAllStopOrNot && stop.fixedStop!=null && index>=stop.fixedStop){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("第  "+msg.getPosition()+" 个信号量：").append("\n");
						resultBuilder.append("=====固定止损=====").append("\n");
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
								Type.ALL_STOP_FIXED_STOP);
						break;
					}
					
					int sumTemp = sum;
					int maxTemp = max;
					int maxPositionTemp = maxPosition;
					int minTemp = min;
					int minPositionTemp = minPosition;
					if(!stop.numericalStopStarBegin){
						sumTemp = conditionSum;
						maxTemp = conditionMax;
						maxPositionTemp = conditionMaxPosition;
						minTemp = conditionMin;
						minPositionTemp = conditionMinPosition;
					}
					if(stop.numericalStop!=null&&((stop.numericalStop>=0&&sumTemp>=stop.numericalStop )
							||(stop.numericalStop<0&&sumTemp<=stop.numericalStop))){
						
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
						totalColl_maxPositive = totalColl_maxPositive + maxTemp;
						totalColl_minNegative = totalColl_minNegative + minTemp;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit,
								maxTemp,maxPositionTemp,minTemp,minPositionTemp,
								Type.ALL_STOP_NUMERICAL_STOP);
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
					
//					ResultProfitStop mResultProfitStop = new ResultProfitStop();
//					mResultProfitStop.setTotalCount(i+1);
//					mResultProfitStop.setPositive(positiveNum);
//					mResultProfitStop.setNegative(negativeNum);
//					mResultProfitStop.setProfit(profit);
//					mResultProfitStop.setMaxPositive(max);
//					mResultProfitStop.setPositionPst(maxPosition);
//					mResultProfitStop.setMinNegative(min);
//					mResultProfitStop.setPositionNgt(minPosition);
//					mResultProfitStop.setStopType(Type.NO_STOP);
//					outputDataList.add(mResultProfitStop);
					setResultProfitStop(i+1, positiveNum, negativeNum, profit, 
							conditionMax, conditionMaxPosition, conditionMin, 
							conditionMinPosition, Type.NO_STOP);
					break;
				}
				
				i++;
				index++;
			}
		}
		//总的统计
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
	
	private void setUnEntranceList(SignalPositionMsg msg,int positiveCountUn,int negativeCountUn){
		SignalPositionUnEntrance unEntrance = new SignalPositionUnEntrance();
		unEntrance.TYPE = SignalPositionUnEntrance.TYPE_SIGNAL_UN_ENTRANCE;
		unEntrance.setSignalPositionMsg(msg);
		unEntrance.setPositiveCount(positiveCountUn);
		unEntrance.setNegativeCount(negativeCountUn);
		unEntrancdList.add(unEntrance);
	}
	
	private void setUnProfitStopList(SignalPositionMsg msg,int positiveCountUn,int negativeCountUn,int positiveCountProfitStop,int negativeCountProfitStop){
		SignalPositionUnEntrance unEntrance = new SignalPositionUnEntrance();
		unEntrance.TYPE = SignalPositionUnEntrance.TYPE_SIGNAL_UN_PROFITSTOP;
		unEntrance.setSignalPositionMsg(msg);
		unEntrance.setPositiveCount(positiveCountUn);
		unEntrance.setNegativeCount(negativeCountUn);
		unEntrance.setPositiveCountProfitStop(positiveCountProfitStop);
		unEntrance.setNegativeCountProfitStop(negativeCountProfitStop);
		unEntrancdList.add(unEntrance);
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
	
	public List<ResultProfitStop> getOutputDataList(){
		return outputDataList;
	}
	
	public List<SignalPositionUnEntrance> getUnEntrancdList() {
		return unEntrancdList;
	}

//	public List<SignalPositionUnEntrance> getUnProfitStopList() {
//		return unProfitStopList;
//	}
	
}
