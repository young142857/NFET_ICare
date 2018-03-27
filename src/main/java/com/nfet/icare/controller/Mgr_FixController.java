package com.nfet.icare.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nfet.icare.pojo.DeviceCount;
import com.nfet.icare.pojo.FaultCount;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Mgr_Fix;
import com.nfet.icare.pojo.Mgr_Warranty;
import com.nfet.icare.pojo.User;
import com.nfet.icare.service.FixServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.util.GlobalMethods;

/**
 * 报修统计
 * 1、报修统计
 * 2、客户报修订单
 */

//报修统计
@Controller
@RequestMapping("/mgr")
public class Mgr_FixController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FixServiceImpl fixServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	//报修统计
	@RequestMapping("/fixStatistics")
	@ResponseBody
	public Map<String, Object> fixStatistics() {
		Map<String, Object> fixMap = new HashMap<>();
		FaultCount faultCount = new FaultCount();
		List<Fix> fixList = new ArrayList<>();
		List<FaultCount> faultCounts = new ArrayList<>();
		List<FaultCount> newFaultCounts = new ArrayList<>();
		List<DeviceCount> deviceCounts = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int [] countArr = new int[7];	
		int [] sumArr = new int[7];	
		String[] nameStr=new String[5];
		String[] typeStr=new String[10];
		
		//折线统计
		fixList = fixServiceImpl.fixList();
		String[] weekStr = GlobalMethods.weekDays();
		int sum = fixServiceImpl.sevenCounts(weekStr[0]);
		for (int i = 0; i < 7; i++) {
			for (Fix fix : fixList) {
				//总报修
				Date date = new Date(fix.getOrderTime().getTime());
				if (sdf.format(date).equals(weekStr[i])) {
					sum ++;
				}
				//新增报修
				if (fix.getOrderTime().toString().indexOf(weekStr[i]) != -1) {
					countArr[i] ++;
				}
			}
			sumArr[i] = sum;
		}
		//扇形统计(故障类型)
		faultCounts = fixServiceImpl.faultCounts();
		if (faultCounts.size()<=4) {
			fixMap.put("faultCounts", faultCounts);
			//故障名称
			for (int i = 0; i < faultCounts.size(); i++) {
				nameStr[i] = faultCounts.get(i).getName();
			}
		}
		else {
			for (int i = 0; i < 4; i++) {
				newFaultCounts.add(faultCounts.get(i));				
			}			
			faultCount.setName("其他");
			faultCount.setValue(fixList.size()-faultCounts.get(0).getValue()-faultCounts.get(1).getValue()-faultCounts.get(2).getValue()-faultCounts.get(3).getValue());
			newFaultCounts.add(faultCount);
			//故障名称
			for (int i = 0; i < newFaultCounts.size(); i++) {
				nameStr[i] = newFaultCounts.get(i).getName();
			}
			fixMap.put("faultCounts", newFaultCounts);
		}
		
		//扇形统计(设备型号)
		deviceCounts = fixServiceImpl.deviceCounts();
		//型号名称
		for (int i = 0; i < deviceCounts.size(); i++) {
			typeStr[i] =  deviceCounts.get(i).getName();
		}
		
//		//将一周的时间格式从年月日改为月日
//		for (int i = 0; i < weekStr.length; i++) {
//			String string = weekStr[i].substring(5);
//			weekStr[i] = string;
//		}
		
		fixMap.put("weekStr", weekStr);
		fixMap.put("nameStr", nameStr);
		fixMap.put("typeStr", typeStr);
		fixMap.put("countArr", countArr);
		fixMap.put("sumArr", sumArr);
		fixMap.put("deviceCounts", deviceCounts);
		
		return fixMap;
	}
	
	//客户报修订单
	@RequestMapping("/allFixOrder")
	@ResponseBody
	public List<Mgr_Fix> allFixOrder() throws ParseException{
		List<Mgr_Fix> allFixOrder = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		
		int num = 1;
		allFixOrder = fixServiceImpl.allFixOrder();
		for (Mgr_Fix mgr_Fix : allFixOrder) {
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
			//获取客户姓名
			mgr_Fix.setUserName(userServiceImpl.checkUserPhone(mgr_Fix.getUserPhone()).getName());			
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
		
		return allFixOrder;
	}	
}
