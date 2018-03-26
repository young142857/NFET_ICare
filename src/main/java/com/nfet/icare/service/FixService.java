package com.nfet.icare.service;


import java.util.List;
import java.util.Map;

import com.nfet.icare.pojo.Branch;
import com.nfet.icare.pojo.DeviceCount;
import com.nfet.icare.pojo.FaultCount;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Mgr_Fix;

public interface FixService {
	//获取送修分公司地址
	public Branch branchAddress(String cityId);
	//增加报修记录
	public void insertFix(Fix fix);
	//更新报修进度
	public void updateFix(Map<String, Object> map);
	//获取报修信息
	public String fixInfo(String fixId);
	//根据设备编号查询报修单号
	public String queryFixId(String deviceNo);
	
	//后端（PC端）
	//报修统计
	public List<Fix> fixList();
	//故障类型数量
	public List<FaultCount> faultCounts();
	//机型数量
	public List<DeviceCount> deviceCounts();
	//七日前报修数量
	public int sevenCounts(String date);
	//设备报修订单
	public List<Mgr_Fix> deviceFixes(String deviceNo);
	//客户报修订单
	public List<Mgr_Fix> allFixOrder();
}
