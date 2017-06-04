package littleTool.bean;
/**
 * 
 * @author Kaiguang
 * �ź����������볡����
 * �����볡����������û�ﵽֹ����������ֹ��
 * ������
 *
 */
public class SignalPositionUnEntrance {
	public static final String TYPE_SIGNAL_UN_ENTRANCE = "�������볡����";
	public static final String TYPE_SIGNAL_UN_PROFITSTOP = "�볡��δ�ﵽֹ����������ֹ";
	public String TYPE ;
	private SignalPositionMsg signalPositionMsg;
	//���ź������濪ʼ����
	private int positiveCount;
	private int negativeCount;
	//���볡λ�ÿ�ʼ����
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
