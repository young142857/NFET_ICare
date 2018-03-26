package com.nfet.icare.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceData;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyPkg;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.WarrantyServiceImpl;
import com.nfet.icare.util.GlobalConstants;
import com.nfet.icare.util.GlobalMethods;
import net.sf.json.JSONObject;

/**
 * 我的设备、保修卡
 * 1、设备列表
 * 2、设备详情
 * 3、单个设备注册
 * 4、多个设备注册
 * 5、保修卡列表
 * 6、保修卡详情
 * 
 */

@Controller
public class DeviceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	@Autowired
	private WarrantyServiceImpl warrantyServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Value("${xxtUrl}")
	private String xxtUrl;
	
	//获取用户设备列表
	@RequestMapping("/deviceList")
	@ResponseBody
	public ModelAndView deviceList(@RequestParam(value="type",required=true) String type,
			HttpServletRequest request) {	
		System.out.println("type is " + type);
		ModelAndView mav = new ModelAndView("myequipment");
		List<Device> deviceList = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		HttpSession session = request.getSession();
		User user = new User();		
		user = (User) session.getAttribute("user");
		logger.info("get the info of user from session");
		map.put("userNo", user.getUserNo());
		//根据产品类型排序
		map.put("order", "model");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			device.setBindTimeStr(device.getBindTime().toString().substring(0,10));
		}
		mav.addObject("deviceList1",deviceList);
		//根据机器编码排序
		map.put("order", "data_device.device_no");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			device.setBindTimeStr(device.getBindTime().toString().substring(0,10));
		}
		mav.addObject("deviceList2",deviceList);
		//根据报修截止日期排序
		map.put("order", "warrant_to");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			device.setBindTimeStr(device.getBindTime().toString().substring(0,10));
		}
		mav.addObject("deviceList3",deviceList);
		if (deviceList.size() == 0) {
			mav.addObject("type", "0");
		}
		else {
			mav.addObject("type", type);
		}
			
		return mav;
	}

	//设备详情
	@RequestMapping("/deviceDetail")
	@ResponseBody
	public ModelAndView deviceDetail(@RequestParam(value="deviceNo") String deviceNo,
			@RequestParam(value="type",required=false) String type,
			HttpServletRequest request) throws ParseException{
		//根据设备编号查询设备信息
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("myequipmentdetail");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		User user = (User) session.getAttribute("user");
		logger.info("get the info of user from session");
		
		//对接收到的设备编码进行处理
		int index = deviceNo.toUpperCase().indexOf("VN");
		if (index != -1) {
			deviceNo = deviceNo.substring(index);			
		}
		
		Device device = deviceServiceImpl.bindOrNot(deviceNo);
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		String warrantStatus = "保内";
		
		String deviceType = deviceInfo.getType();
		String companyName = companyServiceImpl.getCompanyName(user.getCompanyId());
		if (sdf.parse(sdf.format(new Date())).getTime() > sdf.parse(device.getWarrantTo()).getTime()) {
			warrantStatus = "保外";
			device.setWarrantStatus(2);
			device.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			//将设备的保修状态改为保外
			deviceServiceImpl.updateDeviceStatus(device);
		}
		String warrantTo = device.getWarrantTo();
		String image = deviceInfo.getImage();
		int deviceStatus = device.getStatus();
		Ticket ticket = ticketServiceImpl.queryTicket(deviceNo);
		
		Map<String, Object> map = new HashMap<>();
		if ("1".equals(type)) {
			map.put("type", "1");
		}
		else {
			map.put("type", "0");
		}
		map.put("deviceNo", deviceNo);
		map.put("companyName", companyName);
		map.put("deviceType", deviceType);
		map.put("warrantStatus", warrantStatus);
		map.put("warrantTo", sdf1.format(sdf.parseObject(warrantTo)));
		map.put("image", image);
		map.put("status", ticket.getStatus());		
		map.put("deviceStatus", deviceStatus);		
		logger.info("get the detailInfo of " + deviceNo);
		mav.addObject("deviceMap", map);

		return mav;
	}
	
	//用户单个设备注册
	@RequestMapping("/bindSingleDevice")
	@ResponseBody
	public Map<String, String> bindSingleDevice(@RequestParam(value="deviceNo",required=true) String deviceNo,
			HttpServletRequest request) throws ParseException {
		List<Device> bindDeviceList = new ArrayList<>();
		HttpSession session = request.getSession();
		Map<String, String> map = new HashMap<>();
		Map<String, Object> deviceMap = new HashMap<>();		
		Device device = new Device();
		Device device1 = new Device();
		Ticket ticket = new Ticket();
		DeviceInfo deviceInfo = new DeviceInfo();
		User user = (User) session.getAttribute("user");
		logger.info("get the info of user from session");
		
		//对接收到的设备编码进行处理
		int index = deviceNo.toUpperCase().indexOf("VN");
		if (index != -1) {
			deviceNo = deviceNo.substring(index);			
		}
		logger.info("new deviceNo is " + deviceNo);
		
		deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		//判断设备是否存在
		if (deviceInfo == null) {//不存在
			logger.info("the device not exise");
			map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);			
			map.put("desc", "该设备未入库，如有疑问请联系客服：400-8020-048");
		}
		else {//存在
			logger.info("the device exise");
			device = deviceServiceImpl.bindOrNot(deviceNo);
			//判断设备是否已注册
			if (device != null) {//已注册
				logger.info("the device had been binded");
				map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
				if (user.getUserNo().equals(device.getUserNo())) {//是本人注册
					map.put("desc", "该设备您已注册！");					
				}
				else { //他人注册
					map.put("desc", "该设备已被其他用户注册，如有疑问请联系客服：400-8020-048");
				}
			}
			else {//未注册
				logger.info("the device could be binded");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(deviceInfo.getMfgDate()));
								
				//保修时间从出厂两个月后开始
				calendar.add(Calendar.MONTH, 2);
				
				device1.setUserNo(user.getUserNo());
				device1.setDeviceNo(deviceInfo);
				device1.setBindTime(new Timestamp(System.currentTimeMillis()));
				device1.setEcardNo("NFET"+sdf1.format(new Date())+RandomStringUtils.randomNumeric(6));
				device1.setWarrantPeriod(365*3);
				device1.setWarrantFrom(sdf.format(calendar.getTime()));
				//保修时间三年
				calendar.add(Calendar.YEAR, 3);
				device1.setWarrantTo(sdf.format(calendar.getTime()));
				if (sdf.parse(sdf.format(new Date())).getTime()>calendar.getTime().getTime()) {//保外
					device1.setWarrantStatus(2);
				}
				else {//保内
					device1.setWarrantStatus(1);
				}
				device1.setCreateTime(new Timestamp(System.currentTimeMillis()));
				device1.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
				bindDeviceList.add(device1);
				
				//信讯通接口(注册设备)
				deviceMap.put("user_no", user.getUserNo());
				deviceMap.put("device_no", deviceNo);
				String json = GlobalMethods.parseString(deviceMap);
				String result = restTemplate.getForObject(xxtUrl+"queryWarranty/{json}", String.class,json);
				logger.info("XXT:"+result);
				
				//赠送延保券
				ticket.setDeviceNo(deviceNo);
				ticket.setPeriod("3");
				ticket.setTicketNo("NFET"+RandomStringUtils.randomNumeric(8));
				ticket.setUserNo(user.getUserNo());
				ticket.setPhone(user.getPhone());
				ticket.setContent("延保有效期是指在延保券使用后，给所使用的设备在其延保期后延长X个月的保修时间；若已过保，则保修期即从使用当天开始算起，延长X个月保修时间。");
				ticket.setType(1);
				ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
				ticket.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
				if (ticketServiceImpl.queryTicket(deviceNo) == null) {			
					ticketServiceImpl.insertTicket(ticket);
				}
				session.setAttribute("status", 0);
				int ticketCount = (int) session.getAttribute("ticketCount");
				ticketCount += 1;
				session.setAttribute("ticketCount", ticketCount);
				//未注册方给注册
				if (deviceServiceImpl.bindOrNot(deviceNo) == null) {					
					deviceServiceImpl.bindSingleDevice(device1);
				}
				logger.info("insert single data into data_device");
				map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
				map.put("desc", "1个设备注册成功!");
			}
		}
		return map;
	}
	
	//用户多个设备注册
	@RequestMapping("/bindMoreDevice")
	@ResponseBody
	public Map<String, String> bindMoreDevice(@RequestParam(value="deviceNoStart") String deviceNoStart,
			@RequestParam(value="deviceNoEnd") String deviceNoEnd,
			HttpServletRequest request) throws ParseException{
		List<Device> bindDeviceList1 = new ArrayList<>();
		List<Device> bindDeviceList2 = new ArrayList<>();
		List<Device> bindDeviceList3 = new ArrayList<>();
		Map<String, Object> deviceMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();
		Device device = new Device();
		Ticket ticket = new Ticket();
		DeviceInfo deviceInfo = new DeviceInfo();
		HttpSession session = request.getSession();
		int count = 0;
		String desc = "";
		String startString = deviceNoStart.substring(0,deviceNoStart.length()-2);
		String endString = deviceNoEnd.substring(0,deviceNoEnd.length()-2);
		int startInt = Integer.parseInt(deviceNoStart.substring(deviceNoStart.length()-2));
		int endInt = Integer.parseInt(deviceNoEnd.substring(deviceNoEnd.length()-2));
		//判断是否按顺序输入100以内个设备
		if (startString.equals(endString) && startInt<=endInt) {
			logger.info("Reasonable input");
			for (int i = startInt; i <= endInt; i++) {
				String deviceNo = "";
				if (i < 10) {
					deviceNo = startString+"0"+i;
				}
				else {					
					deviceNo = startString+i;
				}
				deviceInfo = deviceServiceImpl.existDevice(deviceNo);
				//判断设备是否存在
				if (deviceInfo == null) {//不存在
					logger.info("the device:"+deviceNo+" not exise");
					map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
				}
				else {//存在
					logger.info("the device:"+deviceNo+" exise");
					device = deviceServiceImpl.bindOrNot(deviceNo);
					//判断设备是否已注册
					if (device != null) {//已注册
						logger.info("the device:"+deviceNo+" had been binded");
						map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
					}
					else {//未注册
						logger.info("the device:"+deviceNo+" could be binded");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(sdf.parse(deviceInfo.getMfgDate()));
						//保修时间从出厂两个月后开始
						calendar.add(Calendar.MONTH, 2);
						User user = (User) session.getAttribute("user");
						logger.info("get the info of user from session");
						Device device1 = new Device();
						device1.setUserNo(user.getUserNo());
						//device1.setUserNo("NFET.1719");
						device1.setDeviceNo(deviceInfo);
						device1.setBindTime(new Timestamp(System.currentTimeMillis()));
						device1.setBindTimeStr(device1.getBindTime().toString().substring(0, 10));
						device1.setEcardNo("NFET"+sdf1.format(new Date())+RandomStringUtils.randomNumeric(6));
						device1.setWarrantPeriod(365*3);
						device1.setWarrantFrom(sdf.format(calendar.getTime()));
						//保修时间三年
						calendar.add(Calendar.YEAR, 3);
						device1.setWarrantTo(sdf.format(calendar.getTime()));
						if (sdf.parse(sdf.format(new Date())).getTime()>calendar.getTime().getTime()) {//保外
							device1.setWarrantStatus(2);
						}
						else {//保内
							device1.setWarrantStatus(1);
						}
						device1.setCreateTime(new Timestamp(System.currentTimeMillis()));
						device1.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
						deviceServiceImpl.bindSingleDevice(device1);
						//赠送延保券
						ticket.setDeviceNo(deviceNo);
						ticket.setPeriod("3");
						ticket.setTicketNo("NFET"+RandomStringUtils.randomNumeric(8));
						ticket.setUserNo(user.getUserNo());
						ticket.setPhone(user.getPhone());
						ticket.setContent("延保有效期是指在延保券使用后，给所使用的设备在其延保期后延长X个月的保修时间；若已过保，则保修期即从使用当天开始算起，延长X个月保修时间。");
						ticket.setType(1);
						ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
						ticket.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
						ticketServiceImpl.insertTicket(ticket);
						int ticketCount = (int) session.getAttribute("ticketCount");
						ticketCount += 1;
						session.setAttribute("ticketCount", ticketCount);
						
						//信讯通接口(注册设备)
						deviceMap.put("user_no", user.getUserNo());
						deviceMap.put("device_no", deviceNo);
						String json = GlobalMethods.parseString(deviceMap);
						String result = restTemplate.getForObject(xxtUrl+"queryWarranty/{json}", String.class,json);
						logger.info("XXT:"+result);
						
						bindDeviceList1.add(device1);
						bindDeviceList2.add(device1);
						bindDeviceList3.add(device1);
						count ++;						
					}
				}				
			}	
			desc = count+"个设备注册成功!";
			map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
			map.put("desc", desc);
		}
		else {
			logger.info("not less than 99 devices or not entered in order");
			map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			desc = "请输入连续的设备编码(一次最多能注册99个设备)";
			map.put("desc",desc );
		}
		ComparatorChain chain1 = new ComparatorChain();
		ComparatorChain chain2 = new ComparatorChain();
		ComparatorChain chain3 = new ComparatorChain();
		//产品类型排序
		chain1.addComparator(new BeanComparator("deviceNo.type"),true);//true,fase正序反序		
		Collections.sort(bindDeviceList1,chain1);
		session.setAttribute("bindDeviceList1", bindDeviceList1);
		//设备编号排序
		chain2.addComparator(new BeanComparator("deviceNo.deviceNo"),true);//true,fase正序反序		
		Collections.sort(bindDeviceList2,chain2);
		session.setAttribute("bindDeviceList2", bindDeviceList2);
		//报修截止日期排序
		chain3.addComparator(new BeanComparator("warrantTo"),true);//true,fase正序反序		
		Collections.sort(bindDeviceList3,chain3);
		session.setAttribute("bindDeviceList3", bindDeviceList3);
		
		session.setAttribute("count", bindDeviceList1.size());
		session.setAttribute("status", 0);
		
		logger.info("insert more data into data_device");
		
		return map;
	}
	
	//用户保修卡列表
	@RequestMapping("/ecardList")
	@ResponseBody
	public ModelAndView ecardList(HttpServletRequest request) throws ParseException{
		ModelAndView mav = new ModelAndView("mywarrantycard");
		List<Device> deviceList = new ArrayList<>();
		List<Device> ecardList1 = new ArrayList<>();
		List<Device> ecardList2 = new ArrayList<>();
		List<Device> ecardList3 = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		HttpSession session = request.getSession();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		User user = new User();
		user = (User) session.getAttribute("user");
		logger.info("get the info of user from session");
		map.put("userNo", user.getUserNo());
		//根据保修卡号排序
		map.put("order", "ecard_no");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			Date nowDate = new Date();
			String warrantTo = device.getWarrantTo();
			Date date = sdf.parse(sdf.format(nowDate));
			Date date1 = sdf.parse(warrantTo);
			int leftDate = (int) ((date1.getTime()-date.getTime())/(24*60*60*1000));
			//如果剩余延保时间小于0则显示0
			if (leftDate < 0) {
				leftDate = 0;
			}
			device.setEcardNum(device.getEcardNo().substring(4));
			int warrantDate = device.getWarrantPeriod()/365;
			device.setWarrantDate(warrantDate);
			device.setLeftDate(leftDate);
			ecardList1.add(device);
		}
		mav.addObject("ecardList1",ecardList1);
		//根据机器编码排序
		map.put("order", "data_device.device_no");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			Date nowDate = new Date();
			String warrantTo = device.getWarrantTo();
			Date date = sdf.parse(sdf.format(nowDate));
			Date date1 = sdf.parse(warrantTo);
			int leftDate = (int) ((date1.getTime()-date.getTime())/(24*60*60*1000));
			//如果剩余延保时间小于0则显示0
			if (leftDate < 0) {
				leftDate = 0;
			}
			device.setEcardNum(device.getEcardNo().substring(4));
			int warrantDate = device.getWarrantPeriod()/365;
			device.setWarrantDate(warrantDate);
			device.setLeftDate(leftDate);
			ecardList2.add(device);
		}
		mav.addObject("ecardList2",ecardList2);
		//根据截止日期排序
		map.put("order", "warrant_to");
		deviceList = deviceServiceImpl.deviceList(map);
		for (Device device : deviceList) {
			Date nowDate = new Date();
			String warrantTo = device.getWarrantTo();
			Date date = sdf.parse(sdf.format(nowDate));
			Date date1 = sdf.parse(warrantTo);
			int leftDate = (int) ((date1.getTime()-date.getTime())/(24*60*60*1000));
			//如果剩余延保时间小于0则显示0
			if (leftDate < 0) {
				leftDate = 0;
			}
			device.setEcardNum(device.getEcardNo().substring(4));
			int warrantDate = device.getWarrantPeriod()/365;
			device.setWarrantDate(warrantDate);
			device.setLeftDate(leftDate);
			ecardList3.add(device);
		}
		mav.addObject("ecardList3",ecardList3);
		logger.info("query the devices of " + user.getName());
		mav.addObject("ecardSize", deviceList.size());
		
		return mav;
	}
	
	//保修卡详情
	@RequestMapping("/ecardDetail")
	@ResponseBody
	public ModelAndView ecardDetail(@RequestParam("deviceNo") String deviceNo,
			HttpServletRequest request) throws ParseException{
		ModelAndView mav = new ModelAndView("myWarrantyDetailpage");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> map = new HashMap<>();
		//获得公司设备信息
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		//获得用户设备信息
		Device device = deviceServiceImpl.bindOrNot(deviceNo);
		//设备名称
		String name = deviceInfo.getName();
		//设备型号
		String type = deviceInfo.getType();
		Date nowDate = new Date();
		//保修结束时间
		String warrantTo = device.getWarrantTo();
		Date date = sdf.parse(sdf.format(nowDate));
		Date date1 = sdf.parse(warrantTo);
		//剩余时间
		String leftDate = ((date1.getTime()-date.getTime())/(24*60*60*1000))+"";
		if (Integer.parseInt(leftDate) < 0) {
			leftDate = "0";
		}
		//保修卡号
		String ecardNo = device.getEcardNo();
		//保修开始时间
		String warrantFrom = device.getWarrantFrom();
		logger.info("get the detailInfo of " + deviceNo);
		map.put("nameType", name+type);
		map.put("leftDate", leftDate);
		map.put("ecardNo", ecardNo);
		map.put("ecardNum", ecardNo.substring(4));
		map.put("deviceNo", deviceNo);
		map.put("warrantFrom", sdf1.format(sdf.parse(warrantFrom)));
		map.put("warrantTo", sdf1.format(sdf.parse(warrantTo)));
		//延保记录
		List<Warranty> warrantyRecord = warrantyServiceImpl.warrantyRecord(deviceNo);		
		for (Warranty warranty : warrantyRecord) {
			WarrantyPkg warrantyPkg = warranty.getWarrantyPkgNo();
			warrantyPkg.setWarrantyPkgPeriod(warranty.getWarrantyPkgNo().getWarrantyPkgPeriod()*12);
			warranty.setWarrantyPkgNo(warrantyPkg);
			warranty.setWarrantyFrom(sdf1.format(sdf.parse(warranty.getWarrantyFrom())));
			warranty.setWarrantyTo(sdf1.format(sdf.parse(warranty.getWarrantyTo())));
			warranty.setOrderTimeStr(warranty.getOrderTime().toString().substring(0, 19));;
		}
		//上门卡记录
		List<Warranty> visitRecord = warrantyServiceImpl.visitRecord(deviceNo);
		for (Warranty warranty : visitRecord) {
			WarrantyPkg warrantyPkg = warranty.getWarrantyVisitNo();
			warrantyPkg.setWarrantyPkgPeriod(warranty.getWarrantyVisitNo().getWarrantyPkgPeriod()*12);
			warranty.setWarrantyVisitNo(warrantyPkg);
			warranty.setVisitFrom(sdf1.format(sdf.parse(warranty.getVisitFrom())));
			warranty.setVisitTo(sdf1.format(sdf.parse(warranty.getVisitTo())));
			warranty.setOrderTimeStr(warranty.getOrderTime().toString().substring(0, 19));;
		}
		mav.addObject("warrantyRecord",warrantyRecord);
		mav.addObject("visitRecord",visitRecord);
		mav.addObject("ecardMap", map);		
		
		return mav;
	}
}

