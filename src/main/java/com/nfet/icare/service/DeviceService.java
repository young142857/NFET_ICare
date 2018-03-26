package com.nfet.icare.service;

import java.util.List;
import java.util.Map;
import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceColumn;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.Mgr_Device;

public interface DeviceService {
	//查询用户设备
	public List<Device> deviceList(Map<String, String> map);
	//查询设备是否存在
	public DeviceInfo existDevice(String deviceNo);
	//查询设备是否已注册
	public Device bindOrNot(String deviceNo);
	//用户注册设备
	public void bindSingleDevice(Device device);
	//更改设备保修状态
	public void updateDeviceStatus(Device device);
	//更改设备状态
	public void updateStatus(Map<String, Object> map);
	//使用延保券
	public void useTicket(Map<String, Object> map);
	//获取设备号及设备绑定信息
	public List<CacheInfo> getDeviceBindInfo();
	
	//添加更多设备信息
	//根据设备型号查询设备行数
	public DeviceColumn deviceColumn(String deviceType);
	
	//后端(PC端)
	//用户的注册设备
	public List<Mgr_Device> deviceData(String userNo);
}
