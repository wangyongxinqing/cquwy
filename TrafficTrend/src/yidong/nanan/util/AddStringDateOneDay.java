package yidong.nanan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddStringDateOneDay {
	public static String addOneDay(String orginaldate){
		String newDate="";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sdf.parse(orginaldate);  
			Calendar rightNow = Calendar.getInstance();  
			rightNow.setTime(dt);  
			rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加10天  
			newDate=sdf.format(rightNow.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return newDate;
	}
}
