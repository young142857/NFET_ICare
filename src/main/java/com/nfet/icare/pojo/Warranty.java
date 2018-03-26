package com.nfet.icare.pojo;

import java.sql.Timestamp;

//延保表
public class Warranty extends BaseBean {
	//延保单号
	private String warrantyNo;
	//用户编号
	private User userNo;
	//设备编号
	private DeviceInfo deviceNo;
	//延保方案编号
	private WarrantyPkg warrantyPkgNo;
	//上门方案编号
	private WarrantyPkg warrantyVisitNo;
	//支付金额
	private String payAmt;
	//支付备注
	private String payMsg;
	//支付状态(0:已支付  1:未支付)
	private int payStatus;
	//延保开始日期
	private String warrantyFrom;
	//延保截止日期
	private String warrantyTo;
	//上门开始日期
	private String visitFrom;
	//上门截止日期
	private String visitTo;
	//下单时间
	private Timestamp orderTime;
	//下单时间(String)
	private String orderTimeStr;
	
	public String getWarrantyNo() {
		return warrantyNo;
	}
	public void setWarrantyNo(String warrantyNo) {
		this.warrantyNo = warrantyNo;
	}
	public User getUserNo() {
		return userNo;
	}
	public void setUserNo(User userNo) {
		this.userNo = userNo;
	}
	public DeviceInfo getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(DeviceInfo deviceNo) {
		this.deviceNo = deviceNo;
	}
	public WarrantyPkg getWarrantyPkgNo() {
		return warrantyPkgNo;
	}
	public void setWarrantyPkgNo(WarrantyPkg warrantyPkgNo) {
		this.warrantyPkgNo = warrantyPkgNo;
	}
	public WarrantyPkg getWarrantyVisitNo() {
		return warrantyVisitNo;
	}
	public void setWarrantyVisitNo(WarrantyPkg warrantyVisitNo) {
		this.warrantyVisitNo = warrantyVisitNo;
	}
	public String getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}
	public String getPayMsg() {
		return payMsg;
	}
	public void setPayMsg(String payMsg) {
		this.payMsg = payMsg;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public String getWarrantyFrom() {
		return warrantyFrom;
	}
	public void setWarrantyFrom(String warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}
	public String getWarrantyTo() {
		return warrantyTo;
	}
	public void setWarrantyTo(String warrantyTo) {
		this.warrantyTo = warrantyTo;
	}
	public String getVisitFrom() {
		return visitFrom;
	}
	public void setVisitFrom(String visitFrom) {
		this.visitFrom = visitFrom;
	}
	public String getVisitTo() {
		return visitTo;
	}
	public void setVisitTo(String visitTo) {
		this.visitTo = visitTo;
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
	@Override
	public String toString() {
		return "Warranty [warrantyNo=" + warrantyNo + ", userNo=" + userNo + ", deviceNo=" + deviceNo
				+ ", warrantyPkgNo=" + warrantyPkgNo + ", warrantyVisitNo=" + warrantyVisitNo + ", payAmt=" + payAmt
				+ ", payMsg=" + payMsg + ", payStatus=" + payStatus + ", warrantyFrom=" + warrantyFrom + ", warrantyTo="
				+ warrantyTo + ", visitTo=" + visitTo + ", orderTime=" + orderTime + "]";
	}
	
}
