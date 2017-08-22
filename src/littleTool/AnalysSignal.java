package littleTool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.DataUtil;

import littleTool.bean.NumAndSumParam;
import littleTool.bean.ResultByStop;
import littleTool.bean.ResultNoStop;
import littleTool.bean.ResultProfitByYearMonth;
import littleTool.bean.ResultProfitStop;
import littleTool.bean.Signal;
import littleTool.bean.SignalPositionMsg;
import littleTool.bean.SignalPositionUnEntrance;
import littleTool.bean.SignalStop;
import littleTool.bean.SumRegion;
import littleTool.bean.SignalStop.Type;
import littleTool.tool.AnalyseProfitByDate;
import littleTool.tool.ProfitByYearMonthHolder;
import littleTool.utils.DateUtil;

public class AnalysSignal {
	
	private List<Integer> dataList;
	private List<String> timeList;
	private String textData;
	private List<SignalPositionMsg> positionMsgList;
	private List<ResultProfitStop> outputDataList;
	private List<ResultProfitByYearMonth> outputProfitByYearMonths;
	private List<SignalPositionUnEntrance> unEntrancdList;
//	private List<SignalPositionUnEntrance> unProfitStopList;
	
	
	public AnalysSignal(List<Integer> dataList,String textData){
		this.dataList = dataList;
		this.textData = textData;
		positionMsgList = new ArrayList<SignalPositionMsg>();
		outputDataList = new ArrayList<ResultProfitStop>();
		outputProfitByYearMonths = new ArrayList<ResultProfitByYearMonth>();
		unEntrancdList = new ArrayList<SignalPositionUnEntrance>();
//		unProfitStopList = new ArrayList<SignalPositionUnEntrance>();
	}
	
	public void setTimeList(List<String> timeList){
		this.timeList = timeList;
	}
	
	public String find(){
		if(Signal.REGEX()==null){
			return null;
		}
		StringBuilder signalBuilder = new StringBuilder();
		Pattern p = Pattern.compile(Signal.REGEX());
		Matcher m = p.matcher(textData);
		int count = 0;
		signalBuilder.append("========�ҵ������������ź�������========").append("\n");
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
			int index = 1;//��ԭʼλ�ÿ�ʼ���𣬴���̶�ֹ���õ�
			int sum = 0;
			int max = 0;
			int maxPosition = 0;
			int min = 0;
			int minPosition = 0;
			
			int startPosition = 0;
			int startPositionI = 0;
			
			int positiveCountUn = 0;
			int negativeCountUn = 0;
			boolean flag = false;
			if(numAndSumParam.isTrue){
				int sumTemp1 = 0;
				startPosition = msg.getEnd();
				while(true){
					if(startPosition+startPositionI>=dataList.size()){
						setUnEntranceList(msg, positiveCountUn, negativeCountUn);
						flag = true;
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
					//startParam -1:����Ŀ��λ��
					if(numAndSumParam.isNumIndexProcessSelect()&&startPositionI < startParam -1){//���ƫ�����ڵ���startParamλ��֮ǰ�����ֵ����ָ�������ֵ��С��ָ������Сֵ�������
						int processSum =  numAndSumParam.getNumIndexProcessSum();
						//Math.abs(sumTemp1)>=Math.abs(processSum)
						if(processSum>=0&&sumTemp1>=processSum || processSum<0&&sumTemp1<=processSum){
//							startPositionI = numAndSumParam.isNumIProSumEnterOrClose()?(startPositionI+1):dataList.size();
							if(numAndSumParam.isNumIProSumEnterOrClose()){
								startPositionI ++;
								index++;
								if(startPosition+startPositionI<dataList.size()){
									int dataPlus = dataList.get(startPosition+startPositionI);
									if(dataPlus>0){
										positiveCountUn++;
									}
									if(dataPlus<0){
										negativeCountUn++;
									}
									sum = sum + dataPlus;
									if(sum>max){
										max = sum;
										maxPosition = startPositionI+1;
									}
									if(sum<min){
										min = sum;
										minPosition = startPositionI+1;
									}
								}
								resultBuilder.append("�볡�źŵ�λ�ã�"+startPositionI).append("\n");
							}else{
								startPositionI = dataList.size();
								int startPositionTemp = msg.getEnd();
								positiveCountUn = negativeCountUn = 0;
								while(true){
									if(startPositionTemp>=dataList.size()){
										setUnEntranceList(msg, positiveCountUn, negativeCountUn);
										flag = true;
										break;
									}
									int dataTemp = dataList.get(startPositionTemp);
									if(dataTemp>0) positiveCountUn++;
									if(dataTemp<0) negativeCountUn++;
									startPositionTemp++;
								}	
							}
							break;
						}
					}
					
					if(startPositionI==(startParam-1)){
						SumRegion region = numAndSumParam.getSumRegion();
						if(region.firstEqualSecond()){
							if(sumTemp1 == region.getFirstNum()){
								startPositionI ++;//������������һ��λ��Ϊ��ʼλ��
								resultBuilder.append("�ҵ�ͬʱ����==").append("��λ�ã�"+(startPosition+startPositionI)).append("\n");
								index++;
								if(startPosition+startPositionI<dataList.size()){
									int dataPlus = dataList.get(startPosition+startPositionI);
									if(dataPlus>0){
										positiveCountUn++;
									}
									if(dataPlus<0){
										negativeCountUn++;
									}
									sum = sum + dataPlus;
									if(sum>max){
										max = sum;
										maxPosition = startPositionI+1;
									}
									if(sum<min){
										min = sum;
										minPosition = startPositionI+1;
									}
								}
								break;
							}
						}
						
						if(region.onlyFirstNotNull()){
							if(sumTemp1>=region.getFirstNum()){
								startPositionI ++;//������������һ��λ��Ϊ��ʼλ��
								resultBuilder.append("�ҵ�ͬʱ����>=").append("��λ�ã�"+(startPosition+startPositionI)).append("\n");
								index++;
								if(startPosition+startPositionI<dataList.size()){
									int dataPlus = dataList.get(startPosition+startPositionI);
									if(dataPlus>0){
										positiveCountUn++;
									}
									if(dataPlus<0){
										negativeCountUn++;
									}
									sum = sum + dataPlus;
									if(sum>max){
										max = sum;
										maxPosition = startPositionI+1;
									}
									if(sum<min){
										min = sum;
										minPosition = startPositionI+1;
									}
								}
								break;
							}
						}
						
						if(region.onlySecondNotNull()){
							if(sumTemp1<=region.getSecondNum()){
								startPositionI ++;//������������һ��λ��Ϊ��ʼλ��
								resultBuilder.append("�ҵ�ͬʱ����<=").append("��λ�ã�"+(startPosition+startPositionI)).append("\n");
								index++;
								if(startPosition+startPositionI<dataList.size()){
									int dataPlus = dataList.get(startPosition+startPositionI);
									if(dataPlus>0){
										positiveCountUn++;
									}
									if(dataPlus<0){
										negativeCountUn++;
									}
									sum = sum + dataPlus;
									if(sum>max){
										max = sum;
										maxPosition = startPositionI+1;
									}
									if(sum<min){
										min = sum;
										minPosition = startPositionI+1;
									}
								}
								break;
							}
						}
						
						if(region.allNotNull()){
							if(sumTemp1>=region.getFirstNum()&&sumTemp1<=region.getSecondNum()){
								startPositionI ++;//������������һ��λ��Ϊ��ʼλ��
								resultBuilder.append("�ҵ�ͬʱ����>=����<=").append("��λ�ã�"+(startPosition+startPositionI)).append("\n");
								index++;
								if(startPosition+startPositionI<dataList.size()){
									int dataPlus = dataList.get(startPosition+startPositionI);
									if(dataPlus>0){
										positiveCountUn++;
									}
									if(dataPlus<0){
										negativeCountUn++;
									}
									sum = sum + dataPlus;
									if(sum>max){
										max = sum;
										maxPosition = startPositionI+1;
									}
									if(sum<min){
										min = sum;
										minPosition = startPositionI+1;
									}
								}
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
					int startPositionTemp = msg.getEnd();
					while(true){
						if(startPositionTemp>=dataList.size()){
							if(startPosition>=dataList.size()){
								setUnEntranceList(msg, positiveCountUn, negativeCountUn);
								flag = true;
							}
							break;
						}
						int data = dataList.get(startPositionTemp);
						if(data>0) positiveCountUn++;
						if(data<0) negativeCountUn++;
						startPositionTemp++;
					}	
				}else{
					int sumTemp = 0;
					startPosition = msg.getEnd();
					while(true){
						if(startPosition+startPositionI>=dataList.size()){
							setUnEntranceList(msg, positiveCountUn, negativeCountUn);
							flag = true;
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
							resultBuilder.append("�ҵ���ʹﵽ").append(sumParam).append("��λ�ã�"+(startPositionI+1)).append("\n");
							startPositionI++;
							if(startPosition+startPositionI<dataList.size()){
								int dataPlus = dataList.get(startPosition+startPositionI);
								if(dataPlus>0){
									positiveCountUn++;
								}
								if(dataPlus<0){
									negativeCountUn++;
								}
								sum = sum + dataPlus;
								if(sum>max){
									max = sum;
									maxPosition = startPositionI+1;
								}
								if(sum<min){
									min = sum;
									minPosition = startPositionI+1;
								}
							}
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
			
			AnalyseProfitByDate profitByDate = new AnalyseProfitByDate(positiveProfit, negativeProfit);
			while(true){
				position = startPosition + i;
				if(position>=dataList.size()){
					if(flag == false)
						setUnProfitStopList(msg,positiveCountUn, negativeCountUn, positiveNum, negativeNum*-1);
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
				}
				if(data<0){
					negativeNum--;
				}
				
				if(stop.stopType==Type.SINGLE_STOP){
					
					if(stop.singleStop!=null&&i>=stop.singleStop-1 && sum!=goal/*|| (goal>=0&&sum>=goal)||(goal<0&&sum<goal)*/){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====����ֹ��=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount +i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								stop.stopType);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
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
							resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
							resultBuilder.append("=====��ֵֹ��=====").append("\n");
							resultBuilder.append("������"+positiveNum).append("\n");
							resultBuilder.append("������"+negativeNum).append("\n");
							int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
							resultBuilder.append("ӯ����"+profit).append("\n");

							totalColl_totalCount = totalColl_totalCount+i+1;
							totalColl_positive = totalColl_positive + positiveNum;
							totalColl_negative = totalColl_negative + negativeNum;
							totalColl_profit = totalColl_profit + profit;
							totalColl_maxPositive = totalColl_maxPositive + maxTemp;
							totalColl_minNegative = totalColl_minNegative + minTemp;
							setResultProfitStop(i+1,positiveNum, negativeNum, profit,
									maxTemp,maxPositionTemp,minTemp,minPositionTemp,
									stop.stopType);
							profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
							break;
					}
				}
				
				if(stop.joinFixedInAllStopOrNot&&stop.stopType==Type.FIXED_STOP){
					if(stop.fixedStop!=null&&index==stop.fixedStop &&sum!=goal){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====�̶�ֹ��=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount +i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								stop.stopType);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
						break;
					}
				}
				
				if(stop.stopType==Type.ALL_STOP){
					
					if(stop.singleStop!=null&&i==stop.singleStop-1&&sum!=goal){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====�ۺ�ֹ��:����=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1, positiveNum, negativeNum, profit,
								max,maxPosition,min,minPosition,
								Type.ALL_STOP_SINGLE_STOP);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
						break;
					}
					if(/*(goal>0&&sum>=goal)||(goal<0&&sum<=goal)*/sum==goal){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====�ۺ�ֹ��:��ֹ��=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								Type.ALL_STOP_NO_STOP);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
						break;
					}
					
					if(stop.joinFixedInAllStopOrNot && stop.fixedStop!=null && index==stop.fixedStop){
						resultBuilder.append("============================").append("\n");
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====�̶�ֹ��=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount +i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + max;
						totalColl_minNegative = totalColl_minNegative + min;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit, 
								max,maxPosition,min,minPosition,
								Type.ALL_STOP_FIXED_STOP);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
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
						resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
						resultBuilder.append("=====�ۺ�ֹ��:��ֵ=====").append("\n");
						resultBuilder.append("������"+positiveNum).append("\n");
						resultBuilder.append("������"+negativeNum).append("\n");
						int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
						resultBuilder.append("ӯ����"+profit).append("\n");
						
						totalColl_totalCount = totalColl_totalCount+i+1;
						totalColl_positive = totalColl_positive + positiveNum;
						totalColl_negative = totalColl_negative + negativeNum;
						totalColl_profit = totalColl_profit + profit;
						totalColl_maxPositive = totalColl_maxPositive + maxTemp;
						totalColl_minNegative = totalColl_minNegative + minTemp;
						setResultProfitStop(i+1,positiveNum, negativeNum, profit,
								maxTemp,maxPositionTemp,minTemp,minPositionTemp,
								Type.ALL_STOP_NUMERICAL_STOP);
						profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
						break;
					}
					
				}
				
				if(/*(goal>=0&&sum>=goal)||(goal<0&&sum<=goal)*/sum==goal){
					resultBuilder.append("============================").append("\n");
					resultBuilder.append("��  "+msg.getPosition()+" ���ź�����").append("\n");
					resultBuilder.append("=====��ֹ��=====").append("\n");
					resultBuilder.append("��ֹ������"+(i+1)).append("\n");
					resultBuilder.append("������"+positiveNum).append("\n");
					resultBuilder.append("������"+negativeNum).append("\n");
					int profit = negativeNum*negativeProfit-positiveNum*positiveProfit+(i+1);
					resultBuilder.append("ӯ����"+profit).append("\n");
					resultBuilder.append("���������"+max).append("\n");
					resultBuilder.append("λ�ã�"+maxPosition).append("\n");
					resultBuilder.append("��С������"+min).append("\n");
					resultBuilder.append("λ�ã�"+minPosition).append("\n");
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
					profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, true);
					break;
				}
				profitByDate.analyse(DateUtil.transFormDate(timeList.get(position)), data, false);
				i++;
				index++;
			}
		}
		//�ܵ�ͳ��
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
