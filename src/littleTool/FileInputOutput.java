package littleTool;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import littleTool.UI.DataTableModel;
import littleTool.UI.ProfitStopUI;
import littleTool.bean.NumAndSumParam;
import littleTool.bean.ResultProfitStop;
import littleTool.bean.SignalPositionUnEntrance;
import littleTool.bean.SignalStop;
import littleTool.out.OutByExcel;
import littleTool.utils.ExceptionUtil;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FileInputOutput {
	
	private String failMsg;
	
	private String outFileName;
	
	private StringBuilder textResult;
	private StringBuilder htmResult;
	
//	private int beginIndex;
//	private int analyLen ;
//	private int beginPercent ;
//	private int endPercent ;
	private JTextArea outMsg;
	private ProfitStopUI profitStopUI;
	
	private List<ResultProfitStop> resultProStopList;
	private List<SignalPositionUnEntrance> unEntranceList;
	
	private JTable table;
	
	private Box box;
	private JFrame win;
	
	public FileInputOutput(){
		profitStopUI = new ProfitStopUI();
		table = new JTable();
		box = Box.createVerticalBox();
		win = new JFrame();
	}

	public boolean analyse(String inputFile, String outputFile) {
		try {
			File file = new File(inputFile);
			if(file.isDirectory()){
				failMsg = "�����ļ�����Ϊ·��";
				return false;
			}
			if (!file.exists()) {
				failMsg = "�����ļ�������";
				return false;
			}
			Document document = Jsoup.parse(file, "UTF-8");
			Elements elements = document.getElementsByClass("mspt");
			if (elements != null) {
				if (elements.isEmpty()) {
					failMsg = "�ļ����ݲ���";
					return false;
				}
				analyseParse(file);
				return fileOutput(outputFile);
				
			} else {
				failMsg = "�ļ����ݲ���";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log("analyse:"+ExceptionUtil.getExceptionMsg(e));
			failMsg = "����������ļ���������ļ��Ƿ���ȷ��";
		}
		return false;
	}
	
	private boolean fileOutput(String outputPath){
		String outFileName = "out";
		if(!StringUtil.isBlank(this.outFileName)){
			outFileName = this.outFileName;
		}
		File file = new File(outputPath);
		if(!file.exists()){
			file.mkdirs();
		}
		String currentTime;
		Date date = new Date(System.currentTimeMillis());
		currentTime = date.toLocaleString().replace(":", "_");
		outByExcel(outputPath+File.separatorChar+outFileName+currentTime);
		
		boolean txtResultBool = fileOutput(outputPath+File.separatorChar+outFileName+currentTime+".txt",textResult.toString());
		boolean htmResultBool = fileOutput(outputPath+File.separatorChar+outFileName+currentTime+".htm", htmResult.toString());
		return txtResultBool|htmResultBool;
	}
	
	private void outByExcel(String outName){
		OutByExcel excel = new OutByExcel();
		excel.out(resultProStopList, outName+".xls");
		excel.outUnEntrance(unEntranceList, outName+"_δ�볡��δ�ﵽֹ���.xls");
	}

	private static boolean fileOutput(String filePath,String content) {
		if (content == null){
			return false;
		}
			
		FileOutputStream fos = null;
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		failMsg = "����������ļ���������ļ��Ƿ���ȷ��";
		return false;
	}

//	public void input() {
//		Scanner scan = new Scanner(System.in);
//		System.out.println("��������Ҫ������ļ���������enter��������");
//		inputFile = scan.nextLine();
//		System.out.println("��������Ҫ�������ļ���������enter��������");
//		outputFile = scan.nextLine();
//		analyse(inputFile, outputFile);
//		scan.close();
//	}

	private String selectedInputFile = null;
	private String selectedOutputFile = null;
	public void dialog() {
		profitStopUI.open();
		Box box1 = Box.createHorizontalBox();
		box1.add(new JLabel("ѡ������ļ���"));
		final JTextField input = new JTextField();
		input.setMaximumSize(new Dimension(400, 50));
		box1.add(input);
		JButton selectInputFile = new JButton("ѡ��");
		box1.add(selectInputFile);
		selectInputFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = null;
				if(selectedInputFile != null){
					jfc = new JFileChooser(selectedInputFile);
				}else{
					jfc = new JFileChooser();
				}
				
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "ѡ��");
				File file = jfc.getSelectedFile();
				if(file != null){
					input.setText(file.getAbsolutePath());
					selectedInputFile = file.getParentFile().getAbsolutePath();
					outFileName = file.getName();
				}
			}
		});

		Box box2 = Box.createHorizontalBox();
		box2.add(new JLabel("ѡ�񵼳���Ŀ¼��"));
		final JTextField output = new JTextField();
		output.setMaximumSize(new Dimension(400, 50));
		box2.add(output);
		JButton selectOutputFolder = new JButton("ѡ��");
		box2.add(selectOutputFolder);
		selectOutputFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JFileChooser jfc = null;
				if(selectedOutputFile != null){
					jfc = new JFileChooser(selectedOutputFile);
				}else{
					jfc = new JFileChooser();
				}
				
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "ѡ��");
				File file = jfc.getSelectedFile();
				if(file != null){
					output.setText(file.getAbsolutePath());
					selectedOutputFile = file.getAbsolutePath();
				}
			}
		});
		
//		JButton analysDetailBtn = new JButton("��ϸ��������...");
//		analysDetailBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				
//				profitStopUI.open();
//			}
//		});
		
//		JLabel conditionSet = new JLabel("���÷���������");
		
		Box boxBegin = Box.createHorizontalBox();
		boxBegin.add(new JLabel("��ʼ������λ�ã�������Ĭ�ϴӵ�һ�����ݿ�ʼ��"));
		final JTextField jtfBeginIndex = new JTextField("1");
		jtfBeginIndex.setPreferredSize(new Dimension(50, 50));
		onlyInputNum(jtfBeginIndex);
		boxBegin.add(jtfBeginIndex);
		
		Box box3 = Box.createHorizontalBox();
		box3.add(new JLabel("����������λ������������50��Ϊ��λ��"));
		final JTextField jtfAnalyLen = new JTextField("50");
		jtfAnalyLen.setPreferredSize(new Dimension(50, 50));
		onlyInputNum(jtfAnalyLen);
		box3.add(jtfAnalyLen);
		
		Box box4 = Box.createHorizontalBox();
		box4.add(new JLabel("��ʼ�İٷֱȣ���������:0~100������30��"));
		final JTextField jtfBeginPercent = new JTextField("30");
		jtfBeginPercent.setPreferredSize(new Dimension(50, 50));
		onlyInputNum(jtfBeginPercent);
		box4.add(jtfBeginPercent);
		box4.add(new JLabel("%"));
		
		Box box5 = Box.createHorizontalBox();
		box5.add(new JLabel("�����İٷֱȣ���������:0~100������40��"));
		final JTextField jtfEndPercent = new JTextField("40");
		jtfEndPercent.setPreferredSize(new Dimension(50, 50));
		onlyInputNum(jtfEndPercent);
		box5.add(jtfEndPercent);
		box5.add(new JLabel("%"));
		
		JLabel outMsgLabel = new JLabel("��������");
		outMsg = new JTextArea();
		JScrollPane scroll = new JScrollPane(outMsg);
		scroll.setMinimumSize(new Dimension(200,300));
		
		JButton button = new JButton("ȷ��");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String inputText = input.getText();
				String outputText = output.getText();
				
//				beginIndex = Integer.parseInt(jtfBeginIndex.getText());
//				analyLen = Integer.parseInt(jtfAnalyLen.getText());
//				beginPercent = Integer.parseInt(jtfBeginPercent.getText());
//				endPercent = Integer.parseInt(jtfEndPercent.getText());
				
				if (!StringUtil.isBlank(inputText)
						&& !StringUtil.isBlank(outputText)) {
					boolean success = analyse(inputText, outputText);
//					JDialog dialog = new JDialog();
//					dialog.setTitle("����ʧ��");
//					dialog.add(new JLabel(failMsg));
					if(success){
//						dialog.setTitle("�����ɹ�");
//						dialog.add(new JLabel("����ļ�Ŀ¼��"+outputText));
//						dialog.setSize(new Dimension(300,200));
//						dialog.setLocationRelativeTo(null);
//						dialog.setVisible(true);
					}else{
						outMsg.setText(failMsg);
					}
					
				}else{
					outMsg.setText("����Ŀ¼�����Ŀ¼Ϊ�գ�����");
				}
			}
		});
		
		
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setMinimumSize(new Dimension(200,400));
		
		box.add(Box.createVerticalStrut(10));
		box.add(box1);
		box.add(Box.createVerticalStrut(10));
		box.add(box2);
//		box.add(Box.createVerticalStrut(10));
//		box.add(conditionSet);
//		box.add(Box.createVerticalStrut(10));
//		box.add(boxBegin);
//		box.add(Box.createVerticalStrut(10));
//		box.add(box3);
//		box.add(Box.createVerticalStrut(10));
//		box.add(box4);
//		box.add(Box.createVerticalStrut(10));
//		box.add(box5);
		box.add(Box.createVerticalStrut(10));
//		box.add(analysDetailBtn);
		box.add(Box.createVerticalStrut(10));
		box.add(outMsgLabel);
		box.add(scroll);
		box.add(Box.createVerticalStrut(10));
		box.add(button);
		box.add(tableScroll);

		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		panel.add(box);

		
		win.setTitle("�ļ��������"); // ����ı���
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setContentPane(panel);
		win.setSize(new Dimension(900, 900));
		win.setLocationRelativeTo(null);
		win.setVisible(true);
	}
	
	public void analyseParse(File inputFile){
		
		try {
			Document document = Jsoup.parse(inputFile, "UTF-8");
			Elements elements = document.getElementsByTag("table");
			Element table2 = elements.get(1);
			Elements trs = table2.getElementsByTag("tr");
			textResult = new StringBuilder();
			StringBuilder textData = new StringBuilder();
			htmResult = new StringBuilder();
			htmResult.append("<div align=center>");
			htmResult.append("<table width=400 cellspacing=\"1\" cellpadding=\"3\" border=\"0\">");
			htmResult.append("<tbody>");
			int i = 0;
			int rightCount = 0;
			int wrongCount = 0;
			List<Integer> dataList = new ArrayList<Integer>();
			List<String> timeList = new ArrayList<String>();
			
			for(Element tr:trs){
				if(i==0){
					i++;
					continue;
				}
				String buff = tr.child(8).text();
				String time = tr.child(1).text();
				try {
					float value = Float.valueOf(buff);
					i++;
					if(value>0){
						dataList.add(1);
						textResult.append("1");
						textResult.append("\r\n");
						textData.append("��");
						setRight(htmResult, i+"",time,buff);
						rightCount++;
					}else if(value<0){
						dataList.add(-1);
						textResult.append("-1");
						textResult.append("\r\n");
						textData.append("x");
						setWrong(htmResult, i+"",time,buff);
						wrongCount++;
					}else{
						textResult.append(value);
						textResult.append("\r\n");
						htmResult.append("<tr align=\"right\">");
						htmResult.append("<td>").append(i).append("</td>");
						htmResult.append("<td>").append(time).append("</td>");
						htmResult.append("<td>").append(buff).append("</td>");
						htmResult.append("<td>").append("0").append("</td>");
						htmResult.append("</tr>");
					}
				} catch (Exception e) {
					failMsg = ExceptionUtil.getExceptionMsg(e);
					System.out.println(time);
					timeList.add(time);
				}
			}
//			Element valueElement = trs.get(2).child(8);
//			System.out.println(valueElement.text());
			System.out.println("dataList len:"+dataList.size());
			System.out.println("timeList len:"+timeList.size());
			
			htmResult.append("</tbody>");
			htmResult.append("</table>");
			htmResult.append("</div>");
			
			AnalysSignal analysSignal = new AnalysSignal(dataList, textData.toString());
			analysSignal.setTimeList(timeList);
			
			int startParam = profitStopUI.getStartParam();
			int sumParam =profitStopUI.getSumRegion().getFirstNum();
			NumAndSumParam numAndSumParam = profitStopUI.getNumAndSumParam();
			int goal=profitStopUI.getGoal();
			int negativeProfit=profitStopUI.getNegativeProfit();
			int positiveProfit=profitStopUI.getPositiveProfit();
			SignalStop stop=profitStopUI.getStop();
			String findResult = analysSignal.find();
			if(findResult==null){
				outMsg.setText("δ�����ź���");
				return;
			}
			String analyResult = analysSignal.analysStopLost(startParam,
					sumParam, numAndSumParam,goal, negativeProfit, positiveProfit,stop);
			
			outMsg.setText(profitStopUI.getParams()+"\n"+findResult+"\n"+analyResult);
//			ProfitStopDataTable.showTable(analysSignal.getOutputDataList());
			resultProStopList = analysSignal.getOutputDataList();
			unEntranceList = analysSignal.getUnEntrancdList();
			table.setModel(new DataTableModel(resultProStopList));
			win.repaint();
//			fileOutput("H:\\��Ŀ\\����ʦ��\\test.htm", htmResult.toString());
		} catch (IOException e) {
			e.printStackTrace();
			failMsg = ExceptionUtil.getExceptionMsg(e);
			fileOutput("D:\\log.txt",failMsg);
		}
	}
	
	private void setRight(StringBuilder sb,String ...tdTexts){
		sb.append("<tr bgcolor=\"#E0E0E0\" align=\"right\">");
		for(String tdText:tdTexts){
			sb.append("<td>").append(tdText).append("</td>");
		}
		sb.append("<td>").append("��").append("</td>");
		sb.append("</tr>");
	}
	
	private void setWrong(StringBuilder sb,String ...tdTexts){
		sb.append("<tr align=\"right\">");
		for(String tdText:tdTexts){
			sb.append("<td>").append(tdText).append("</td>");
		}
		sb.append("<td>").append("��").append("</td>");
		sb.append("</tr>");
	}
	
	public static void onlyInputNum(JTextField textField){
		textField.addKeyListener(new KeyListener() {  
		    @Override  
		    public void keyTyped(KeyEvent e) {  
		        // ֻ����������  
		        char keyCh = e.getKeyChar();  
		        if ((keyCh < '0') || (keyCh > '9')) {  
		            if (keyCh != '\n') // �س��ַ�  
		                e.setKeyChar('\0');  
		        }  
		    }  
		  
		    @Override  
		    public void keyReleased(KeyEvent e) {  
		    }  
		  
		    @Override  
		    public void keyPressed(KeyEvent e) {  
		    }  
		});  
	}
	
	private static void log(String msg){
		fileOutput("D:\\log.txt",msg);
	}

	public static void main(String[] args) {
		try {
			FileInputOutput fileInputOutput = new FileInputOutput();
			fileInputOutput.dialog();
		} catch (Exception e) {
			e.printStackTrace();
			log("main exception:"+ExceptionUtil.getExceptionMsg(e));
		}
//		fileInputOutput.analyse("C:\\Users\\Kaiguang\\Documents\\DeMAUD30.htm", "C:\\Users\\Kaiguang\\Documents\\out");
	}
}
