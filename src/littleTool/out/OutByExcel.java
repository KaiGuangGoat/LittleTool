package littleTool.out;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import littleTool.bean.ResultByStop;
import littleTool.bean.ResultNoStop;
import littleTool.bean.ResultProfitStop;
import littleTool.bean.SignalStop.Type;
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
	
	private HSSFWorkbook workBook ;
	private HSSFSheet sheet ;
	
	
	public OutByExcel(){
		workBook = new HSSFWorkbook();
		sheet = workBook.createSheet();
	}
	
	@SuppressWarnings("deprecation")
	public void out(List<ResultProfitStop> dataList,String outName){
		if(dataList==null||dataList.size()==0){
			return;
		}
		HSSFRow row = sheet.createRow(0);
		for(int i=0;i<10;i++){
			HSSFCell cell = row.createCell((short)i);
			cell.setCellValue(COLUMN_NAMES[i]);
		}
		for(int i=0;i<dataList.size();i++){
			ResultProfitStop stop = dataList.get(i);
			Type type = stop.getStopType();
			HSSFRow rowData = sheet.createRow(i+1);
			int index = 0;
			rowData.createCell(index++).setCellValue(type ==null?"汇总求和":type.getTypeStr(type));
			rowData.createCell(index++).setCellValue(stop.getTotalCount()+"");
			rowData.createCell(index++).setCellValue(stop.getPositive()+"");
			rowData.createCell(index++).setCellValue(stop.getNegative()+"");
			rowData.createCell(index++).setCellValue(stop.getPositive()+stop.getNegative()+"");
			rowData.createCell(index++).setCellValue(stop.getProfit()+"");
			rowData.createCell(index++).setCellValue(stop.getMaxPositive());
			rowData.createCell(index++).setCellValue(stop.getPositionPst());
			rowData.createCell(index++).setCellValue(stop.getMinNegative());
			rowData.createCell(index++).setCellValue(stop.getPositionNgt());
		}
		write(outName);
	}
	
	private String write(String fileName){
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
