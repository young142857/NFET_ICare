package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.DeviceMapper;
import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceColumn;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.Mgr_Device;

@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	private DeviceMapper deviceMapper;
	
	@Override
	public List<Device> deviceList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return deviceMapper.deviceList(map);
	}

	@Override
	public DeviceInfo existDevice(String deviceNo) {
		// TODO Auto-generated method stub
		return deviceMapper.existDevice(deviceNo);
	}

	@Override
	public Device bindOrNot(String deviceNo) {
		// TODO Auto-generated method stub
		return deviceMapper.bindOrNot(deviceNo);
	}

	@Override
	public void bindSingleDevice(Device device) {
		// TODO Auto-generated method stub
		deviceMapper.bindSingleDevice(device);
	}

	@Override
	public void updateDeviceStatus(Device device) {
		// TODO Auto-generated method stub
		deviceMapper.updateDeviceStatus(device);
	}

	@Override
	public void useTicket(Map<String, Object> map) {
		// TODO Auto-generated method stub
		deviceMapper.useTicket(map);
	}

	@Override
	public void updateStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		deviceMapper.updateStatus(map);
	}

	@Override
	public List<Mgr_Device> deviceData(String userNo) {
		// TODO Auto-generated method stub
		return deviceMapper.deviceData(userNo);
	}
	
	@Override
	public List<CacheInfo> getDeviceBindInfo() {
		// TODO Auto-generated method stub
		List<CacheInfo> deviceBindList = deviceMapper.getDeviceBindInfo();
		return deviceBindList;
	}

	@Override
	public DeviceColumn deviceColumn(String deviceType) {
		// TODO Auto-generated method stub
		return deviceMapper.deviceColumn(deviceType);
	}
	
	
}
