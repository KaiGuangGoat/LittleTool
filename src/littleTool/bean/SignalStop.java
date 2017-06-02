package littleTool.bean;


public class SignalStop {
	public static enum Type{
		NO_STOP,
		SINGLE_STOP,
		NUMERICAL_STOP,
		ALL_STOP,
		FIXED_STOP,
		ALL_STOP_NO_STOP,
		ALL_STOP_SINGLE_STOP,
		ALL_STOP_NUMERICAL_STOP,
		ALL_STOP_FIXED_STOP;
		public String getTypeStr(Type type){
			String typeStr = "";
			switch (type) {
			case NO_STOP:
				typeStr = "��ֹ��";
				break;
			case SINGLE_STOP:
				typeStr = "����ֹ��";
				break;
			case NUMERICAL_STOP:
				typeStr = "��ֵֹ��";
				break;
			case ALL_STOP:
				typeStr = "�ۺ�ֹ��";
				break;
			case ALL_STOP_NO_STOP:
				typeStr = "�ۺ�ֹ����ֹ��";
				break;
			case ALL_STOP_SINGLE_STOP:
				typeStr = "�ۺ�ֹ�𣺵���ֹ��";			
				break;
			case ALL_STOP_NUMERICAL_STOP:
				typeStr = "�ۺ�ֹ����ֵֹ��";
				break;
			case ALL_STOP_FIXED_STOP:
				typeStr = "�ۺ�ֹ�𣺹̶�ֹ��";
				break;

			default:
				break;
			}
			return typeStr;
		}
	}
	
	public Type stopType = Type.NO_STOP;
	public Integer singleStop;
	public Integer numericalStop;
	public Integer fixedStop;
	public boolean numericalStopStarBegin;
	public boolean joinFixedInAllStopOrNot;
}
