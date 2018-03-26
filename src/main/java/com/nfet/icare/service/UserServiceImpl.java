package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.UserMapper;
import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Mgr_User;
import com.nfet.icare.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	//用户初始注册
	@Override
	public void initReg(User user) {
		// TODO Auto-generated method stub
		userMapper.initReg(user);
	}
	
	//判断手机号是否已被注册
	@Override
	public User checkUserPhone(String phone) {
		// TODO Auto-generated method stub
		return	userMapper.checkUserPhone(phone);
	}

	//完善用户信息
	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		userMapper.updateUser(user);
	}
	
	//注册赠送积分
	@Override
	public void giveScore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		userMapper.giveScore(map);
	}

	// 获取所有的手机号
	@Override
	public List<CacheInfo> getAllPhones() {
		// TODO Auto-generated method stub
		List<CacheInfo> phoneList = userMapper.getAllPhones();
		return phoneList;
	}

	@Override
	public List<Mgr_User> userList() {
		// TODO Auto-generated method stub
		return userMapper.userList();
	}

	@Override
	public int sevenCounts(String date) {
		// TODO Auto-generated method stub
		return userMapper.sevenCounts(date);
	}
	
}
