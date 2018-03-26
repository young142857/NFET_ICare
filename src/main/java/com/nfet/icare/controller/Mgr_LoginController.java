package com.nfet.icare.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nfet.icare.pojo.Staff;
import com.nfet.icare.service.StaffServiceImpl;
import com.nfet.icare.util.GlobalConstants;

/**
 * 员工后台登录
 * 1、员工登录
 * 
 */

@Controller
@RequestMapping("/mgr")
public class Mgr_LoginController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StaffServiceImpl staffServiceImpl;
	
	//员工登录
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam(value="staffNo",required=true) String staffNo,
			@RequestParam(value="password",required=true) String password,
			HttpServletRequest request){
		Map<String, Object> loginMap = new HashMap<>();
		HttpSession session = request.getSession();
		
		Staff staff = staffServiceImpl.queryStaff(staffNo);
		logger.info("query staff of " + staffNo);
		if (staff == null) { //员工不存在
			logger.info("the staff is not exist");
			loginMap.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			loginMap.put("desc", "工号未入库！");
		}
		else if (staff.getPassword().equals(password)) { //密码一致
			logger.info("the password is correct");
			session.setAttribute("staff", staff);
			loginMap.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
			loginMap.put("roleId", staff.getRole().getRoleId());
		}
		else { //密码不一致
			logger.info("the password is uncorrect");
			loginMap.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			loginMap.put("desc", "密码不正确！");
		}
		
		return loginMap;
	}
}