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
		"止损类型","经历的订单数", "正数", "负数","正数负数之和","盈利","最大正数","位置", "最小负数","位置"
		/*"单量止损",	"正数",	"负数",	"盈利",	
		"数值止损",	"正数",	"负数",	"盈利",	
		"综合止损",	"正数",	"负数",	"盈利" */};
	
	private static final String[] COLUMN_NAMES_UN = {
		"未满足类型","信号量的位置","信号量开始算起的正数个数","信号量开始算起的负数个数","求和","入场信号量开始算起的正数个数","入场信号量开始算起的负数个数","求和"
	};
	
	private static final String[] COLUMN_NAMES_PROFIT_MONTH = {
		"","1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"
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
			rowData.createCell(index++).setCellValue(type ==null?"汇总求和":type.getTypeStr(type));
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
		for(Entry<Integer, Integer[]> entry:data.entrySet()){
			HSSFRow rowData = sheet.createRow(++rowIndex);
			int columnIndex = 0;
			int year = entry.getKey();
			rowData.createCell(columnIndex++).setCellValue(year+"年");
			for(Integer profit:entry.getValue()){
				rowData.createCell(columnIndex++).setCellValue(profit==null?0:profit);
			}
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
