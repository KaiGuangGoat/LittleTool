package littleTool.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public static String getExceptionMsg(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw,true);
		e.printStackTrace(pw);
		sw.flush();
		pw.flush();
		return sw.toString();
	}
}
