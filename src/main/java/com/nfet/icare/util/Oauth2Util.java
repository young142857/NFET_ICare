package com.nfet.icare.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nfet.icare.pojo.SNSUserInfo;
import com.nfet.icare.pojo.WechatOauth2Token;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @ClassName: Oauth2Util
 * @Description:Oauth2网页授权工具类
 * @author: yinjun
 * @date: 2017/6/20
 * @version V1.0
 * @Copyright: 2017 NFET All rights reserved.
 */
public class Oauth2Util {
	private static Logger logger = LoggerFactory.getLogger(Oauth2Util.class);

	/**
	 * @Title: getOauth2AccessToken
	 * @Description: 获取code后，请求以下链接获取access_token：
	 *               https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&
	 *               secret=SECRET&code=CODE&grant_type=authorization_code
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 */
	public static WechatOauth2Token getOauth2AccessToken(String code, String appId, String secret) {
		WechatOauth2Token wot = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret
				+ "&code=CODE&grant_type=authorization_code";
		requestUrl = requestUrl.replace("CODE", code);
		logger.info("requestUrl is " + requestUrl);

		// https获取网页授权凭证
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				logger.info("获取到了网页授权凭证, jsonObject不为null");
				wot = new WechatOauth2Token();
				logger.info("access_token　is:" + jsonObject.getString("access_token"));
				wot.setAccessToken(jsonObject.getString("access_token"));
				logger.info("expires_in　is:" + jsonObject.getInt("expires_in"));
				wot.setExpiresIn(jsonObject.getInt("expires_in"));
				logger.info("refresh_token　is:" + jsonObject.getString("refresh_token"));
				wot.setRefreshToken(jsonObject.getString("refresh_token"));
				logger.info("openid　is:" + jsonObject.getString("openid"));
				wot.setOpenId(jsonObject.getString("openid"));
				logger.info("scope　is:" + jsonObject.getString("scope"));
				wot.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				e.printStackTrace();
				wot = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				logger.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return wot;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
		SNSUserInfo snsUserInfo = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 通过网页授权获取用户信息
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				snsUserInfo = new SNSUserInfo();
				// 用户的标识
				snsUserInfo.setOpenId(jsonObject.getString("openid"));
				// 昵称
				snsUserInfo.setNickname(jsonObject.getString("nickname"));
				// 性别（1是男性，2是女性，0是未知）
				snsUserInfo.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				snsUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				snsUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				snsUserInfo.setCity(jsonObject.getString("city"));
				// 用户头像
				snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
				System.out.println("user image :" + snsUserInfo.getHeadImgUrl());
				// 用户特权信息
				snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
			} catch (Exception e) {
				snsUserInfo = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				logger.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return snsUserInfo;
	}

	public static String getJsSdkToken(String appid, String secret) {
		String weixin_jssdk_acceToken_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		String requestUrl = String.format(weixin_jssdk_acceToken_url, appid, secret);

		logger.info("requestUrl　is:" + requestUrl);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		logger.info("getJsSdkToken　jsonObject is:" + jsonObject);
		String access_token = "";
		// 如果请求成功
		if (null != jsonObject) {
			try {
				access_token = jsonObject.getString("access_token");
			} catch (JSONException e) {
				access_token = null;
			}
		}
		return access_token;
	}

	public static String getJsTicket(String accessToken, String appid) {
		String weixin_jssdk_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
		String requestUrl = String.format(weixin_jssdk_ticket_url, accessToken);
		logger.info("requestUrl　is:" + requestUrl);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		String ticket = "";
		// 如果请求成功
		if (null != jsonObject) {
			try {
				ticket = jsonObject.getString("ticket");
			} catch (JSONException e) {
				ticket = null;
			}
		}
		return ticket;
	}

}
