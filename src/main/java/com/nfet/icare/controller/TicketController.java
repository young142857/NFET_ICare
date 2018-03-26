package com.nfet.icare.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.WarrantyServiceImpl;
import com.nfet.icare.util.GlobalConstants;
import com.nfet.icare.util.GlobalMethods;

import net.sf.json.JSONObject;

/** 
 * 我的优惠券
 * 1、延保券列表
 * 2、领取延保券
 * 3、使用延保券
 * 4、延保券详情
 * 
 */

@Controller
public class TicketController {	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	@Autowired
	private WarrantyServiceImpl warrantyServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${xxtUrl}")
	private String xxtUrl;
	@Value("${phoneUrl}")
	private String phoneUrl;
	
	//延保券列表
	@RequestMapping("/ticketList")
	@ResponseBody
	public ModelAndView ticketList(HttpServletRequest request){
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("mycoupon");
		User user = (User) session.getAttribute("user");
		//所有延保券
		List<Ticket> ticketList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		//已使用
		List<Ticket> ticketList1 = new ArrayList<>();
		//未使用
		List<Ticket> ticketList2 = new ArrayList<>();
		
		map.put("userNo", user.getUserNo());	
		map.put("type", 1);
		ticketList = ticketServiceImpl.ticketList(map);
		for (Ticket ticket : ticketList) {
			if (ticket.getStatus() == 1) {
				//已使用
				ticketList1.add(ticket);
				logger.info("get the used list of ticket");
			}
			else {
				ticketList2.add(ticket);
				logger.info("get the never use list of ticket");
			}
		}		
		mav.addObject("ticketList1", ticketList1);
		mav.addObject("ticketList2", ticketList2);
		mav.addObject("ticketSize", ticketList.size());
		
		return mav;
	}
	
	//领取延保券
	@RequestMapping("/getTicket")
	@ResponseBody
	public String getTicket(@RequestParam(value="count",required=true) String count,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("status", 1);
		User user = (User) session.getAttribute("user");
		
		logger.info("get the ticket");
		
		return GlobalConstants.ERROR_CODE_SUCCESS;
	}
	
	//使用延保券
	@RequestMapping("/useTicket")
	@ResponseBody
	public String useTicket(@RequestParam(value="deviceNo",required=true) String deviceNo,
			HttpServletRequest request) throws ParseException, IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = request.getSession();
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> payMap = new HashMap<>();
		Warranty warranty = new Warranty();
		WarrantyPkg pkg = new WarrantyPkg();
		pkg.setWarrantyPkgNo("25");
		String warrantyFrom = "";
		User user = (User) session.getAttribute("user");
		Device device = deviceServiceImpl.bindOrNot(deviceNo);
		//如果已过保、从点击领取之日开始算延保开始
		if (sdf.parse(sdf.format(new Date())).getTime() > sdf.parse(device.getWarrantTo()).getTime()) {
			warrantyFrom = sdf.format(new Date());
			calendar.setTime(new Date());
		}
		else {	
			warrantyFrom = device.getWarrantTo();
			calendar.setTime(sdf.parse(device.getWarrantTo()));
		}
		//延保三个月
		calendar.add(Calendar.MONTH, 3);
		String warrantyTo = sdf.format(calendar.getTime());
		Timestamp lastModifyTime = new Timestamp(System.currentTimeMillis());
		map.put("giveWarrant", 1);
		map.put("warrantyTo", warrantyTo);
		map.put("deviceNo", deviceNo);
		map.put("period", 90);
		map.put("lastModifyTime", lastModifyTime);
		//一个设备只能免费延保三个月一次
		if (ticketServiceImpl.queryTicket(deviceNo).getStatus()==0) {
			//设备信息更新
			deviceServiceImpl.useTicket(map);
			logger.info("update the info of device");
			//延保券信息更新
			ticketServiceImpl.useTicket(map);
			logger.info("update the info of ticket");
			int ticketCount = (int) session.getAttribute("ticketCount");
			ticketCount -= 1;
			session.setAttribute("ticketCount", ticketCount);
			//信讯通获取延保订单号
			payMap.put("user_no", user.getUserNo());
			payMap.put("device_no", deviceNo);
			payMap.put("warranty_pkg_no", "25");
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
			//延保记录信息更新		
			warranty.setWarrantyNo(warranty_ext_no);
			warranty.setUserNo(user);
			warranty.setPayStatus(0);
			warranty.setWarrantyFrom(warrantyFrom);
			warranty.setWarrantyTo(warrantyTo);
			warranty.setVisitTo("");
			warranty.setOrderTime(new Timestamp(System.currentTimeMillis()));
			warranty.setCreateTime(new Timestamp(System.currentTimeMillis()));
			warranty.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			warranty.setDeviceNo(device.getDeviceNo());
			warranty.setWarrantyPkgNo(pkg);
			warranty.setWarrantyVisitNo(new WarrantyPkg());
			warranty.setPayAmt("0");
			warrantyServiceImpl.insertWarranty(warranty);		
			logger.info("update the info of warranty");
			return GlobalConstants.ERROR_CODE_SUCCESS;
		}
		else {
			return GlobalConstants.ERROR_CODE_FAIL;
		}
		
	}
	
	//延保券详情
	@RequestMapping("/ticketDetail")
	@ResponseBody
	public ModelAndView ticketDetail(@RequestParam(value="deviceNo",required=true) String deviceNo){
		ModelAndView mav = new ModelAndView("mycoupondetailpage");
		Ticket ticket = ticketServiceImpl.queryTicket(deviceNo);
		mav.addObject("ticket", ticket);
		
		return mav;
	}
}
