package com.nfet.icare;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.nfet.icare.util.GlobalMethods;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
/*		String url = "http://192.168.123.101/3s/call/VIPRegister/%s";
		Map<String, Object> map = new HashMap<>();
		map.put("user_name", "张三");
		map.put("phone", "17512581995");
		map.put("company", "富通电科");
		String json = GlobalMethods.parseString(map);
		
		
		System.out.println(String.format(url, json));	*/
		
		String url = "http://192.168.123.101/3s/call/VIPRegister/";
		String tail = "{\"phone\":\"17512581995\",\"user_name\":\"张三\",\"company\":\"富通电科\"}";
		System.out.println(URLEncoder.encode(tail,"GBK"));
	}

}
