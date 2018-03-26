package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Mgr_User;
import com.nfet.icare.pojo.User;

public interface UserService {
	//判断手机号是否已被注册
	public User checkUserPhone(String phone);
	//用户初始注册
	public void initReg(User user);
	//完善用户信息
	public void updateUser(User user);
	//注册赠送积分
	public void giveScore(Map<String, Object> map);
	//获取所有的手机号
	public List<CacheInfo> getAllPhones();
	
	//后端（PC端）
	//所有用户列表
	public List<Mgr_User> userList();
	//一周前注册数量
	public int sevenCounts(String date);
}
