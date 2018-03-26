package com.nfet.icare.dao;

import java.util.List;

import com.nfet.icare.pojo.DeviceInfo;

//解析Excel
public interface ParseExcelMapper {
	//保存设备信息
	public int insertDeviceInfo(List<DeviceInfo> deviceInfos);
}
