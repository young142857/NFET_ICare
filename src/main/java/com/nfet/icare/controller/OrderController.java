package com.nfet.icare.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.User;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyPkg;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.EvaluateServiceImpl;
import com.nfet.icare.service.FixServiceImpl;
import com.nfet.icare.service.OrderServiceImpl;
import com.nfet.icare.util.GlobalMethods;

import net.sf.json.JSONObject;


/**
 * 我的订单
 * 1、订单列表
 * 2、报修详情
 * 3、延保详情
 * 
 */

@Controller
public class OrderController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;	
	@Autowired
	private EvaluateServiceImpl evaluateServiceImpl;	
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;	
	@Autowired
	private FixServiceImpl fixServiceImpl;	
	@Autowired
	private RestTemplate restTemplate;
	@Value("${xxtUrl}")
	private String xxtUrl;
	
	//我的订单
	@RequestMapping("/myOrder")
	@ResponseBody
	public ModelAndView myOrder(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("myorder");
		HttpSession session = request.getSession();
		List<Fix> fixList = new ArrayList<>();	
		//报修维修中订单
		List<Fix> fixList1 = new ArrayList<>();		
		//报修已完成未评价订单
		List<Fix> fixList2 = new ArrayList<>();		
		//报修已完成已评价订单
		List<Fix> fixList3 = new ArrayList<>();	
		//报修维修中已评价订单
		List<Fix> fixList4 = new ArrayList<>();	
		
		List<Warranty> warrantyList = new ArrayList<>();
		//延保未支付订单	
		List<Warranty> warrantyList1 = new ArrayList<>();
		//延保已支付
		List<Warranty> warrantyList2 = new ArrayList<>();
		
		User user = (User) session.getAttribute("user");
		logger.info("get the info of user from session");
		//报修列表
		fixList = orderServiceImpl.fixList(user.getUserNo());
		//fixList = orderServiceImpl.fixList("NFET.8812");
		for (Fix fix : fixList) {
			if (fix.getFixSchedule()!=4) {//维修中
				if (evaluateServiceImpl.queryEvaluate(fix.getFixId()) != null) {//已评价
					fix.setOrderTimeStr(fix.getOrderTime().toString().substring(0,19));
					fixList4.add(fix);
				}	
				else {
					fix.setOrderTimeStr(fix.getOrderTime().toString().substring(0,19));
					fixList1.add(fix);
				}
			}
			else {
				if (evaluateServiceImpl.queryEvaluate(fix.getFixId()) != null) {//已评价
					fix.setOrderTimeStr(fix.getOrderTime().toString().substring(0,19));
					fixList2.add(fix);
				}
				else {//未评价
					fix.setOrderTimeStr(fix.getOrderTime().toString().substring(0,19));
					fixList3.add(fix);
				}
			}
		}
		logger.info("get the info of fixList");
		//将数据传给前台
		mav.addObject("fixList1", fixList1);
		mav.addObject("fixList2", fixList2);
		mav.addObject("fixList3", fixList3);
		mav.addObject("fixList4", fixList4);
		//延保列表
		warrantyList = orderServiceImpl.warrantyList(user.getUserNo());
		//warrantyList = orderServiceImpl.warrantyList("NFET.8812");
		for (Warranty warranty : warrantyList) {
			if (warranty.getPayStatus() == 1) {//未支付
				if (warranty.getWarrantyPkgNo() != null) {
					WarrantyPkg warrantyPkg = orderServiceImpl.warrantyPkg(warranty.getWarrantyNo());
					warranty.setWarrantyPkgNo(warrantyPkg);
				}
				if (warranty.getWarrantyVisitNo() != null) {						
					WarrantyPkg warrantyPkg = orderServiceImpl.warrantyVisit(warranty.getWarrantyNo());
					warranty.setWarrantyVisitNo(warrantyPkg);
				}
				warranty.setOrderTimeStr(warranty.getOrderTime().toString().substring(0,19));
				warrantyList1.add(warranty);
			}
			else {
				if (warranty.getWarrantyPkgNo() != null) {
					WarrantyPkg warrantyPkg = orderServiceImpl.warrantyPkg(warranty.getWarrantyNo());
					warranty.setWarrantyPkgNo(warrantyPkg);
				}
				if (warranty.getWarrantyVisitNo() != null) {						
					WarrantyPkg warrantyPkg = orderServiceImpl.warrantyVisit(warranty.getWarrantyNo());
					warranty.setWarrantyVisitNo(warrantyPkg);
				}
				warranty.setOrderTimeStr(warranty.getOrderTime().toString().substring(0,19));
				warrantyList2.add(warranty);
			}
		}
		logger.info("get the info of warrantList");
		mav.addObject("warrantyList1", warrantyList1);
		mav.addObject("warrantyList2", warrantyList2);
		//订单数量
		mav.addObject("fixSize", fixList.size());
		mav.addObject("warrantySize", warrantyList.size());
		
		return mav;
	}
	
	//报修详情
	@RequestMapping("/fixDetail")
	@ResponseBody
	public ModelAndView fixDetail(@RequestParam(value="fixId",required=true) String fixId,
			@RequestParam(value="sort",required=true) String sort){
		ModelAndView mav1 = new ModelAndView("fixover");
		ModelAndView mav2 = new ModelAndView("fixorder");
		Map<String, Object> fixMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		
		//调用信讯通接口获取报修进度
		fixMap.put("fix_no", fixId);
		//fixMap.put("fix_no", "fix201708040006");
		String json = GlobalMethods.parseString(fixMap);
		String result = restTemplate.getForObject(xxtUrl+"fixStatus/{json}", String.class,json);
		JSONObject jsonObject = JSONObject.fromObject(result);	
		String status = jsonObject.getString("status").substring(2);
		//更新报修进度
		map.put("status", status);
		map.put("fixId", fixId);
		map.put("lastModifyTime", new Timestamp(System.currentTimeMillis()));
		fixServiceImpl.updateFix(map);
		if ("4".equals(status)) {
			//设备状态置为正常
			map.put("status", 0);
			map.put("deviceNo", fixServiceImpl.fixInfo(fixId));
			map.put("lastModifyTime", new Timestamp(System.currentTimeMillis()));
			deviceServiceImpl.updateStatus(map);
		}
		
		Fix fix = new Fix();
		fix = orderServiceImpl.fixDetail(fixId);
		fix.setOrderTimeStr(fix.getOrderTime().toString().substring(0,19));
		logger.info("get the fixDetail of " + fixId);
		mav1.addObject("fix", fix);
		mav2.addObject("fix", fix);
		
		if ("1".equals(sort)) {	//已评价		
			return mav1;
		}
		else {  //未评价
			return mav2;
		}
	}
	
	//延保详情
	@RequestMapping("/warrantyDetail")
	@ResponseBody
	public ModelAndView warrantDetail(@RequestParam(value="warrantyNo",required=true) String warrantyNo,
			@RequestParam(value="sort",required=true) String sort) throws ParseException{
		ModelAndView mav1 = new ModelAndView("deferredorder");
		ModelAndView mav2 = new ModelAndView("deferredorderover");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		
		Warranty warranty = new Warranty();
		warranty = orderServiceImpl.warrantyDetail(warrantyNo);
		logger.info("get the warrantyDetail of " + warrantyNo);		
		warranty.setWarrantyPkgNo(orderServiceImpl.warrantyPkg(warrantyNo));
		warranty.setWarrantyVisitNo(orderServiceImpl.warrantyVisit(warrantyNo));
		if (warranty.getWarrantyPkgNo() != null) {
			//如果选择了上门且延保卡、延保期需要加上上门且延保卡的延保期限
			if (warranty.getWarrantyVisitNo() != null && warranty.getWarrantyVisitNo().getWarrantyType() == 3) {
				warranty.getWarrantyPkgNo().setWarrantyPkgPeriod(warranty.getWarrantyPkgNo().getWarrantyPkgPeriod()+warranty.getWarrantyVisitNo().getWarrantyPkgPeriod());
			}
		}
		//将日期从yyyy-MM-dd转成yyyy年MM月dd日
		warranty.setWarrantyFrom(sdf1.format(sdf.parse(warranty.getWarrantyFrom())));
		warranty.setWarrantyTo(sdf1.format(sdf.parse(warranty.getWarrantyTo())));
		if (warranty.getVisitFrom() != null && !"".equals(warranty.getVisitFrom())) {			
			warranty.setVisitFrom(sdf1.format(sdf.parse(warranty.getVisitFrom())));
		}
		if (warranty.getVisitTo() != null && !"".equals(warranty.getVisitTo())) {			
			warranty.setVisitTo(sdf1.format(sdf.parse(warranty.getVisitTo())));
		}
		warranty.setOrderTimeStr(warranty.getOrderTime().toString().substring(0,19));
		
		//返回数据到前台
		mav1.addObject("warranty", warranty);
		mav2.addObject("warranty", warranty);
		
		//根据状态选择跳转页面
		if ("1".equals(sort)) {	//未支付		
			return mav1;
		}
		else { //已支付
			return mav2;
		} 
	}

}
