package littleTool.bean;
/**
 * 
 * @author Kaiguang
 * 信号量不满足入场条件
 * 满足入场条件，但还没达到止损条件就终止了
 * 的数据
 *
 */
public class SignalPositionUnEntrance {
	public static final String TYPE_SIGNAL_UN_ENTRANCE = "不满足入场条件";
	public static final String TYPE_SIGNAL_UN_PROFITSTOP = "入场但未达到止损条件就终止";
	public String TYPE ;
	private SignalPositionMsg signalPositionMsg;
	//从信号量后面开始算起
	private int positiveCount;
	private int negativeCount;
	//从入场位置开始算起
	private int positiveCountProfitStop;
	private int negativeCountProfitStop;
	
	public int getSumAfterUnProfitStop(){
		return positiveCountProfitStop - negativeCountProfitStop;
	}
	
	public int getSumAfterUnEntrance(){
		return positiveCount - negativeCount;
	}
	
	public SignalPositionMsg getSignalPositionMsg() {
		return signalPositionMsg;
	}
	public void setSignalPositionMsg(SignalPositionMsg signalPositionMsg) {
		this.signalPositionMsg = signalPositionMsg;
	}
	public int getPositiveCount() {
		return positiveCount;
	}
	public void setPositiveCount(int positiveCount) {
		this.positiveCount = positiveCount;
	}
	public int getNegativeCount() {
		return negativeCount;
	}
	public void setNegativeCount(int negativeCount) {
		this.negativeCount = negativeCount;
	}
	public int getPositiveCountProfitStop() {
		return positiveCountProfitStop;
	}
	public void setPositiveCountProfitStop(int positiveCountProfitStop) {
		this.positiveCountProfitStop = positiveCountProfitStop;
	}
	public int getNegativeCountProfitStop() {
		return negativeCountProfitStop;
	}
	public void setNegativeCountProfitStop(int negativeCountProfitStop) {
		this.negativeCountProfitStop = negativeCountProfitStop;
	}
}
