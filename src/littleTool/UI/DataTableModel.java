package littleTool.UI;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import littleTool.bean.ResultProfitStop;
import littleTool.bean.SignalStop.Type;

public class DataTableModel implements TableModel{
	
	private static final String[] COLUMN_NAMES = {
		"止损类型","经历的订单数", "正数", "负数","正数负数之和","盈利","最大正数","位置", "最小负数","位置",
		/*"单量止损",	"正数",	"负数",	"盈利",	
		"数值止损",	"正数",	"负数",	"盈利",	
		"综合止损",	"正数",	"负数",	"盈利" */};
	
	private List<ResultProfitStop> dataList;
	public DataTableModel(List<ResultProfitStop> dataList){
		this.dataList = dataList;
	}

	@Override
	public int getRowCount() {
		return dataList==null?0:dataList.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return COLUMN_NAMES[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		ResultProfitStop stop = dataList.get(rowIndex);
		String value = "";
		Type type = stop.getStopType();
		int i=0;
		switch (columnIndex) {
		case 0:
			value = type==null?"汇总求和":type.getTypeStr(type);
			break;
		case 1:
			value = stop.getTotalCount()+"";
			break;
		case 2:
			value = stop.getPositive()+"";
			break;
		case 3:
			value = stop.getNegative()+"";
			break;
		case 4 :
			value = (stop.getPositive()+stop.getNegative())+"";
			break;
		case 5:
			value = stop.getProfit()+"";
			break;
		case 6:
			value = stop.getMaxPositive()+"";
			break;
		case 7:
			value = stop.getPositionPst()+"";
			break;
		case 8:
			value = stop.getMinNegative()+"";
			break;
		case 9:
			value = stop.getPositionNgt()+"";
			break;

		default:
			
			break;
		}
		return value;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

}
