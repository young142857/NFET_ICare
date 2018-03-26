package com.nfet.icare.pojo;

import java.sql.Timestamp;

//报修表
public class Mgr_Fix extends BaseBean {
	//序号
	private int num;
	//报修单号
	private String fixId;
	//用户编号
	private String userNo;
	//用户姓名
	private String userName;
	//用户手机号
	private String userPhone;
	//用户公司名称
	private String companyName;
	//用户公司电话
	private String companyPhone;
	//用户公司行业
	private String companyIndustry;
	//设备信息
	private DeviceInfo deviceNo;
	//故障类型
	private String faultName;
	//故障描述
	private String fixDesc;
	//维修方式(1:上门 2:送修)
	private int fixPattern;
	//维修方式(str)
	private String fixPatternStr;
	//维修地址所属省份
	private Province province;
	//维修地址所属城市
	private City city;
	//维修地址所属地区
	private Area area;
	//维修区域
	private String zone;
	//详细维修地址
	private String address;
	//维修进度(1:等待客服确认 2:等待维修人员上门 3:维修中 4:维修完成)
	private int fixSchedule;
	//维修进度(str)
	private String fixScheduleStr;
	//下单时间
	private Timestamp orderTime;
	//下单时间(String)
	private String orderTimeStr;
	//评价信息
	private String evaluate;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getFixId() {
		return fixId;
	}
	public void setFixId(String fixId) {
		this.fixId = fixId;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getCompanyIndustry() {
		return companyIndustry;
	}
	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}
	public DeviceInfo getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(DeviceInfo deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getFaultName() {
		return faultName;
	}
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}
	public String getFixDesc() {
		return fixDesc;
	}
	public void setFixDesc(String fixDesc) {
		this.fixDesc = fixDesc;
	}
	public int getFixPattern() {
		return fixPattern;
	}
	public void setFixPattern(int fixPattern) {
		this.fixPattern = fixPattern;
	}
	public String getFixPatternStr() {
		return fixPatternStr;
	}
	public void setFixPatternStr(String fixPatternStr) {
		this.fixPatternStr = fixPatternStr;
	}
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getFixSchedule() {
		return fixSchedule;
	}
	public void setFixSchedule(int fixSchedule) {
		this.fixSchedule = fixSchedule;
	}
	public String getFixScheduleStr() {
		return fixScheduleStr;
	}
	public void setFixScheduleStr(String fixScheduleStr) {
		this.fixScheduleStr = fixScheduleStr;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public String getOrderTimeStr() {
		return orderTimeStr;
	}
	public void setOrderTimeStr(String orderTimeStr) {
		this.orderTimeStr = orderTimeStr;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
}
