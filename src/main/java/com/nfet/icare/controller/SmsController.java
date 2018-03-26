package com.nfet.icare.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nfet.icare.util.Oauth2Util;

import net.sf.json.JSONObject;

@RestController
public class SmsController {

	private static Logger logger = LoggerFactory.getLogger(Oauth2Util.class);

	@Value("${smsUrl}")
	private String smsUrl;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/sms/{phone}", method = RequestMethod.GET)
	public JSONObject jsdkHandler(HttpServletRequest request, @PathVariable String phone) {
		HttpSession session = request.getSession();
		String requestUrl = smsUrl + phone;
		/*
		 * System.out.println("requestUrl is:" + requestUrl); JSONObject
		 * jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		 */
		
		JSONObject jsonObject = restTemplate.getForObject(requestUrl, JSONObject.class);
		logger.info("get sms for " + phone + " is:" + jsonObject);
		String smsCode = jsonObject.getString("smsCode");
		session.setAttribute("smsCode", smsCode);
		return jsonObject;
	}

}
