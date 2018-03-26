package com.nfet.icare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.ParseExcelMapper;
import com.nfet.icare.pojo.DeviceInfo;

@Service
public class ParseExcelServiceImpl implements ParseExcelService {
	
	@Autowired
	private ParseExcelMapper parseExcelMapper;

	@Override
	public int insertDeviceInfo(List<DeviceInfo> deviceInfos) {
		// TODO Auto-generated method stub
		return parseExcelMapper.insertDeviceInfo(deviceInfos);
	}
}
