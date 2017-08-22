package littleTool.out;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import littleTool.bean.ResultProfitStop;
import littleTool.bean.SignalPositionUnEntrance;
import littleTool.bean.SignalStop.Type;
import littleTool.tool.ProfitByYearMonthHolder;
import littleTool.utils.ExceptionUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class OutByExcel {
	
	private static final String[] COLUMN_NAMES = {
		"ֹ������","�����Ķ�����", "����", "����","��������֮��","ӯ��","�������","λ��", "��С����","λ��"
		/*"����ֹ��",	"����",	"����",	"ӯ��",	
		"��ֵֹ��",	"����",	"����",	"ӯ��",	
		"�ۺ�ֹ��",	"����",	"����",	"ӯ��" */};
	
	private static final String[] COLUMN_NAMES_UN = {
		"δ��������","�ź�����λ��","�ź�����ʼ�������������","�ź�����ʼ����ĸ�������","���","�볡�ź�����ʼ�������������","�볡�ź�����ʼ����ĸ�������","���"
	};
	
	private static final String[] COLUMN_NAMES_PROFIT_MONTH = {
		"","1��","2��","3��","4��","5��","6��","7��","8��","9��","10��","11��","12��"
	};
	
	private HSSFWorkbook workBook ;
	private HSSFSheet sheet ;
	
	private HSSFWorkbook workBook2;
	private HSSFSheet sheet2;
	
	public OutByExcel(){
		workBook = new HSSFWorkbook();
		sheet = workBook.createSheet();
		
		workBook2 = new HSSFWorkbook();
		sheet2 = workBook2.createSheet();
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void out(List<ResultProfitStop> dataList,String outName){
		if(dataList==null||dataList.size()==0){
			return;
		}
		HSSFRow row = sheet.createRow(0);
		for(int i=0;i<10;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(COLUMN_NAMES[i]);
		}
		for(int i=0;i<dataList.size();i++){
			ResultProfitStop stop = dataList.get(i);
			Type type = stop.getStopType();
			HSSFRow rowData = sheet.createRow(i+1);
			int index = 0;
			rowData.createCell(index++).setCellValue(type ==null?"�������":type.getTypeStr(type));
			rowData.createCell(index++).setCellValue(stop.getTotalCount());
			rowData.createCell(index++).setCellValue(stop.getPositive());
			rowData.createCell(index++).setCellValue(stop.getNegative());
			rowData.createCell(index++).setCellValue(stop.getPositive()+stop.getNegative());
			rowData.createCell(index++).setCellValue(stop.getProfit());
			rowData.createCell(index++).setCellValue(stop.getMaxPositive());
			rowData.createCell(index++).setCellValue(stop.getPositionPst());
			rowData.createCell(index++).setCellValue(stop.getMinNegative());
			rowData.createCell(index++).setCellValue(stop.getPositionNgt());
		}
		write(outName,workBook);
	}
	
	public void outUnEntrance(List<SignalPositionUnEntrance> dataList,String outName){
		if(dataList==null||dataList.size()==0){
			return;
		}
		HSSFRow row = sheet2.createRow(0);
		for(int i=0;i<COLUMN_NAMES_UN.length;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(COLUMN_NAMES_UN[i]);
		}
		for(int i=0;i<dataList.size();i++){
			SignalPositionUnEntrance unEntrance = dataList.get(i);
			
			HSSFRow rowData = sheet2.createRow(i+1);
			int index = 0;
			rowData.createCell(index++).setCellValue(unEntrance.TYPE);
			rowData.createCell(index++).setCellValue(unEntrance.getSignalPositionMsg().getEnd());
			rowData.createCell(index++).setCellValue(unEntrance.getPositiveCount());
			rowData.createCell(index++).setCellValue(unEntrance.getNegativeCount());
			rowData.createCell(index++).setCellValue(unEntrance.getSumAfterUnEntrance());
			rowData.createCell(index++).setCellValue(unEntrance.getPositiveCountProfitStop());
			rowData.createCell(index++).setCellValue(unEntrance.getNegativeCountProfitStop());
			rowData.createCell(index++).setCellValue(unEntrance.getSumAfterUnProfitStop());
		}
		write(outName,workBook2);
	}
	
	public void outProfitByYearMonth(String outName){
		Map<Integer,Integer[]> data = ProfitByYearMonthHolder.INSTANCE.getProfitByYearMonths();
		ProfitByYearMonthHolder.INSTANCE.reset();
		if(data == null || data.size() == 0){
			return;
		}
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		int rowIndex = 0;
		HSSFRow row = sheet.createRow(rowIndex);
		for(int i=0;i<COLUMN_NAMES_PROFIT_MONTH.length;i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(COLUMN_NAMES_PROFIT_MONTH[i]);
		}
		Integer[] sumOfColumns = new Integer[12];
		for(Entry<Integer, Integer[]> entry:data.entrySet()){
			HSSFRow rowData = sheet.createRow(++rowIndex);
			int columnIndex = 0;
			int year = entry.getKey();
			rowData.createCell(columnIndex++).setCellValue(year+"��");
			for(Integer profit:entry.getValue()){
				if(profit != null){
					rowData.createCell(columnIndex).setCellValue(profit);
					if(sumOfColumns[columnIndex-1]==null){
						sumOfColumns[columnIndex-1] = 0;
					}
					sumOfColumns[columnIndex-1] += profit;
				}
				
				columnIndex++;
			}
		}
		int columnIndex = 0;
		HSSFRow rowData = sheet.createRow(++rowIndex);
		rowData.createCell(columnIndex++).setCellValue("����");
		for(Integer sumOfColumn:sumOfColumns){
			rowData.createCell(columnIndex++).setCellValue(sumOfColumn==null?0:sumOfColumn);
		}
		write(outName,workBook);
	}
	
	public void  outBaseData(List<String>timeList,List<Integer>dataList,String outName){
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		for(int i=0;i<dataList.size();i++){
			HSSFRow row = sheet.createRow(i);
			int columnIndex = 0;
			row.createCell(columnIndex++).setCellValue(timeList.get(i));
			row.createCell(columnIndex++).setCellValue(dataList.get(i));
		}
		write(outName,workBook);
	}
	
	private String write(String fileName,HSSFWorkbook workBook){
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			workBook.write(fos);
			fos.close();
			return "";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ExceptionUtil.getExceptionMsg(e);
		} catch (IOException e) {
			e.printStackTrace();
			return ExceptionUtil.getExceptionMsg(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFCell cell = row.createCell((short)0);
		cell.setCellValue("test1");
		cell = row.createCell((short)1);
		cell.setCellValue("test2");
		cell.setCellStyle(cellStyle);
		
		try {
			FileOutputStream fos = new FileOutputStream("H:/test.excel");
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
