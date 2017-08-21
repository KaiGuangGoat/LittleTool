package littleTool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static void main(String[] args) throws ParseException {
		String time = "2010.2.28 07:30";
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		
		Date date = dataFormat.parse(time);
		Calendar calendar = Calendar.getInstance();
		System.out.println(date);
		StringBuilder sb = new StringBuilder();
		sb.append(getYear(date)).append("-").append(getMonth(date)).append("-").append(date.getDate());
		System.out.println(sb.toString());
		System.out.println(isMonthLastDay(date));
	}
	
	public static Date transFormDate(String source) {
		try {
			return transFormDate(source, "yyyy.MM.dd HH:mm");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date transFormDate(String source,String pattern) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(source);
	}
	
	public static boolean isSameYear(Date firstDate,Date secondDate){
		return firstDate.getYear() == secondDate.getYear();
	}
	
	public static boolean isSameMonth(Date firstDate,Date secondDate){
		return firstDate.getMonth() == secondDate.getMonth();
	}
	
	public static int getYear(Date date){
		return date.getYear() + 1900;
	}
	
	public static int getMonth(Date date){
		return date.getMonth()+1;
	}
	
	public static int getMonthLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime().getDate();
	}
	
	public static boolean isMonthLastDay(Date date){
		int lastDay = getMonthLastDay(date);
		return date.getDate() == lastDay;
	}
	
}
