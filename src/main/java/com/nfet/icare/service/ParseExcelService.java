package com.nfet.icare.service;

import java.util.List;

import com.nfet.icare.pojo.DeviceInfo;

//解析Excel
public interface ParseExcelService {
	//保存设备信息
	public int insertDeviceInfo(List<DeviceInfo> deviceInfos);
}
