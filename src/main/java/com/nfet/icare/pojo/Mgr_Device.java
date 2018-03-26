package com.nfet.icare.pojo;

import java.sql.Timestamp;

//设备
public class Mgr_Device extends BaseBean {
	//序号
	private int num;
	//用户编号
	private String userNo;
	//设备信息
	private DeviceInfo deviceNo;
	//注册时间
	private Timestamp bindTime;
	//注册时间(String)
	private String bindTimeStr;
	//保修卡号
	private String ecardNo;
	//保修卡号(纯数字)
	private String ecardNum;
	//上次保修期限
	private int warrantPeriod;
	//保修开始时间
	private String warrantFrom;
	//保修截至时间
	private String warrantTo;
	//上门截止时间
	private String visitTo;
	//保修状态(1:保内 2:保外)
	private int warrantStatus;
	//设备状态(0:正常 1:报修 2:维修中)
	private int status;
	//保修状态(str)
	private String statusStr;
	//保修时长
	private String warrantDate;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}	
	public DeviceInfo getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(DeviceInfo deviceNo) {
		this.deviceNo = deviceNo;
	}
	public Timestamp getBindTime() {
		return bindTime;
	}
	public void setBindTime(Timestamp bindTime) {
		this.bindTime = bindTime;
	}
	public String getBindTimeStr() {
		return bindTimeStr;
	}
	public void setBindTimeStr(String bindTimeStr) {
		this.bindTimeStr = bindTimeStr;
	}
	public String getEcardNo() {
		return ecardNo;
	}
	public void setEcardNo(String ecardNo) {
		this.ecardNo = ecardNo;
	}
	public String getEcardNum() {
		return ecardNum;
	}
	public void setEcardNum(String ecardNum) {
		this.ecardNum = ecardNum;
	}
	public int getWarrantPeriod() {
		return warrantPeriod;
	}
	public void setWarrantPeriod(int warrantPeriod) {
		this.warrantPeriod = warrantPeriod;
	}
	public String getWarrantFrom() {
		return warrantFrom;
	}
	public void setWarrantFrom(String warrantFrom) {
		this.warrantFrom = warrantFrom;
	}
	public String getWarrantTo() {
		return warrantTo;
	}
	public void setWarrantTo(String warrantTo) {
		this.warrantTo = warrantTo;
	}
	public String getVisitTo() {
		return visitTo;
	}
	public void setVisitTo(String visitTo) {
		this.visitTo = visitTo;
	}
	public int getWarrantStatus() {
		return warrantStatus;
	}
	public void setWarrantStatus(int warrantStatus) {
		this.warrantStatus = warrantStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getWarrantDate() {
		return warrantDate;
	}
	public void setWarrantDate(String warrantDate) {
		this.warrantDate = warrantDate;
	}
}

