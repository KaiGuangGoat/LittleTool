package littleTool.bean;

public class SumRegion {
	private Integer firstNum;
	private Integer secondNum;
	
	public boolean firstEqualSecond(){
		if(firstNum!=null&&secondNum!=null){
			return firstNum.equals(secondNum);
		}
		return false;
	}
	
	public boolean onlyFirstNotNull(){
		return firstNum!=null&&secondNum==null;
	}
	
	public boolean onlySecondNotNull(){
		return firstNum==null&&secondNum!=null;
	}
	
	public boolean allNotNull(){
		return firstNum!=null&&secondNum!=null;
	}
	
	public boolean allNull(){
		return firstNum==null&&secondNum==null;
	}
	
	
	public Integer getFirstNum() {
		if(firstNum == null){
			return 0;
		}
		return firstNum;
	}
	public void setFirstNum(Integer firstNum) {
		this.firstNum = firstNum;
	}
	public Integer getSecondNum() {
		return secondNum;
	}
	public void setSecondNum(Integer secondNum) {
		this.secondNum = secondNum;
	}
}
