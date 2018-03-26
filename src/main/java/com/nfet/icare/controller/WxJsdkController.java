package com.nfet.icare.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nfet.icare.pay.WXPay;
import com.nfet.icare.pay.WXPayConfigImpl;
import com.nfet.icare.pojo.PreorderResult;
import com.nfet.icare.pojo.WechatInfo;
import com.nfet.icare.pojo.WechatOauth2Token;
import com.nfet.icare.util.Oauth2Util;
import com.nfet.icare.util.Sign;
import com.nfet.icare.util.WXUtils;

/**
 * @author mark
 *
 *         JSSDK使用步骤 step1：绑定域名,先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
 *         step2：引入JS文件,在需要调用JS接口的页面引入如下JS文件，（支持https）：http://res.wx.qq.com/open/js/jweixin-1.2.0.js
 *         step3：通过config接口注入权限验证配置 step4：通过wx.scanQRCode调用微信摄像头
 */
@Controller
@RequestMapping("/wxjs")
public class WxJsdkController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WechatInfo wechatInfo;

	@Value("${signUrl}")
	private String signUrl;

	/**
	 * 获取微信js sdk ticket
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	public String jsdkHandler(@RequestParam(value="type",required=true) String type,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		String jsTicket;
		if (session.getAttribute("wxJsTicket") == null) {
			// 获取网页access_token(不同于获取用户新的access_token)
			logger.info("js access_token is null in session");
			String jsAccessToken = Oauth2Util.getJsSdkToken(wechatInfo.getAppid(), wechatInfo.getSecret());

			// 获取网页js-ticket
			logger.info("js access_token is :" + jsAccessToken);
			jsTicket = Oauth2Util.getJsTicket(jsAccessToken, wechatInfo.getAppid());
			logger.info("js ticket is :" + jsTicket);
		} else {
			logger.info("js access_token is in session:");
			jsTicket = String.valueOf(session.getAttribute("wxJsTicket"));
			logger.info(jsTicket);
		}

		// signUrl += type;
		Map<String, String> signObj = Sign.sign(jsTicket, signUrl+"?type="+type);
		String nonceStr = signObj.get("nonceStr");
		String timestamp = signObj.get("timestamp");
		String signature = signObj.get("signature");
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("signature", signature);

		if ("1".equals(type)) {
			return "scanequipment";
		}
		else if ("2".equals(type)) {
			return "onlinerepair";
		}
		else {
			return "onlinewarranty";
		}
	}

	/**
	 * 微信支付预下单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	@ResponseBody
	public PreorderResult preOrder(@RequestParam(value="deviceNo",required=true) String deviceNo,
			@RequestParam(value="payAmt",required=true) String payAmt,
			HttpServletRequest request) throws Exception {
		System.out.println(deviceNo);
		System.out.println(payAmt);
		PreorderResult pr = new PreorderResult();
		String openid = "";
		WechatOauth2Token weixinOauth2Token = (WechatOauth2Token) request.getSession()
				.getAttribute("weixinOauth2Token");
		if (weixinOauth2Token != null) {
			openid = weixinOauth2Token.getOpenId();
		}
		WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		WXPay wx = new WXPay(config);
		String out_trade_no = RandomStringUtils.randomAlphanumeric(32);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("body", "nfeto2o");
		data.put("out_trade_no", out_trade_no);
		data.put("device_info", deviceNo);
		//data.put("device_info", "DPK23801");
		data.put("fee_type", "CNY");
		data.put("total_fee", Integer.parseInt(payAmt)*100+"");
		//data.put("total_fee", "1");
		data.put("spbill_create_ip", WXUtils.getIpAddr(request));
		data.put("notify_url", "http://m.e-is.cn");
		data.put("trade_type", "JSAPI");
		data.put("openid", openid);

		System.out.println("openid is:" + openid);
		pr.setOpenid(openid);

		Map<String, String> result = wx.unifiedOrder(data);
		logger.info("preOrder result is :" + result);
		String return_code = result.get("return_code");
		if ("SUCCESS".equals(return_code)) {
			String prepay_id = result.get("prepay_id");
			logger.info("preOrder prepay_id is :" + prepay_id);

			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appId", wechatInfo.getAppid());
			params.put("timeStamp", Long.toString(new Date().getTime()));
			params.put("nonceStr", WXUtils.CreateNoncestr(32));
			params.put("package", "prepay_id=" + prepay_id);
			params.put("signType", "MD5");
			String paySign = WXUtils.createSign("UTF-8", params, wechatInfo.getApiKey());

			logger.info("preOrder paySign is :" + paySign);
			// String pkg = "prepay_id=" + prepay_id;
			pr.setNonceStr(params.get("nonceStr").toString());
			pr.setTimeStamp(params.get("timeStamp").toString());
			pr.setPkg(params.get("package").toString());
			pr.setPaySign(paySign);
			pr.setAppid(params.get("appId").toString());

			System.out.println("Preorder params is:" + pr);

		}
		return pr;

	}
}
