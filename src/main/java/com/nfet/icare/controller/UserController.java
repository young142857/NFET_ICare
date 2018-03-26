package com.nfet.icare.controller;

import java.sql.Timestamp;
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

import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.SNSUserInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.pojo.WxUser;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.service.WxUserServiceImpl;
import com.nfet.icare.service.ZoneServiceImpl;
import com.nfet.icare.util.CacheMgr;
import com.nfet.icare.util.GlobalConstants;
import com.nfet.icare.util.GlobalMethods;

import net.sf.json.JSONObject;

/**
 * 用户信息
 * 1、用户注册
 * 2、完善用户信息
 * 3、注册赠送积分
 *
 */

@Controller
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	private WxUserServiceImpl wxUserServiceImpl;
	@Autowired
	private ZoneServiceImpl zoneServiceImpl;
	@Value("${xxtUrl}")
	private String xxtUrl;

	//用户初始注册
	@RequestMapping(value="/reg")
	@ResponseBody
	public Map<String, String> initReg(@RequestParam(value="userName",required=true) String userName,
			@RequestParam(value="phone",required=true) String phone,
			@RequestParam(value="company",required=true) String companyName,
			@RequestParam(value="vCode",required=true) String vCode,
			HttpServletRequest request){		
		Map<String, String> map = new HashMap<>();
		Map<String, Object> userMap = new HashMap<>();
		Map<String, Object> ticketMap = new HashMap<>();
		Map<String, Object> zoneMap = new HashMap<>();
		SNSUserInfo snsUserInfo = new SNSUserInfo();
		User user = new User();
		User user1 = new User();
		WxUser wxUser = new WxUser();
		Company company = new Company();
		Company company1 = new Company();
		int ticketCount = 0;
		HttpSession session = request.getSession();
		//验证码
		String code = (String) session.getAttribute("smsCode");			
		//信息填写不完整
		if (userName ==null || "".equals(userName) ||
				phone == null || "".equals(phone) ||
				companyName == null || "".equals(companyName) ||
				vCode == null || "".equals(vCode)) {
			map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			map.put("desc", "请将信息填写完整!");
		}
		else {
			user = userServiceImpl.checkUserPhone(phone);
			//手机号不存在、进行注册
			if (user == null) {
				logger.info("phone:" + phone + " can be registered");
				//验证码一致
				if (vCode.equals(code)) {
					logger.info("code:" + code + " equal " + vCode);
					
					company = companyServiceImpl.checkCompanyName(companyName);
					//输入公司已存在				
					if (company != null) {
						logger.info("companyName:" + companyName + " already exists ");
						user1.setCompanyId(company.getCompanyId());												
						//传递公司参数
						session.setAttribute("company", company);
						logger.info("save the info of company  in session");
						//保存区域
						if (company.getProvinceId() != 0) {
							zoneMap.put("province", zoneServiceImpl.queryProvinceName(company.getProvinceId()));
						}
						if (company.getCityId() != 0) {
							zoneMap.put("city", zoneServiceImpl.quertCityName(company.getCityId()));
						}
						if (company.getAreaId() != 0) {
							zoneMap.put("area", zoneServiceImpl.queryAreaName(company.getAreaId()));
						}
						session.setAttribute("zone", zoneMap);
					}
					else {//公司不存在
						logger.info("companyName:" + companyName + " not exists ");
						if (companyServiceImpl.getMaxCompanyId() == null) {
							company1.setCompanyId(1);	
						}
						else {							
							company1.setCompanyId(companyServiceImpl.getMaxCompanyId()+1);												
						}
						company1.setCompanyName(companyName);
						company1.setCreateTime(new Timestamp(System.currentTimeMillis()));
						company1.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
						//传递公司参数
						session.setAttribute("company", company1);
						logger.info("save the info of company  in session");
						companyServiceImpl.insertCompany(company1);
						logger.info("insert data into data_company");
						
						//新增的公司信息放入缓存
						CacheMgr.getInstance().putCompany(companyName,company1.getCompanyId()+"");
						
						user1.setCompanyId(company1.getCompanyId());
						
					}					
					snsUserInfo = (SNSUserInfo)session.getAttribute("snsUserInfo");
					
					//调用信讯通接口获取userNO
					userMap.put("user_name", userName);
					userMap.put("phone", phone);
					userMap.put("company", companyName);
					String json = GlobalMethods.parseString(userMap);
					String result = restTemplate.getForObject(xxtUrl+"register/{json}", String.class,json);
					JSONObject jsonObject = JSONObject.fromObject(result);
					logger.info("3s return result:"+jsonObject);
					String userNo = jsonObject.getString("user_no");
					
					user1.setUserNo(userNo);
					user1.setName(userName);
					user1.setPhone(phone);
					user1.setGiveScore(0);
					user1.setWxName(snsUserInfo.getNickname());
					user1.setWxImg(snsUserInfo.getHeadImgUrl());
					user1.setCreateTime(new Timestamp(System.currentTimeMillis()));
					user1.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
					//传递用户参数
					session.setAttribute("user", user1);
					logger.info("save the info of user  in session");
					//传递优惠券参数
					ticketMap.put("userNo", userNo);
					ticketMap.put("type", 1);
					List<Ticket> ticketList = ticketServiceImpl.ticketList(ticketMap);
					for (Ticket ticket : ticketList) {
						if (ticket.getStatus() == 0) {
							ticketCount ++;
						}
					}
					session.setAttribute("ticketCount", ticketCount);
					logger.info("save the count of ticket  in session");
					//保存用户openid
					if (wxUserServiceImpl.queryWxUser(snsUserInfo.getOpenId()) == null) {
						wxUser.setOpenId(snsUserInfo.getOpenId());
						wxUser.setPhone(phone);
						wxUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
						wxUser.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
						wxUserServiceImpl.insertWxUser(wxUser);
					}					
					//保存用户数据
					if (userServiceImpl.checkUserPhone(phone) == null) {						
						userServiceImpl.initReg(user1);
						logger.info("insert data into data_user");
						
						//新增的用户信息放入缓存
						CacheMgr.getInstance().putUser(phone, userNo);
					}
					map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
				}
				else {//验证码不一致
					logger.info(code + " not equal " + vCode);
					map.put("desc", "验证码不匹配!");
					map.put("errorCode",GlobalConstants.ERROR_CODE_FAIL);
				}
			}
			else {
				logger.info(phone + " have been registered");
				map.put("desc", "该手机号已注册!");
				map.put("errorCode", "1");
			}
		}
		
		return map;
	}
	
	//完善用户信息
	@RequestMapping("/completeUserInfo")
	@ResponseBody	
	public Map<String, String> completeUserInfo(
			@RequestParam(value="userName",required=true) String userName,
			@RequestParam(value="companyName",required=true) String companyName,
			@RequestParam(value="industry",required=true) String industry,
			@RequestParam(value="provinceId",required=false) int provinceId,
			@RequestParam(value="cityId",required=false) int cityId,
			@RequestParam(value="areaId",required=false) int areaId,
			@RequestParam(value="address",required=false) String address,
			HttpServletRequest request){
		Map<String, String> map = new HashMap<>();
		Map<String, Object> userMap = new HashMap<>();
		Map<String, Object> zoneMap = new HashMap<>();
		Company company = new Company();
		User user = new User();
		SNSUserInfo snsUserInfo = new SNSUserInfo();
		HttpSession session = request.getSession();
		snsUserInfo = (SNSUserInfo)session.getAttribute("snsUserInfo");
		user = (User) session.getAttribute("user");			
		logger.info("get the info of company from session");
		
		//信息填写不完整
		if (userName == null || "".equals(userName) ||
				companyName == null || "".equals(companyName) ||
				industry == null || "".equals(industry)) {
			logger.info("uncompleted the info");
			map.put("errorCode", GlobalConstants.ERROR_CODE_FAIL);
			map.put("desc", "所属行业不能为空!");
		}
		else {			
			company = (Company) session.getAttribute("company");
			//调用信讯通接口
			userMap.put("user_no", user.getUserNo());
			userMap.put("user_name", userName);
			userMap.put("phone", user.getPhone());
			userMap.put("company", companyName);
			//通过区域id获取区域名称
			String provinceName = zoneServiceImpl.queryProvinceName(provinceId); 
			String cityName = zoneServiceImpl.quertCityName(cityId);
			String areaName = zoneServiceImpl.queryAreaName(areaId);
			userMap.put("zone", provinceName+cityName+areaName);
			userMap.put("industry", industry);
			userMap.put("address", address);
			userMap.put("wx_name", snsUserInfo.getNickname());
			//userMap.put("wx_name", "11");
			String json = GlobalMethods.parseString(userMap);
			restTemplate.getForObject(xxtUrl+"update/{json}", String.class,json);
			logger.info("3s for update : " + restTemplate.getForObject(xxtUrl+"update/{json}", String.class,json));
			
			//更新用户信息
			user.setName(userName);
			user.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			//更新公司信息
			company.setCompanyName(companyName);
			company.setIndustry(industry);
			company.setProvinceId(provinceId);
			company.setCityId(cityId);
			company.setAreaId(areaId);
			company.setAddress(address);
			company.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			userServiceImpl.updateUser(user);
			logger.info("complete the info of user");
			companyServiceImpl.updateCompany(company);
			logger.info("complete the info of company");
			//保存区域
			if (provinceId != 0) {
				zoneMap.put("province", zoneServiceImpl.queryProvinceName(provinceId));
				System.out.println("1:"+zoneServiceImpl.queryProvinceName(provinceId));
			}
			if (cityId != 0) {
				zoneMap.put("city", zoneServiceImpl.quertCityName(cityId));
				System.out.println("2:"+zoneServiceImpl.quertCityName(cityId));
			}
			if (areaId != 0) {
				zoneMap.put("area", zoneServiceImpl.queryAreaName(areaId));
				System.out.println("3:"+zoneServiceImpl.queryAreaName(areaId));
			}
			session.setAttribute("zone", zoneMap);
			//传递新的用户参数
			session.setAttribute("user", user);
			//传递新的公司参数
			session.setAttribute("company", company);
			logger.info("save the info of company  in session");
			map.put("errorCode", GlobalConstants.ERROR_CODE_SUCCESS);
		}		
		return map;
	}
	
	//注册赠送积分
	@RequestMapping("/giveScore")
	@ResponseBody
	public String giveScore(@RequestParam(value="userNo",required=true) String userNo,
			HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		map.put("userNo", userNo);
		map.put("giveScore", 1);
		map.put("score", 50);
		userServiceImpl.giveScore(map);
		logger.info("give 50 score to new user");
		user.setScore(50);
		user.setGiveScore(1);
		session.setAttribute("user", user);
		
		return GlobalConstants.ERROR_CODE_SUCCESS;
	}
}