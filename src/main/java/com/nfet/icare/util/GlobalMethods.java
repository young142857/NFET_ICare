package com.nfet.icare.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class GlobalMethods {

	//信讯通传参数
	public static String parseString(Map<String, Object> map) {
		String str = "{";
		for (String key : map.keySet()) {
			str += "\""+key+"\""+":"+"\""+map.get(key)+"\",";
		}
		int index = str.lastIndexOf(",");
		str = str.substring(0, index);
		str += "}";
		return str;
	}
	
	//获取一周时间
	public static String[] weekDays(){
		String[] weekStr=new String[7];
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -7);
		for (int i = 0; i < 7; i++) {
			calendar.add(Calendar.DATE, 1);
			weekStr[i] = sdf.format(calendar.getTime());
		}

		return weekStr;
	}
}
