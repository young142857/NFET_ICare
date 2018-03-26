package com.nfet.icare.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.nfet.icare.pojo.Area;
import com.nfet.icare.pojo.Branch;
import com.nfet.icare.pojo.City;
import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.FaultList;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Province;
import com.nfet.icare.pojo.SNSUserInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.FixServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.ZoneServiceImpl;
import com.nfet.icare.util.GlobalConstants;

import com.nfet.icare.util.GlobalMethods;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 设备报修
 * 1、故障类型列表
 * 2、送修公司地址获取
 * 3、生成报修单
 * 4、点击报修
 */

@Controller
public class FixController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FixServiceImpl fixServiceImpl;	
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;	
	@Autowired
	private ZoneServiceImpl zoneServiceImpl;	
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${xxtUrl}")
	private String xxtUrl;
	
	//故障类型列表
	@RequestMapping("/faultList")
	@ResponseBody
	public List<FaultList> faultList(){
		//调信讯通接口获取故障类型
		String result = restTemplate.getForObject(xxtUrl+"faultTypes/{}", String.class);
		logger.info("3s for faultTypes : " + result);
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONArray array = jsonObject.getJSONArray("data");
		List<FaultList> faultList = JSONArray.toList(array, FaultList.class);
		
		return faultList;
	}
	
	//获取送修分公司地址
	@RequestMapping("/branchAddress")
	@ResponseBody
	public Branch branchAddress(@RequestParam(value="cityId",required=true) String cityId){
		Branch branch = new Branch();
		branch = fixServiceImpl.branchAddress(cityId);
		logger.info("get the info of branch");
		return branch;
	}
	
	//生成报修单
	@RequestMapping("/fixOrder")
	@ResponseBody
	public ModelAndView fixOrder(@RequestParam(value="deviceNo",required=true) String deviceNo,
			HttpServletRequest request) throws ParseException{
		ModelAndView mav = new ModelAndView("repairorder");
		Device device = new Device();
		Device device1 = new Device();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		HttpSession session = request.getSession();
		Map<String, Object> deviceMap = new HashMap<>();
		User user = (User) session.getAttribute("user");
		Ticket ticket = new Ticket();
		
		//对接收到的设备编码进行处理
		int index = deviceNo.toUpperCase().indexOf("VN");
		if (index != -1) {
			deviceNo = deviceNo.substring(index);			
		}
		
		device = deviceServiceImpl.bindOrNot(deviceNo);
		DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
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
		logger.info("get the info of device");
		mav.addObject("device", device);
		
		return mav;		
	}
	
	//判断设备是否存在
	@RequestMapping("/checkFix")
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
				if (user.getUserNo().equals(device.getUserNo())) { //是报修人的设备
					logger.info("status:" + fixServiceImpl.queryFixId(deviceNo));
					if ("0".equals(fixServiceImpl.queryFixId(deviceNo))) { //未报修
						logger.info("unfix");
						map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
					}
					else {//已报修
						logger.info("fixing");
						map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
						map.put("desc", "该设备正在维修中！");												
					}
				}
				else { //不是报修人的设备
					map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
					map.put("desc", "该设备已被其他用户注册，您无法报修！如有疑问请联系报修客服：400-8020-048");
				}
			}
			else { //设备未绑定
				map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
			}
		}
		
		return map;
	}
	
	//点击报修
	@RequestMapping("/clickFix")
	@ResponseBody
	public Map<String, Object> clickFix(@RequestParam(value="deviceNo",required=true) String deviceNo,
			@RequestParam(value="fixNo",required=true) String fixNo,
			@RequestParam(value="faultName",required=true) String faultName,
			@RequestParam(value="fixDesc",required=false) String fixDesc,
			@RequestParam(value="fixPattern",required=true) String fixPattern,
			@RequestParam(value="industry",required=true) String industry,
			@RequestParam(value="provinceId",required=false) String provinceId,
			@RequestParam(value="cityId",required=false) String cityId,
			@RequestParam(value="areaId",required=false) String areaId,
			@RequestParam(value="address",required=false) String address,
			HttpServletRequest request){
		Map<String, Object> fixMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> userMap = new HashMap<>();
		Map<String, Object> zoneMap = new HashMap<>();
		Fix fix = new Fix();
		FaultList fault = new FaultList();
		Province province = new Province();
		City city = new City();
		Area area = new Area();
		DeviceInfo deviceInfo = new DeviceInfo();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Company company = (Company) session.getAttribute("company");
		SNSUserInfo snsUserInfo = (SNSUserInfo) session.getAttribute("snsUserInfo");
		logger.info("get the info of company according to companyId");
		
		deviceInfo = deviceServiceImpl.existDevice(deviceNo);
		fault.setType_no(fixNo);
		//设备未报修或已维修完成才给报修
		if ("0".equals(fixServiceImpl.queryFixId(deviceNo))) {			
			if ("0".equals(fixPattern)) {//上门维修			
				fixMap.put("fix_type", "1");
				fixMap.put("address", address);
				//此时完善用户信息			
				//调用信讯通接口
				userMap.put("user_no", user.getUserNo());
				userMap.put("user_name", user.getName());
				userMap.put("phone", user.getPhone());
				userMap.put("company", company.getCompanyName());
				//通过区域id获取区域名称
				String provinceName = zoneServiceImpl.queryProvinceName(Integer.parseInt(provinceId)); 
				String cityName = zoneServiceImpl.quertCityName(Integer.parseInt(cityId));
				String areaName = zoneServiceImpl.queryAreaName(Integer.parseInt(areaId));
				userMap.put("zone", provinceName+cityName+areaName);
				userMap.put("industry", industry);
				userMap.put("address", address);
				userMap.put("wx_name", snsUserInfo.getNickname());
				String json = GlobalMethods.parseString(userMap);
				restTemplate.getForObject(xxtUrl+"update/{json}", String.class,json);
				logger.info("3s for update : " + restTemplate.getForObject(xxtUrl+"update/{json}", String.class,json));
				//更新公司信息
				company.setProvinceId(Integer.parseInt(provinceId));
				company.setCityId(Integer.parseInt(cityId));
				company.setAreaId(Integer.parseInt(areaId));
				company.setAddress(address);				
				//保存区域
				if (!"0".equals(provinceId)) {
					zoneMap.put("province", zoneServiceImpl.queryProvinceName(Integer.parseInt(provinceId)));
				}
				if (!"0".equals(cityId)) {
					zoneMap.put("city", zoneServiceImpl.quertCityName(Integer.parseInt(cityId)));
				}
				if (!"0".equals(areaId)) {
					zoneMap.put("area", zoneServiceImpl.queryAreaName(Integer.parseInt(areaId)));
				}
				session.setAttribute("zone", zoneMap);
			}
			else {
				fixMap.put("fix_type", "0");
				fixMap.put("address", address);
			}
			//更新公司信息
			company.setIndustry(industry);
			company.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			companyServiceImpl.updateCompany(company);
			logger.info("complete the info of company");
			//传递新的公司参数
			session.setAttribute("company", company);
			logger.info("save the info of company in session");
			
			fixMap.put("device_no", deviceNo);
			fixMap.put("fault_type", fixNo);
			fixMap.put("fault_desc", fixDesc);		
			String json = GlobalMethods.parseString(fixMap);
			String result = restTemplate.getForObject(xxtUrl+"fix/{json}", String.class,json);
			logger.info("3s for fix : " + result);
			JSONObject jsonObject = JSONObject.fromObject(result);	
			String fixId = jsonObject.getString("fix_no");
			
			//设备状态置为维修中
			map.put("status", 1);
			map.put("deviceNo", deviceNo);
			map.put("lastModifyTime", new Timestamp(System.currentTimeMillis()));
			deviceServiceImpl.updateStatus(map);
			
			//保存数据到报修表
			province.setProvinceId(Integer.parseInt(provinceId));
			city.setCityId(Integer.parseInt(cityId));
			area.setAreaId(Integer.parseInt(areaId));
			fix.setProvince(province);
			fix.setCity(city);
			fix.setArea(area);
			fix.setAddress(address);			
			fix.setFixId(fixId);
			fix.setDeviceNo(deviceInfo);
			fix.setUserNo(user.getUserNo());
			fix.setUserPhone(user.getPhone());
			fix.setCompanyName(company.getCompanyName());
			if (company.getPhone() != null) {			
				fix.setCompanyPhone(company.getPhone());
			}
			fix.setCompanyIndustry(industry);
			fix.setFixPattern(Integer.parseInt(fixPattern));
			fix.setFaultName(faultName);
			fix.setFixDesc(fixDesc);
			fix.setOrderTime(new Timestamp(System.currentTimeMillis()));
			fix.setCreateTime(new Timestamp(System.currentTimeMillis()));
			fix.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			fixServiceImpl.insertFix(fix);
			logger.info("insert the data of fix into data_fix");
			
			resultMap.put("code", GlobalConstants.ERROR_CODE_SUCCESS);
			resultMap.put("fixId", fixId);
			resultMap.put("deviceNo", deviceNo);
		}
		else {
			resultMap.put("code", GlobalConstants.ERROR_CODE_FAIL);
		}
		
		System.out.println("code:"+resultMap.get("code"));;
		
		return resultMap; 
	}
	
	//报修成功
	@RequestMapping("/fixsuccess")
	@ResponseBody
	public ModelAndView fixsuccess(@RequestParam(value="fixId",required=true) String fixId,
			@RequestParam(value="deviceNo",required=true) String deviceNo){
		ModelAndView mav = new ModelAndView("fixsuccess");
		mav.addObject("fixId", fixId);
		mav.addObject("deviceNo", deviceNo);
		
		return mav;
	}
	
	//报修进度页面
	@RequestMapping("/fixStatusPage")
	@ResponseBody
	public ModelAndView fixStatusPage(@RequestParam(value="fixId",required=true) String fixId,
			@RequestParam(value="deviceNo",required=true) String deviceNo){
		ModelAndView mav = new ModelAndView("orderstatus");
		Map<String, Object> fixMap = new HashMap<>();
		logger.info("fixStatusPage-----ficId: "+fixId);
		logger.info("fixStatusPage-----deviceNo: "+deviceNo);
				
		//调用信讯通接口
		fixMap.put("fix_no", fixId);
		String json = GlobalMethods.parseString(fixMap);
		String result = restTemplate.getForObject(xxtUrl+"fixStatus/{json}", String.class,json);
		logger.info("3s for fixStatus : " + result);
		JSONObject jsonObject = JSONObject.fromObject(result);	
		String status = jsonObject.getString("status").substring(2);
		
		Device device = deviceServiceImpl.bindOrNot(deviceNo);
		mav.addObject("fixId", fixId);
		mav.addObject("deviceNo", deviceNo);
		mav.addObject("device", device);
		mav.addObject("status", status);
		
		return mav;
	}
	
	//报修进度
	@RequestMapping("/fixSchedule")
	@ResponseBody
	public String fixSchedule(@RequestParam(value="fixId",required=true) String fixId){
		Map<String, Object> fixMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		
		//调用信讯通接口
		fixMap.put("fix_no", fixId);
		String json = GlobalMethods.parseString(fixMap);
		String result = restTemplate.getForObject(xxtUrl+"fixStatus/{json}", String.class,json);
		logger.info("3s for fixStatus : " + result);
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
		
//		return "{\"error_code\":\"0\",\"desc\":\"OK\",\"device_no\":\"VN36085969\",\"status\":\"004\",\"last_modify_time\":\"2017-07-13 16:36:55\",\"data\":[{\"time\":\"2017-07-13 16:36:55\",\"msg\":\"已报修\"},"
//		+"{\"time\":\"2017-07-13 18:36:55\",\"msg\":\"已受理\"},{\"time\":\"2017-07-13 20:36:55\",\"msg\":\"处理中\"},{\"time\":\"2017-07-13 22:36:55\",\"msg\":\"完成\"}"+"]}";
		return result;
	}
 }
