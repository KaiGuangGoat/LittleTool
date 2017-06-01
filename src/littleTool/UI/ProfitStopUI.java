package littleTool.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jsoup.helper.StringUtil;

import littleTool.FileInputOutput;
import littleTool.bean.NumAndSumParam;
import littleTool.bean.Signal;
import littleTool.bean.SignalRange;
import littleTool.bean.SignalStop;
import littleTool.bean.SignalStop.Type;
import littleTool.bean.SumRegion;

public class ProfitStopUI {
	private static String[] SIGNAL_LABEL = new String[]{"A","B","C","D","E","F","G","H"};
	private JFrame win;
	
	private int startParam = 0;//顺序数第几个开始
//	private Integer numIndexProcessSum;//中间过程出现的设定的数值，结束条件，>=|sum|或<=-|sum|
	
//	private int sumLargeParam =2;
//	private int sumLessParam;
	private SumRegion sumRegion;//求和区间范围
//	private boolean numAndSumParam = false;
	private NumAndSumParam numAndSumParam;
	private int goal=5;
	private int negativeProfit=-28;
	private int positiveProfit=32;
	private SignalStop stop;
	
	private Box box0;
	private Box box1;
	
	StringBuilder params;
	
	public ProfitStopUI(){
		stop = new SignalStop();
		win = new JFrame();
		sumRegion = new SumRegion();
		numAndSumParam = new NumAndSumParam();
		init();
	}
	
	public void init(){
		
		
		box0 = Box.createHorizontalBox();
		box1 = Box.createHorizontalBox();
//		box2 = Box.createHorizontalBox();
		for(int i=0;i<8;i++){
			final ButtonGroup signalBtnGro = new ButtonGroup();
			final JRadioButton moreRadio = new JRadioButton("大于或等于");
			moreRadio.setSelected(true);
			JRadioButton equalRadio = new JRadioButton("等于");
			signalBtnGro.add(moreRadio);
			signalBtnGro.add(equalRadio);
			
			Box box01 = Box.createVerticalBox();
			box01.add(new JLabel(SIGNAL_LABEL[i]));
			box01.add(moreRadio);
			box01.add(equalRadio);
			
//			box0.add(new JLabel("  [----"+SIGNAL_LABEL[i]+":"));
//			box0.add(moreRadio);
//			box0.add(equalRadio);
			box0.add(box01);
			
			
			JCheckBox posOrNegCheckMoreBox = new JCheckBox();
			JLabel positiveMoreLabel = new JLabel("  "+SIGNAL_LABEL[i]+"：");
			JTextField positiveMoreJTF = new JTextField();
			positiveMoreJTF.setMaximumSize(new Dimension(40, 40));
			FileInputOutput.onlyInputNum(positiveMoreJTF);
			JTextField negativeMoreJTF = new JTextField();
			negativeMoreJTF.setMaximumSize(new Dimension(40, 40));
			FileInputOutput.onlyInputNum(negativeMoreJTF);
			box1.add(posOrNegCheckMoreBox);
			box1.add(new JLabel("正或负_"));
			box1.add(positiveMoreLabel);
			box1.add(positiveMoreJTF);
			box1.add(Box.createHorizontalStrut(10));
		}
		
		JLabel beginIndexLabel = new JLabel("起始位置");
		Box box3 = Box.createVerticalBox();
		final Box box3_1 = Box.createHorizontalBox();
		box3_1.add(new JCheckBox("1、后面顺数第几个：",true));
		
		final JTextField numIndexJTF = new JTextField("1");
		FileInputOutput.onlyInputNum(numIndexJTF);
		numIndexJTF.setMaximumSize(new Dimension(200,100));
		box3_1.add(numIndexJTF);
		
		box3_1.add(new JLabel("   过程中设定的最大正数或最小负数："));
		final JTextField numIndexProcessSumJTF = new JTextField("");
		FileInputOutput.onlyInputNum(numIndexProcessSumJTF);
		numIndexProcessSumJTF.setMaximumSize(new Dimension(200,100));
		box3_1.add(numIndexProcessSumJTF);
		final JCheckBox pstOrNgtNumIndexProcessSum = new JCheckBox("正数",true);
		box3_1.add(pstOrNgtNumIndexProcessSum);
		final JCheckBox numIndexProcessSelect = new JCheckBox("选择",true);
		box3_1.add(numIndexProcessSelect);
		ButtonGroup enterOrClose = new ButtonGroup();
		final JRadioButton enterJbtn = new JRadioButton("满足则进入",true);
		JRadioButton closeJbtn = new JRadioButton("满足则结束",false);
		enterOrClose.add(enterJbtn);
		enterOrClose.add(closeJbtn);
		box3_1.add(enterJbtn);
		box3_1.add(closeJbtn);
		
		
		box3.add(box3_1);
		box3.add(Box.createVerticalStrut(10));
		
		Box box3_2 = Box.createHorizontalBox();
		box3_2.add(new JLabel("2、后面求和达到某值数或范围： >=大于或等于"));
		final JTextField sumIndexLargeJTF = new JTextField();
		FileInputOutput.onlyInputNum(sumIndexLargeJTF);
		sumIndexLargeJTF.setMaximumSize(new Dimension(200,100));
		box3_2.add(sumIndexLargeJTF);
		final JCheckBox pstOrNgtSum = new JCheckBox("正数   ",true);
		box3_2.add(pstOrNgtSum);
		
		box3_2.add(new JLabel(" <=小于或等于"));
		final JTextField sumIndexLessJTF = new JTextField();
		FileInputOutput.onlyInputNum(sumIndexLessJTF);
		sumIndexLessJTF.setMaximumSize(new Dimension(200,100));
		box3_2.add(sumIndexLessJTF);
		final JCheckBox pstOrNgtSumLess = new JCheckBox("正数 ",true);
		box3_2.add(pstOrNgtSumLess);
		
		box3.add(box3_2);
		box3.add(Box.createVerticalStrut(10));
		final JCheckBox numAndSumIndex = new JCheckBox("1和2是否同时满足");
		box3.add(numAndSumIndex);
		
		Box box4 = Box.createHorizontalBox();
		box4.add(new JLabel("目标数值："));
		final JTextField goalJTF = new JTextField();
		FileInputOutput.onlyInputNum(goalJTF);
		goalJTF.setMaximumSize(new Dimension(300,100));
		final JCheckBox pstOrNgtGoal = new JCheckBox("目标数值是否为正数",true);
		box4.add(goalJTF);
		box4.add(pstOrNgtGoal);
		
		Box box5 = Box.createHorizontalBox();
		box5.add(new JLabel("止损方式："));
		ButtonGroup stopButtons = new ButtonGroup();
		final JRadioButton noStopBtn = new JRadioButton("无止损");
		noStopBtn.setSelected(true);
		stopButtons.add(noStopBtn);
		final JRadioButton signalStopBtn = new JRadioButton("单量止损");
		stopButtons.add(signalStopBtn);
		final JRadioButton numericalStopBtn = new JRadioButton("数值止损");
		stopButtons.add(numericalStopBtn);
		final JRadioButton allStopBtn = new JRadioButton("综合止损");
		stopButtons.add(allStopBtn);
		final JRadioButton fixedStopBtn = new JRadioButton("固定止损");
		stopButtons.add(fixedStopBtn);
		final JCheckBox joinFixedStopBtnOrNot = new JCheckBox("是否把固定止损加入到综合止损里面",false);
		
		box5.add(noStopBtn);
		box5.add(signalStopBtn);
		box5.add(numericalStopBtn);
		box5.add(allStopBtn);
		box5.add(fixedStopBtn);
		box5.add(joinFixedStopBtnOrNot);
		
		Box box6 = Box.createHorizontalBox();
		box6.add(new JLabel("单量止损："));
		final JTextField signalJTF = new JTextField();
		signalJTF.setMaximumSize(new Dimension(100,100));
		FileInputOutput.onlyInputNum(signalJTF);
		box6.add(signalJTF);
		final JCheckBox pstOrNgtSignal = new JCheckBox("正或负",true);
		box6.add(pstOrNgtSignal);
		box6.add(new JLabel("数值止损："));
		final JTextField numerJTF = new JTextField();
		numerJTF.setMaximumSize(new Dimension(100,100));
		FileInputOutput.onlyInputNum(numerJTF);
		box6.add(numerJTF);
		final JCheckBox pstOrNgtNumer = new JCheckBox("正或负",true);
		box6.add(pstOrNgtNumer);
		box6.add(new JLabel("数值止损起始位置："));
		ButtonGroup numerGroup = new ButtonGroup();
		final JRadioButton numerOneJRB = new JRadioButton("原始位置后起始",true);
		JRadioButton numerTwoJRB = new JRadioButton("满足条件的位置后起始");
		numerGroup.add(numerOneJRB);
		numerGroup.add(numerTwoJRB);
		box6.add(numerOneJRB);
		box6.add(numerTwoJRB);
		box6.add(new JLabel("    固定止损："));
		final JTextField fixedJTF = new JTextField();
		FileInputOutput.onlyInputNum(fixedJTF);
		fixedJTF.setMaximumSize(new Dimension(100,100));
		box6.add(fixedJTF);
		
		Box box7 = Box.createHorizontalBox();
		box7.add(new JLabel("负数盈利变量参数："));
		final JTextField negativeProfitJTF = new JTextField();
		negativeProfitJTF.setMaximumSize(new Dimension(100,100));
		FileInputOutput.onlyInputNum(negativeProfitJTF);
		box7.add(negativeProfitJTF);
		final JCheckBox postOrNgtNegProfit = new JCheckBox("正或负",true);
		box7.add(postOrNgtNegProfit);
		box7.add(new JLabel("正数盈利变量参数："));
		final JTextField positiveProfitJTF = new JTextField();
		positiveProfitJTF.setMaximumSize(new Dimension(100,100));
		FileInputOutput.onlyInputNum(positiveProfitJTF);
		box7.add(positiveProfitJTF);
		final JCheckBox postOrNgtPosProfit = new JCheckBox("正或负",true);
		box7.add(postOrNgtPosProfit);
		
		Box box = Box.createVerticalBox();
		box.add(new JLabel("信号量"));
		box.add(box0);
		box.add(box1);
//		box.add(box2);
		box.add(Box.createVerticalStrut(10));
		box.add(beginIndexLabel);
		box.add(box3);
		box.add(Box.createVerticalStrut(10));
		box.add(box4);
		box.add(Box.createVerticalStrut(10));
		box.add(box5);
		box.add(Box.createVerticalStrut(10));
		box.add(box6);
		box.add(Box.createVerticalStrut(10));
		box.add(new JLabel("盈利参数"));
		box.add(box7);
		JButton sure = new JButton("确定修改");
		sure.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				params = new StringBuilder();
				params.append("=========================\n");
				setSignalList();
				params.append("信号量:").append(Signal.REGEX()).append("\n");
				if(!StringUtil.isBlank(numIndexJTF.getText())){
					startParam = Integer.parseInt(numIndexJTF.getText());
					if(!((JCheckBox)box3_1.getComponent(0)).isSelected()){
						startParam = 0;
					}
					if(numAndSumIndex.isSelected()){
						startParam = Integer.parseInt(numIndexJTF.getText());
					}
					params.append("开始位置:").append(startParam).append("\n");
				}
				
				
				if(numAndSumIndex.isSelected()){
					params.append("\n===开始位置选择和求和达到某值同时满足===").append("\n");
					if(!StringUtil.isBlank(numIndexProcessSumJTF.getText())&&numIndexProcessSelect.isSelected()){
						int numIndexProcessSum = getNum(numIndexProcessSumJTF,pstOrNgtNumIndexProcessSum.isSelected());
						numAndSumParam.setNumIndexProcessSum(numIndexProcessSum);
						params.append("过程中设定的最大正数或最小负数：").append(numIndexProcessSum).append("\n");
					}
					
					if(!StringUtil.isBlank(sumIndexLargeJTF.getText())){
						sumRegion.setFirstNum(getNum(sumIndexLargeJTF, pstOrNgtSum.isSelected()));
//						sumLargeParam = getNum(sumIndexLargeJTF, pstOrNgtSum.isSelected());
						params.append("求和达到某值数或范围,>=大于或等于：").append(sumRegion.getFirstNum()).append("\n");
					}else{
						sumRegion.setFirstNum(null);
					}
					
					if(!StringUtil.isBlank(sumIndexLessJTF.getText())){
						sumRegion.setSecondNum(getNum(sumIndexLessJTF,  pstOrNgtSumLess.isSelected()));
						params.append("求和达到某值数或范围,<=小于或等于：").append(sumRegion.getSecondNum()).append("\n");
					}else{
						sumRegion.setSecondNum(null);
					}
					params.append("\n");
					numAndSumParam.isTrue = true;
					numAndSumParam.setNumIProSumEnterOrClose(enterJbtn.isSelected());
					numAndSumParam.setSumRegion(sumRegion);
					numAndSumParam.setNumIndexProcessSelect(numIndexProcessSelect.isSelected());
					startParam = Integer.parseInt(numIndexJTF.getText());
				}else{
					if(!StringUtil.isBlank(sumIndexLargeJTF.getText())){
						sumRegion.setFirstNum(getNum(sumIndexLargeJTF, pstOrNgtSum.isSelected()));
//						sumLargeParam = getNum(sumIndexLargeJTF, pstOrNgtSum.isSelected());
						params.append("求和达到某值数：").append(sumRegion.getFirstNum()).append("\n");
					}
					numAndSumParam.isTrue = false;
				}
				
				if(!StringUtil.isBlank(goalJTF.getText())){
					goal = getNum(goalJTF, pstOrNgtGoal.isSelected());
					params.append("目标数值:").append(goal).append("\n");
				}
				
				if(noStopBtn.isSelected()){
					stop.stopType = Type.NO_STOP;
					params.append("止损方式:").append("无止损").append("\n");
				}
				if(signalStopBtn.isSelected()){
					stop.stopType = Type.SINGLE_STOP;
					params.append("止损方式:").append("单量止损").append("\n");
				}
				if(numericalStopBtn.isSelected()){
					stop.stopType = Type.NUMERICAL_STOP;
					params.append("止损方式:").append("数值止损").append("\n");
				}
				
				if(fixedStopBtn.isSelected()){
					stop.stopType = Type.FIXED_STOP;
					params.append("止损方式：").append("固定止损").append("\n");
				}
				if(allStopBtn.isSelected()){
					stop.stopType = Type.ALL_STOP;
					params.append("止损方式：").append("综合止损").append("\n");
					stop.joinFixedInAllStopOrNot = joinFixedStopBtnOrNot.isSelected();
				}
				if(!StringUtil.isBlank(signalJTF.getText())){
					stop.singleStop = getNum(signalJTF, pstOrNgtSignal.isSelected());
					params.append("单量止损量:").append(stop.singleStop).append("\n");
				}
				if(!StringUtil.isBlank(numerJTF.getText())){
					stop.numericalStop = getNum(numerJTF, pstOrNgtNumer.isSelected());
					params.append("数值止损量:").append(stop.numericalStop).append("\n");
				}
				if(!StringUtil.isBlank(fixedJTF.getText())){
					stop.fixedStop = getNum(fixedJTF,true);
					params.append("固定止损:").append(stop.fixedStop).append("\n");
				}
				
				stop.numericalStopStarBegin = numerOneJRB.isSelected();
				if(stop.numericalStopStarBegin){
					params.append("数值止损起始位置为原始位置后开始\n");
				}else{
					params.append("数值止损起始位置为满足条件的位置后开始\n");
				}
				
				if(!StringUtil.isBlank(negativeProfitJTF.getText())){
					negativeProfit = getNum(negativeProfitJTF, postOrNgtNegProfit.isSelected());
					params.append("负数盈利变量参数:").append(negativeProfit).append("\n");
				}
				
				if(!StringUtil.isBlank(positiveProfitJTF.getText())){
					positiveProfit = getNum(positiveProfitJTF, postOrNgtPosProfit.isSelected());
					params.append("正数盈利变量参数:").append(positiveProfit).append("\n");
				}
				params.append("=========================\n");
				
				JOptionPane.showMessageDialog(null, params.toString());
//				win.setVisible(false);
			}
		});
		box.add(sure);
		
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		panel.add(box);
		
		win.setTitle("详细设置(注意：设置好参数后切记要点击 确定修改 按钮，否则不能生效！！！)"); // 窗体的标题
		win.setDefaultCloseOperation(JFrame.NORMAL);
		win.setContentPane(panel);
		
		win.setSize(new Dimension(1500, 600));
//		win.setLocationRelativeTo(null);
	}
	
	public void open(){
		win.setVisible(true);
	}
	
	private int getNum(JTextField jtf,boolean positiveNum){
		String text = jtf.getText();
//		if(text==null||"".equals(text)){
//			return null;
//		}
		int num = Integer.parseInt(text);
		return positiveNum?num:num*-1;
	}
	
	private void setSignalList(){
		Signal.signalList.removeAll(Signal.signalList);
		SignalRange range = SignalRange.MORE;
//		Component[] comp0 = box0.getComponents();
		Component[] radioBoxs = box0.getComponents();
		Component[] comp = box1.getComponents();
		
		for(int i=0;i<8;i++){
			
			JTextField signalJTF = (JTextField) comp[i*5+3];
			boolean checked = ((JCheckBox)comp[i*5+0]).isSelected();
			String countText = signalJTF.getText();
			if(StringUtil.isBlank(countText)){
				continue;
			}
			Signal signal = new Signal();
			signal.setCount(Integer.parseInt(countText));
//			boolean more = ((JRadioButton)comp0[i*3+1]).isSelected();
			Box radioBox = (Box) radioBoxs[i];
			boolean more = ((JRadioButton)(radioBox.getComponent(1))).isSelected();
			if(more){
				range = SignalRange.MORE;
			}else{
				range = SignalRange.EQUALS;
			}
			signal.setRange(range);
			if(checked){
				signal.setPositiveNum(true);
			}else{
				signal.setPositiveNum(false);
			}
			Signal.signalList.add(signal);
		}
	}
	
	public String getParams(){
		if(params==null){
			return null;
		}
		return params.toString();
	}
	
	public int getStartParam() {
		return startParam;
	}

	
	public NumAndSumParam getNumAndSumParam() {
		return numAndSumParam;
	}

	public int getGoal() {
		return goal;
	}

	public int getNegativeProfit() {
		return negativeProfit;
	}

	public int getPositiveProfit() {
		return positiveProfit;
	}

	public SumRegion getSumRegion() {
		return sumRegion;
	}

	public SignalStop getStop() {
		
		
		
		return stop;
	}
}
