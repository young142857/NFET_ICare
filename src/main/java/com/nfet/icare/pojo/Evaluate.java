package com.nfet.icare.pojo;

//用户评价
public class Evaluate extends BaseBean {
	//评价编号
	private int evaluateNo;
	//报修单号
	private String fixNo;
	//设备评价
	private int evaluateDevice;
	//客服态度评价
	private int evaluateService;
	//维修工评价
	private int evaluateRepair;
	//评价描述
	private String evaluateDesc;
	//匿名评价
	private int evaluateAnonymous;
	
	public int getEvaluateNo() {
		return evaluateNo;
	}
	public void setEvaluateNo(int evaluateNo) {
		this.evaluateNo = evaluateNo;
	}
	public String getFixNo() {
		return fixNo;
	}
	public void setFixNo(String fixNo) {
		this.fixNo = fixNo;
	}
	public int getEvaluateDevice() {
		return evaluateDevice;
	}
	public void setEvaluateDevice(int evaluateDevice) {
		this.evaluateDevice = evaluateDevice;
	}
	public int getEvaluateService() {
		return evaluateService;
	}
	public void setEvaluateService(int evaluateService) {
		this.evaluateService = evaluateService;
	}
	public int getEvaluateRepair() {
		return evaluateRepair;
	}
	public void setEvaluateRepair(int evaluateRepair) {
		this.evaluateRepair = evaluateRepair;
	}
	public String getEvaluateDesc() {
		return evaluateDesc;
	}
	public void setEvaluateDesc(String evaluateDesc) {
		this.evaluateDesc = evaluateDesc;
	}
	public int getEvaluateAnonymous() {
		return evaluateAnonymous;
	}
	public void setEvaluateAnonymous(int evaluateAnonymous) {
		this.evaluateAnonymous = evaluateAnonymous;
	}	
}
