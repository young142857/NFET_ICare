package com.nfet.icare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.WxUserMapper;
import com.nfet.icare.pojo.WxUser;

@Service
public class WxUserServiceImpl implements WxUserService {
	
	@Autowired
	private WxUserMapper wxUserMapper;

	@Override
	public void insertWxUser(WxUser wxUser) {
		// TODO Auto-generated method stub
		wxUserMapper.insertWxUser(wxUser);
	}

	@Override
	public WxUser queryWxUser(String openId) {
		// TODO Auto-generated method stub
		return wxUserMapper.queryWxUser(openId);
	}
	
}
