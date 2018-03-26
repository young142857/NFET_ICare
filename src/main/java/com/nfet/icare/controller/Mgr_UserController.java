package com.nfet.icare.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.Mgr_Company;
import com.nfet.icare.pojo.Mgr_Device;
import com.nfet.icare.pojo.Mgr_Fix;
import com.nfet.icare.pojo.Mgr_User;
import com.nfet.icare.pojo.Mgr_Warranty;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.FixServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.service.WarrantyServiceImpl;
import com.nfet.icare.util.GlobalMethods;

/**客户资料管理
 * 1、客户统计 
 * 2、客户资料
 * 3、客户注册设备
 * 4、设备报修订单
 * 5、设备延保订单
 * 6、客户公司资料
 * 7、退出系统
 * 
 */
@Controller
@RequestMapping("/mgr")
public class Mgr_UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserServiceImpl userServiceImpl;	
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	@Autowired
	private FixServiceImpl fixServiceImpl;
	@Autowired
	private WarrantyServiceImpl warrantyServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	
	//用户统计 
	@RequestMapping("/userList")
	@ResponseBody
	public Map<String, Object> userList() {
		Map<String, Object> userMap = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int [] countArr = new int[7];	
		int [] sumArr = new int[7];		
		List<Mgr_User> userList = new ArrayList<>();
		
		userList = userServiceImpl.userList();
		logger.info("get all info of users");
		for (Mgr_User mgr_User : userList) {
			//更改日期格式
			mgr_User.setRegTime(mgr_User.getCreateTime().toString().substring(0, 19));
			//更改公司地址
			if (mgr_User.getCompany().getProvince() != null && mgr_User.getCompany().getCity() != null && mgr_User.getCompany().getArea() != null) {
				mgr_User.getCompany().setAddress(mgr_User.getCompany().getProvince().getProvinceName()+" "+
						mgr_User.getCompany().getCity().getCityName()+" "+
						mgr_User.getCompany().getArea().getAreaName()+" "+mgr_User.getCompany().getAddress());
			}
		}
		
		String[] weekStr = GlobalMethods.weekDays();
		int sum = userServiceImpl.sevenCounts(weekStr[0]);
		for (int i = 0; i < 7; i++) {
			for (Mgr_User user : userList) {
				//总用户
				Date date = new Date(user.getCreateTime().getTime());
				if (sdf.format(date).equals(weekStr[i])) {
					sum ++;
				}
				//新增用户
				if (user.getCreateTime().toString().indexOf(weekStr[i]) != -1) {
					countArr[i] ++;
				}
			}
			sumArr[i] = sum;
		}	
		
//		//将一周的时间格式从年月日改为月日
//		for (int i = 0; i < weekStr.length; i++) {
//			String string = weekStr[i].substring(5);
//			weekStr[i] = string;
//		}
		
		userMap.put("weekStr", weekStr);
		userMap.put("countArr", countArr);
		userMap.put("sumArr", sumArr);
		userMap.put("rows", userList);
		
		return userMap;
	}
	
	//客户资料
	@RequestMapping("/userData")
	@ResponseBody
	public List<Mgr_User> userData() throws ParseException{
		List<Mgr_User> userData = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		
		userData = userServiceImpl.userList();
		logger.info("get all info of users");
		int num = 1;
		for (Mgr_User mgr_User : userData) {
			//加序号
			mgr_User.setNum(num);
			num ++;
			//注册时间显示格式
			mgr_User.setRegTime(sdf2.format(sdf1.parse(mgr_User.getCreateTime().toString().substring(0, 19))));			
			//更改公司地址
			if (mgr_User.getCompany().getProvince() != null && mgr_User.getCompany().getCity() != null && mgr_User.getCompany().getArea() != null) {
				mgr_User.getCompany().setAddress(mgr_User.getCompany().getProvince().getProvinceName()+" "+
						mgr_User.getCompany().getCity().getCityName()+" "+
						mgr_User.getCompany().getArea().getAreaName()+" "+mgr_User.getCompany().getAddress());
			}
		}
		
		return userData;
	}
	
	//客户注册设备
	@RequestMapping("/userDevices")
	@ResponseBody
	public List<Mgr_Device> userDevices(@RequestParam(value="userNo",required=true) String userNo) throws ParseException{
		List<Mgr_Device> deviceData = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
			
		int num = 1;
		//String userNo = "wxvip201708190002";
		deviceData = deviceServiceImpl.deviceData(userNo);
		logger.info("get the devices of " + userNo);
		for (Mgr_Device mgr_Device : deviceData) {
			//加序号
			mgr_Device.setNum(num);
			num ++;
			//更改保修状态显示格式
			if (mgr_Device.getWarrantStatus() == 1) {
				mgr_Device.setStatusStr("保内");
			}
			else {
				mgr_Device.setStatusStr("保外");
			}
			//绑定时间格式修改
			mgr_Device.setBindTimeStr(sdf4.format(sdf3.parse(mgr_Device.getBindTime().toString().substring(0,19))));
			//更改日期显示格式
			mgr_Device.setWarrantFrom(sdf2.format(sdf1.parse(mgr_Device.getWarrantFrom())));
			mgr_Device.setWarrantTo(sdf2.format(sdf1.parse(mgr_Device.getWarrantTo())));			
			//更改出厂时间显示格式
			DeviceInfo deviceInfo = mgr_Device.getDeviceNo();
			deviceInfo.setMfgDate(sdf2.format(sdf1.parse(mgr_Device.getDeviceNo().getMfgDate())));
			mgr_Device.setDeviceNo(deviceInfo);
			//拼接保修时长
			mgr_Device.setWarrantDate(mgr_Device.getWarrantFrom()+"-"+mgr_Device.getWarrantTo());
		}
		
		return deviceData;
	}
	
	//设备报修订单
	@RequestMapping("/deviceFixes")
	@ResponseBody
	public List<Mgr_Fix> deviceFixes(@RequestParam(value="deviceNo",required=true) String deviceNo) throws ParseException {
		List<Mgr_Fix> deviceFixes = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		
		int num = 1;
		deviceFixes = fixServiceImpl.deviceFixes(deviceNo);
		for (Mgr_Fix mgr_Fix : deviceFixes) {
			//加序号
			mgr_Fix.setNum(num);
			num ++;
			//维修方式
			if (mgr_Fix.getFixPattern() == 0) {
				mgr_Fix.setFixPatternStr("上门");
			}
			else {
				mgr_Fix.setFixPatternStr("送修");
			}
			//维修进度
			if (mgr_Fix.getFixSchedule() == 1) {
				mgr_Fix.setFixScheduleStr("报修成功");
			}
			else if (mgr_Fix.getFixSchedule() == 2) {
				mgr_Fix.setFixScheduleStr("客服确认");
			}
			else if (mgr_Fix.getFixSchedule() == 3) {
				mgr_Fix.setFixScheduleStr("维修中");
			}
			else if (mgr_Fix.getFixSchedule() == 4) {
				mgr_Fix.setFixScheduleStr("维修完成");
			}
			//修改下单时间显示格式
			mgr_Fix.setOrderTimeStr(sdf2.format(sdf1.parse(mgr_Fix.getOrderTime().toString().substring(0,19))));
			//修改维修地址显示内容
			if (mgr_Fix.getArea() != null) {
				mgr_Fix.setAddress(mgr_Fix.getProvince().getProvinceName()+" "
						+mgr_Fix.getCity().getCityName()+" "
						+mgr_Fix.getArea().getAreaName()+" "
						+mgr_Fix.getAddress());
			}
			else {				
				mgr_Fix.setAddress(mgr_Fix.getProvince().getProvinceName()+" "
						+mgr_Fix.getCity().getCityName()+" "
						+mgr_Fix.getAddress());
			}
		}
		
		return deviceFixes;
	}
	
	//设备延保订单
	@RequestMapping("/deviceWarranties")
	@ResponseBody
	public List<Mgr_Warranty> deviceWarranties(@RequestParam(value="deviceNo",required=true) String deviceNo) throws ParseException{
		List<Mgr_Warranty> deviceWarranties = new ArrayList<>();
		List<Mgr_Warranty> warrantyOrders = new ArrayList<>();
		List<Mgr_Warranty> visitOrders = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		
		int num = 1;
		warrantyOrders = warrantyServiceImpl.mgrWarrantyRecord(deviceNo);
		logger.info("get the warrantyOrder of " + deviceNo);
		visitOrders = warrantyServiceImpl.mgrVisitRecord(deviceNo);
		logger.info("get the visitOrder of " + deviceNo);
		for (Mgr_Warranty mgr_Warranty : visitOrders) {
			mgr_Warranty.setWarrantyFrom(mgr_Warranty.getVisitFrom());
			mgr_Warranty.setWarrantyTo(mgr_Warranty.getVisitTo());
		}
		//将两种订单合并
		deviceWarranties.addAll(warrantyOrders);
		deviceWarranties.addAll(visitOrders);
		for (Mgr_Warranty mgr_Warranty : deviceWarranties) {
			//加序号
			mgr_Warranty.setNum(num);
			num ++;
			//更改下单时间显示格式
			mgr_Warranty.setOrderTimeStr(sdf4.format(sdf3.parse(mgr_Warranty.getOrderTime().toString().substring(0,19))));
			//延保期显示方式
			mgr_Warranty.setWarrantyFrom(sdf2.format(sdf1.parse(mgr_Warranty.getWarrantyFrom())));
			mgr_Warranty.setWarrantyTo(sdf2.format(sdf1.parse(mgr_Warranty.getWarrantyTo())));
			mgr_Warranty.setWarrantyDate(mgr_Warranty.getWarrantyFrom()+"-"+mgr_Warranty.getWarrantyTo());
			//延保期限(月)
			if (mgr_Warranty.getWarrantyPkgNo().getWarrantyPkgPeriod() == 90) {
				mgr_Warranty.setWarrantyPeriod("3");
			}			
			else {
				mgr_Warranty.setWarrantyPeriod(mgr_Warranty.getWarrantyPkgNo().getWarrantyPkgPeriod()*12+"");
			}
		}
		
		return deviceWarranties;
	}
	
	//客户公司资料
	@RequestMapping("/companyList")
	@ResponseBody
	public List<Mgr_Company> companyList(){
		List<Mgr_Company> companyList = new ArrayList<>();
		
		companyList = companyServiceImpl.companyList();
		logger.info("get all companies");
		//修改地址显示格式
		for (Mgr_Company mgr_Company : companyList) {
			String address = "";
			if (mgr_Company.getProvince() != null) {
				address += mgr_Company.getProvince().getProvinceName(); 
			}
			if (mgr_Company.getCity() != null) {
				address += mgr_Company.getCity().getCityName();
			}
			if (mgr_Company.getArea() != null) {
				address += mgr_Company.getArea().getAreaName();
			}
			if (mgr_Company.getAddress() != null) {
				address += mgr_Company.getAddress();
			}
			mgr_Company.setAddress(address);
		}
		
		return companyList;
	}
	
	//退出系统 
	@RequestMapping("/exitSys")
	public String exitSys(HttpServletRequest request){
		HttpSession session = request.getSession();

		//清除员工信息
		session.removeAttribute("staff");
		
		return "login";
	}
}
