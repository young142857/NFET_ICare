package com.nfet.icare.service;

import com.nfet.icare.pojo.WxUser;

//微信用户的openId和手机号
public interface WxUserService {
	//保存
	public void insertWxUser(WxUser wxUser);
	//查询
	public WxUser queryWxUser(String openId);
}
