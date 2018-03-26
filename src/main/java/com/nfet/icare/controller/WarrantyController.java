package com.nfet.icare.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import com.nfet.icare.pojo.DeviceColumn;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyPkg;
import com.nfet.icare.pojo.WechatInfo;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.OrderServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.service.WarrantyServiceImpl;
import com.nfet.icare.util.GlobalConstants;
import com.nfet.icare.util.GlobalMethods;

import net.sf.json.JSONObject;

/**
 * 设备延保
 * 1、判断设备
 * 2、延保套餐选择
 * 3、点击支付
 * 4、付款成功
 * 
 * */

@Controller
public class WarrantyController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;	
	@Autowired
	private WarrantyServiceImpl warrantyServiceImpl;	
	@Autowired
	private UserServiceImpl userServiceImpl;	
	@Autowired
	private OrderServiceImpl orderServiceImpl;	
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	WechatInfo wechatInfo;
	@Value("${xxtUrl}")
	private String xxtUrl;
	@Value("${phoneUrl}")
	private String phoneUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	//判断设备是否存在
	@RequestMapping("/checkExist")
	@ResponseBody
	public Map<String, String> checkExist(@RequestParam(value="deviceNo",required=true) String deviceNo,
			HttpServletRequest request){
		Map<String, String> map = new HashMap<>();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		//对接收到的设备编码进行处理
		int index = deviceNo.toUpperCase().indexOf("VN");
		if (index != -1) {
			deviceNo = deviceNo.substring(index);			
		}
		
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		if (deviceInfo == null) { //设备不存在
			map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			map.put("desc", "该设备未入库，如有疑问请联系客服：400-8020-048");
		}
		else { //设备存在
			Device device = deviceServiceImpl.bindOrNot(deviceNo);
			//设备已绑定
			if (device != null) {
				if (user.getUserNo().equals(device.getUserNo())) { //是延保人的设备
					map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
				}
				else { //不是延保人的设备
					map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
					map.put("desc", "该设备已被其他用户注册，您无法延保！如有问题请联系延保客服：800-8289-488");
				}
			}
			else { //设备未绑定
				map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
			}
		}
		
		return map;
	}
	
	//延保套餐选择
	@RequestMapping("/warrantyList")
	@ResponseBody
	public ModelAndView warrantyList(@RequestParam(value="deviceNo",required=true) String deviceNo,
			HttpServletRequest request) throws ParseException{
		ModelAndView mav = new ModelAndView("choosewarranty");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		HttpSession session = request.getSession();
		Map<String, Object> deviceMap = new HashMap<>();
		User user = (User) session.getAttribute("user");
		Device device = new Device();
		Device device1 = new Device();
		Ticket ticket = new Ticket();
		Calendar c = Calendar.getInstance();
		
		//对接收到的设备编码进行处理
		int index = deviceNo.toUpperCase().indexOf("VN");
		if (index != -1) {
			deviceNo = deviceNo.substring(index);			
		}
		
		device = deviceServiceImpl.bindOrNot(deviceNo);		
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		DeviceColumn deviceColumn = deviceServiceImpl.deviceColumn(deviceInfo.getType());
		//判断是否绑定过
		if (device == null) {//未绑定			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(deviceInfo.getMfgDate()));
							
			//保修时间从出厂两个月后开始
			calendar.add(Calendar.MONTH, 2);
			
			device1.setUserNo(user.getUserNo());
			device1.setDeviceNo(deviceInfo);
			device1.setBindTime(new Timestamp(System.currentTimeMillis()));
			device1.setEcardNo("NFET"+sdf2.format(new Date())+RandomStringUtils.randomNumeric(6));
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
			ticketServiceImpl.insertTicket(ticket);
			int ticketCount = (int) session.getAttribute("ticketCount");
			ticketCount += 1;
			session.setAttribute("ticketCount", ticketCount);
			
			deviceServiceImpl.bindSingleDevice(device1);
			logger.info("insert single data into data_device");
			device = device1;
		}
		device.setWarrantFrom(sdf1.format(sdf.parse(device.getWarrantFrom())));
		device.setWarrantTo(sdf1.format(sdf.parse(device.getWarrantTo())));
		String warrantTo = device.getWarrantTo();
		String visitTo = "";
		if (device.getVisitTo()!=null) {
			device.setVisitTo(sdf1.format(sdf.parse(device.getVisitTo())));
			visitTo = device.getVisitTo();			
		}
		//延保套餐列表
		//List<WarrantyPkg> warrantyPkgList = warrantyServiceImpl.warrantyPkgList();
		//logger.info("get the list of warranty_pkg");
		//最新延保套餐列表
		List<WarrantyPkg> warrantyPkgList = warrantyServiceImpl.newWarrantyList(deviceColumn.getWarrantyType());
		logger.info("get the detailInfo of " + deviceInfo.getType());
		//上门服务卡列表
		List<WarrantyPkg> warrantyVisitList = warrantyServiceImpl.warrantyVisitList();
		logger.info("get the list of warranty_visit");
		for (WarrantyPkg warrantyPkg : warrantyPkgList) {
			c.setTime(sdf1.parse(warrantTo));
			c.add(Calendar.YEAR, warrantyPkg.getWarrantyPkgPeriod());
			warrantyPkg.setWarrantTo(sdf1.format(c.getTime()));
		}
		for (WarrantyPkg warrantyPkg : warrantyVisitList) {
			if (visitTo != null && !"".equals(visitTo)) {
				c.setTime(sdf1.parse(visitTo));
				c.add(Calendar.DATE, 1);
			}
			else {				
				c.setTime (new Date());
			}
			//获取上门截止时间
			c.add(Calendar.YEAR, warrantyPkg.getWarrantyPkgPeriod()-1);
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			warrantyPkg.setVisitTo(sdf1.format(c.getTime()));
			if (warrantyPkg.getWarrantyType() == 3) {
				//获取延保截止时间
				c.setTime(sdf1.parse(warrantTo));
				c.add(Calendar.YEAR, warrantyPkg.getWarrantyPkgPeriod());
				warrantyPkg.setWarrantTo(sdf1.format(c.getTime()));
			}			
		}
		
		mav.addObject("device", device);
		mav.addObject("warrantyPkgList", warrantyPkgList);
		mav.addObject("warrantyVisitList", warrantyVisitList);
		
		return mav;
	}
	
	//选择套餐后点击支付
	@RequestMapping("/clickPay")
	@ResponseBody
	public ModelAndView clickPay(@RequestParam(value="pkgType",required=false) String pkgType,
			@RequestParam(value="visitType",required=false) String visitType,
			@RequestParam(value="deviceNo",required=true) String deviceNo,
			@RequestParam(value="payAmt",required=true) String payAmt,
			HttpServletRequest request) throws ParseException{
		ModelAndView mav = new ModelAndView("establishdorder");
		Map<String, String> map = new HashMap<>();
		HttpSession session = request.getSession();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Warranty warranty = new Warranty();
		User user = new User();
		Device device = new Device();
		DeviceInfo deviceInfo = new DeviceInfo();
		WarrantyPkg warrantyPkg = new WarrantyPkg();
		WarrantyPkg warrantyVisit = new WarrantyPkg();
		
		device = deviceServiceImpl.bindOrNot(deviceNo);
		deviceInfo.setDeviceNo(deviceNo);
		user = (User) session.getAttribute("user");
		WarrantyPkg pkg = new WarrantyPkg();
		WarrantyPkg visit = new WarrantyPkg();
		String warrantyTo = "";
		String visitTo = "";
		String pkgNo = "";
		String visitNo = "";
		//先取出设备未选择套餐已有的延保截止和上门截止
		if (device.getWarrantTo() != null) {
			warrantyTo = device.getWarrantTo();
		}
		if (device.getVisitTo() != null) {
			visitTo = device.getVisitTo();
		}
		
		//根据选择的套餐获取延保截止时间
		if (!"".equals(pkgType) && pkgType != null) {
			pkg = warrantyServiceImpl.getWarrantyNo(pkgType);
			pkgNo = pkg.getWarrantyPkgNo();
			logger.info("get the num according to pkgType");
			c.setTime(sdf.parse(warrantyTo));
			c.add(Calendar.YEAR, pkg.getWarrantyPkgPeriod());
			warrantyTo = sdf.format(c.getTime());
		}
		//根据选择的套餐获取上门截止时间
		if (!"".equals(visitType) && visitType != null) {
			visit = warrantyServiceImpl.getWarrantyNo(visitType);
			visitNo = visit.getWarrantyPkgNo();
			logger.info("get the num according to visitType");
			if (device.getVisitTo() != null) {
				c.setTime(sdf.parse(device.getVisitTo()));
				c.add(Calendar.DATE, 1);
			}else {
				c.setTime(new Date());
			}
			c.add(Calendar.YEAR, visit.getWarrantyPkgPeriod()-1);
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			visitTo = sdf.format(c.getTime());
			//如果选择了上门且延保、延保截止需要增加
			if (visit.getWarrantyType() == 3) {
				c.setTime(sdf.parse(warrantyTo));
				c.add(Calendar.YEAR, visit.getWarrantyPkgPeriod());
				warrantyTo = sdf.format(c.getTime());
			}
		}		
		warrantyPkg.setWarrantyPkgNo(pkgNo);
		warrantyVisit.setWarrantyPkgNo(visitNo);
		warranty.setUserNo(user);
		warranty.setDeviceNo(deviceInfo);
		warranty.setWarrantyPkgNo(warrantyPkg);
		warranty.setWarrantyVisitNo(warrantyVisit);
		warranty.setPayAmt(payAmt);
		int no = 100000+(int)(Math.random()*899999);
		warranty.setWarrantyNo("NFETF1081707170"+no+"(预)");
		//延保开始时间
		if (warrantyTo.equals(device.getWarrantTo())) {//未选择延保套餐
			warranty.setWarrantyFrom(device.getWarrantFrom());
		}
		else {	
			//如果已过保、延保开始时间从下单之日开始
			if (sdf.parse(sdf.format(new Date())).getTime() > sdf.parse(device.getWarrantTo()).getTime()) {
				warranty.setWarrantyFrom(sdf.format(new Date()));
			}
			else {				
				warranty.setWarrantyFrom(device.getWarrantTo());
			}
		}
		//上门开始时间
		if (visitTo.equals(device.getVisitTo()) || "".equals(visitTo)) {//未选择上门套餐
			warranty.setVisitFrom(device.getVisitTo());
		}
		else {	
			//如果已过保、上门开始时间从下单之日开始
			if (device.getVisitTo() == null) {
				c.setTime(new Date());
				c.add(Calendar.DATE, 1);
				warranty.setVisitFrom(sdf.format(c.getTime()));
			}
			else if (sdf.parse(sdf.format(new Date())).getTime() > sdf.parse(device.getVisitTo()).getTime()) {
				c.setTime(new Date());
				c.add(Calendar.DATE, 1);
				warranty.setVisitFrom(sdf.format(c.getTime()));
			}
			else {	
				c.setTime(sdf.parse(device.getVisitTo()));
				c.add(Calendar.DATE, 1);
				warranty.setVisitFrom(sdf.format(c.getTime()));
			}
		}
		warranty.setPayStatus(1);
		warranty.setWarrantyTo(warrantyTo);
		warranty.setVisitTo(visitTo);
		warranty.setOrderTime(new Timestamp(System.currentTimeMillis()));
		warranty.setCreateTime(new Timestamp(System.currentTimeMillis()));
		warranty.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
		map.put("deviceNo", device.getDeviceNo().getDeviceNo());
		map.put("model", device.getDeviceNo().getType());
		map.put("pkgType", pkgType);
		map.put("visitType", visitType);
		map.put("pkgPrice", pkg.getWarrantyPkgPrice());
		map.put("visitPrice", visit.getWarrantyPkgPrice());
		map.put("payAmt", payAmt);
		map.put("warrantyNo", warranty.getWarrantyNo());
		map.put("image", device.getDeviceNo().getImage());
		mav.addObject("map", map);
		if (warranty.getUserNo() != null) {			
			warrantyServiceImpl.insertWarranty(warranty);
			logger.info("save the data into data_warranty_extend");
		}
		
		return mav;
	}
	
	//付款成功
	@RequestMapping("/paySuccess")
	@ResponseBody
	public String paySuccess(@RequestParam(value="warrantyNo",required=true) String warrantyNo,
			HttpServletRequest request
			) throws ParseException, IOException{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> warrantyMap = new HashMap<>();
		Map<String, Object> payMap = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Warranty warranty = new Warranty();
		//String warrantyNo = "NO.20170719";
		//获取到订单信息		
		warranty = warrantyServiceImpl.queryWarranty(warrantyNo);
		logger.info("get the info of " + warrantyNo);
		//设备编码
		String deviceNo = warranty.getDeviceNo().getDeviceNo();
		//保修开始时间
		String warrantyFrom = warranty.getWarrantyFrom();
		//保修截止时间
		String warrantyTo = warranty.getWarrantyTo();
		//上门截止时间
		String visitTo = warranty.getVisitTo();
		//保修状态
		int warrantyStatus = 1;
		if (sdf.parse(sdf.format(new Date())).getTime() > sdf.parse(warrantyTo).getTime() ) {
			warrantyStatus = 2;
		}
		Date warrantyFromDate = sdf.parse(warrantyFrom);
		Date warrantyToDate = sdf.parse(warrantyTo);
		//此次延保的时长
		int period = (int) ((warrantyToDate.getTime()-warrantyFromDate.getTime())/(1000*3600*24));
		//更新用户积分		
		int score = user.getScore();  //原积分		
		int increaseScore = Integer.parseInt(warranty.getPayAmt())/10;  //增长积分
		score += increaseScore;
		logger.info("new score is " + score);
		user.setScore(score);
		user.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
		userServiceImpl.updateUser(user);
		session.setAttribute("user", user);
		//信讯通保存
		payMap.put("user_no", user.getUserNo());
		payMap.put("device_no", deviceNo);
		if (orderServiceImpl.warrantyPkg(warrantyNo) != null) {
			payMap.put("warranty_pkg_no", orderServiceImpl.warrantyPkg(warrantyNo).getWarrantyPkgNo());			
		}
		else if (orderServiceImpl.warrantyVisit(warrantyNo) != null) {
			payMap.put("warranty_pkg_no", orderServiceImpl.warrantyVisit(warrantyNo).getWarrantyPkgNo());
		}
		payMap.put("pay_amt", warranty.getPayAmt());
		payMap.put("pay_msg", "is pay");
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		//根据设备型号获取设备的三级ID
		DeviceColumn deviceColumn = deviceServiceImpl.deviceColumn(deviceInfo.getType());
		logger.info("get the detailInfo of " + deviceInfo.getType());
		logger.info("deviceID : " + deviceColumn.getFirstId() + "-" + deviceColumn.getSecondId() + "-" + deviceColumn.getThirdId());
		
		payMap.put("ProductCode1_ID", deviceColumn.getFirstId());
		payMap.put("ProductCode2_ID", deviceColumn.getSecondId());
		payMap.put("ProductCode3_ID", deviceColumn.getThirdId());
			
		//获取用户手机的区号
		phoneUrl = String.format(phoneUrl, user.getPhone());
		Document doc = Jsoup.connect(phoneUrl).get();
		Elements els = doc.getElementsByClass("tdc2");
		payMap.put("YB_AreaCode", els.get(3).text());
		payMap.put("YB_BxCode", "0");
		String json = GlobalMethods.parseString(payMap);
		String result = restTemplate.getForObject(xxtUrl+"extendWarranty/{json}", String.class,json);
		JSONObject jsonObject = JSONObject.fromObject(result);	
		String warranty_ext_no = jsonObject.getString("warranty_ext_no");
		
		//将延保订单置为已支付
		warrantyMap.put("warrantyNo", warrantyNo);
		warrantyMap.put("warranty_ext_no", warranty_ext_no);
		warrantyMap.put("status", "0");
		warrantyMap.put("lastModifyTime", new Timestamp(System.currentTimeMillis()));
		warrantyServiceImpl.updatePayStatus(warrantyMap);	
		logger.info("update the status of " + warrantyNo);
		Timestamp lastModifyTime = new Timestamp(System.currentTimeMillis());
		//更新设备信息(此次延保时长，延保结束，上门截止)
		map.put("deviceNo", deviceNo);
		map.put("period", period);
		map.put("warrantyTo", warrantyTo);
		map.put("visitTo", visitTo);
		map.put("warrantyStatus", warrantyStatus);
		map.put("lastModifyTime", lastModifyTime);
		warrantyServiceImpl.updateDeviceInfo(map);
		logger.info("update the info of " + deviceNo);
		
		return GlobalConstants.ERROR_CODE_SUCCESS;
	}
}
