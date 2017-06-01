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
				typeStr = "ÎÞÖ¹Ëð";
				break;
			case SINGLE_STOP:
				typeStr = "µ¥Á¿Ö¹Ëð";
				break;
			case NUMERICAL_STOP:
				typeStr = "ÊýÖµÖ¹Ëð";
				break;
			case ALL_STOP:
				typeStr = "×ÛºÏÖ¹Ëð";
				break;
			case ALL_STOP_NO_STOP:
				typeStr = "×ÛºÏÖ¹Ëð£ºÎÞÖ¹Ëð";
				break;
			case ALL_STOP_SINGLE_STOP:
				typeStr = "×ÛºÏÖ¹Ëð£ºµ¥Á¿Ö¹Ëð";			
				break;
			case ALL_STOP_NUMERICAL_STOP:
				typeStr = "×ÛºÏÖ¹Ëð£ºÊýÖµÖ¹Ëð";
				break;
			case ALL_STOP_FIXED_STOP:
				typeStr = "×ÛºÏÖ¹Ëð£º¹Ì¶¨Ö¹Ëð";
				break;

			default:
				break;
			}
			return typeStr;
		}
	}
	
	public Type stopType = Type.NO_STOP;
	public int singleStop;
	public int numericalStop;
	public int fixedStop;
	public boolean numericalStopStarBegin;
	public boolean joinFixedInAllStopOrNot;
}
