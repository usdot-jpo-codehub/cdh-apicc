package gov.dot.its.cdh.utils;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class CDHUtilsImpl implements CDHUtils {

	public void printMessage(boolean isError, String message) {

		String msg = "[%s] %s : %s";
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		System.out.println(String.format(msg, dateFormat.format(date), isError? "Error": "", message));
	}
}
