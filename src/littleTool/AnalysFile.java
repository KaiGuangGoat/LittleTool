package littleTool;

import java.util.List;

public class AnalysFile {
	
	private int beginIndex = 1;
	private int analyLen = 50;//分析条数的单位，比如以50条数据为单位分析
	private int beginPercent = 30;
	private int endPercent = 40;
	private boolean begin = false;
	private boolean end = false;
	
	public String profitOrLost(List<Integer> dataList){
		StringBuilder result = new StringBuilder();
		int dataSize = dataList.size();
		int profitZoneBegin = 0;
		int profitZoneEnd = 0;
		int profitZoneCount = 0;
		for(int i=0;i<dataSize-analyLen;i++){
			int totalPlus = 0;
			int positiveNumCount = 0;
			int negativeNumCount = 0;
			for(int j=i;j<analyLen+i;j++){
				int data = dataList.get(j);
				totalPlus += data;
				if(data>0){
					positiveNumCount++;
				}
				if(data<0){
					negativeNumCount++;
				}
			}
			Float positivePercent = (float) (positiveNumCount*100/analyLen);
			Float negativePercent = (float) (negativeNumCount*100/analyLen);
			if(!begin){
				if(positivePercent>beginPercent){
					begin = true;
//					end = false;
					profitZoneBegin = i;
					result.append("====================================").append("\r\n");
					result.append("profitZoneBegin:").append(i).append("\r\n");
					result.append("====================================").append("\r\n");
				}
			}
			
			if(!end){
				if(begin&&positivePercent>endPercent){
					end = true;
//					begin = false;
					profitZoneEnd = i;
					result.append("++++++++++++++++++++++++++++++++++++").append("\r\n");
					result.append("profitZoneEnd:").append(i).append("\r\n");
					result.append("经过订单数量：").append(profitZoneEnd-profitZoneBegin).append("\r\n");
					result.append("\r\n").append("++++++++++++++++++++++++++++++++++++").append("\r\n");
				}
			}
			
			result.append(1+i).append("--").append(analyLen+i);
			result.append("    ").append(positivePercent).append("%");
			result.append("    ").append(negativePercent).append("%");
			result.append("    ").append(totalPlus);
			result.append("\r\n");
			result.append("\r\n");
		}
		String resultStr = result.toString();
		/*System.out.println(resultStr);*/
		return resultStr;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public void setEndPercent(int endPercent) {
		this.endPercent = endPercent;
	}

	public void setAnalyLen(int analyLen) {
		this.analyLen = analyLen;
	}

	public void setBeginPercent(int beginPercent) {
		this.beginPercent = beginPercent;
	}

}
