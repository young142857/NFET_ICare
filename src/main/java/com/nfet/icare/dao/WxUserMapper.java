package com.nfet.icare.dao;


import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.WxUser;

//微信用户的openId和手机号
public interface WxUserMapper {
	//保存
	public void insertWxUser(@Param("wxUser") WxUser wxUser);
	//查询
	public WxUser queryWxUser(@Param("openId") String openId);
}
