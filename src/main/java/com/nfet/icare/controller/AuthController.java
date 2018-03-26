package com.nfet.icare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.SNSUserInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.pojo.WechatInfo;
import com.nfet.icare.pojo.WechatOauth2Token;
import com.nfet.icare.pojo.WxUser;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.service.WxUserServiceImpl;
import com.nfet.icare.service.ZoneServiceImpl;
import com.nfet.icare.util.Oauth2Util;

/**
 * 微信用户信息获取(openid,headimg...)
 * 
 * @author mark
 *
 */
@Controller
@RequestMapping("/wechat")
public class AuthController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WechatInfo wechatInfo;
	@Autowired
	private WxUserServiceImpl wxUserServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	private ZoneServiceImpl zoneServiceImpl;

	@RequestMapping(value = "oauth/{opt}", method = RequestMethod.GET)
	public String oauthController(@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "state", required = true) String state, Model model, HttpServletRequest request,
			@PathVariable String opt, RedirectAttributes attr) {
		WechatOauth2Token weixinOauth2Token = null;
		String accessToken = null;
		HttpSession session = request.getSession();
		logger.info("opt is " + opt);
		logger.info("Code=============" + code + "==========state=======" + state);
		// 用户同意授权
		if (!"authdeny".equals(code)) {

			// 获取网页授权access_token，第一次访问绑定到会话上，如果刷新页面不再使用code
			if (session.getAttribute("weixinOauth2Token") == null) {
				logger.info("not session");
				weixinOauth2Token = Oauth2Util.getOauth2AccessToken(code, wechatInfo.getAppid(),
						wechatInfo.getSecret());
				session.setAttribute("weixinOauth2Token", weixinOauth2Token);
			} else {
				logger.info("session");
				weixinOauth2Token = (WechatOauth2Token) session.getAttribute("weixinOauth2Token");
			}
			// 网页授权接口访问凭证
			accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			logger.info("openId is:" + openId);
			if (session.getAttribute("snsUserInfo") == null) {
				// 获取用户信息
				SNSUserInfo snsUserInfo = Oauth2Util.getSNSUserInfo(accessToken, openId);
				logger.info("snsUserInfo is:" + snsUserInfo);
				session.setAttribute("snsUserInfo", snsUserInfo);

				// 设置要传递的参数
				model.addAttribute("snsUserInfo", snsUserInfo);
				model.addAttribute("state", state);
			}

			if (!isRegisterUser(openId, session)) {
				logger.info("not register user,openid is:" + openId);
				return "registerpage";
			} else {
				if (request.getParameter("deviceType") != null) {
					attr.addAttribute("type", request.getParameter("deviceType"));
					return "redirect:/deviceList";
				} else if ("wxjs".equals(opt)) {
					attr.addAttribute("type", "1");
					return "redirect:/wxjs/scan";
				} else {
					return opt;
				}
			}
		} else {
			return "404";
		}
	}

	/**
	 * 判断用户是否已经注册
	 * 
	 * @param openId
	 * @return
	 */
	private boolean isRegisterUser(String openId, HttpSession session) {
		WxUser wxUser = wxUserServiceImpl.queryWxUser(openId);
		if (wxUser == null) {// 未找到、没有注册
			return false;
		} else {
			// 已注册、将用户信息存入session
			Map<String, Object> ticketMap = new HashMap<>();
			Map<String, Object> zoneMap = new HashMap<>();
			User user = userServiceImpl.checkUserPhone(wxUser.getPhone());
			Company company = companyServiceImpl.queryCompanyInfo(user.getCompanyId());
			// 保存区域
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
			// 优惠券数量
			ticketMap.put("userNo", user.getUserNo());
			ticketMap.put("type", 1);
			List<Ticket> ticketList = ticketServiceImpl.ticketList(ticketMap);
			int ticketCount = 0;
			for (Ticket ticket : ticketList) {
				if (ticket.getStatus() == 0) {
					ticketCount++;
				}
			}
			session.setAttribute("ticketCount", ticketCount);
			session.setAttribute("user", user);
			session.setAttribute("company", company);
			return true;
		}
	}
}
