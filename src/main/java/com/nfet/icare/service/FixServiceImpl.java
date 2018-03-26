package com.nfet.icare.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.FixMapper;
import com.nfet.icare.pojo.Branch;
import com.nfet.icare.pojo.DeviceCount;
import com.nfet.icare.pojo.FaultCount;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Mgr_Fix;

@Service
public class FixServiceImpl implements FixService {
	@Autowired
	private FixMapper fixMapper;

	@Override
	public Branch branchAddress(String cityId) {
		// TODO Auto-generated method stub
		return fixMapper.branchAddress(cityId);
	}

	@Override
	public void insertFix(Fix fix) {
		// TODO Auto-generated method stub
		fixMapper.insertFix(fix);
	}

	@Override
	public void updateFix(Map<String, Object> map) {
		// TODO Auto-generated method stub
		fixMapper.updateFix(map);
	}

	@Override
	public String fixInfo(String fixId) {
		// TODO Auto-generated method stub
		return fixMapper.fixInfo(fixId);
	}

	@Override
	public String queryFixId(String deviceNo) {
		// TODO Auto-generated method stub
		return fixMapper.queryFixId(deviceNo);
	}

	@Override
	public List<Fix> fixList() {
		// TODO Auto-generated method stub
		return fixMapper.fixList();
	}

	@Override
	public List<FaultCount> faultCounts() {
		// TODO Auto-generated method stub
		return fixMapper.faultCounts();
	}

	@Override
	public List<DeviceCount> deviceCounts() {
		// TODO Auto-generated method stub
		return fixMapper.deviceCounts();
	}

	@Override
	public int sevenCounts(String date) {
		// TODO Auto-generated method stub
		return fixMapper.sevenCounts(date);
	}

	@Override
	public List<Mgr_Fix> deviceFixes(String deviceNo) {
		// TODO Auto-generated method stub
		return fixMapper.deviceFixes(deviceNo);
	}

	@Override
	public List<Mgr_Fix> allFixOrder() {
		// TODO Auto-generated method stub
		return fixMapper.allFixOrder();
	}

}
