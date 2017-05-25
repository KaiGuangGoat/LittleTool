package littleTool.UI;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import littleTool.bean.ResultProfitStop;

public class ProfitStopDataTable {
	
	private static JTable table = new JTable();
	
	public static JScrollPane showTable(List<ResultProfitStop> dataList){
		
		table.setModel(new DataTableModel(dataList));
		JScrollPane scroll = new JScrollPane(table);
		return scroll;
//		final JFrame win = new JFrame();
//		JPanel panel = new JPanel();
//		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
//		panel.setLayout(layout);
//		panel.add(scroll);
//		JButton sure = new JButton("确定");
//		sure.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				win.removeAll();
//				win.setVisible(false);
//			}
//		});
//		panel.add(sure);
		
//		win.setTitle("止损输出表"); // 窗体的标题
//		win.setDefaultCloseOperation(JFrame.NORMAL);
//		win.setContentPane(panel);
//		
//		win.setSize(new Dimension(1500, 400));
//		win.setVisible(true);;
	}
	
	
}
