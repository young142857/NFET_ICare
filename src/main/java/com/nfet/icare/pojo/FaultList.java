package com.nfet.icare.pojo;

//故障列表
public class FaultList extends BaseBean {
	//故障编号
	private String type_no;
	//故障类型
	private String type_name;
	
	public String getType_no() {
		return type_no;
	}
	public void setType_no(String type_no) {
		this.type_no = type_no;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	@Override
	public String toString() {
		return "FaultList [type_no=" + type_no + ", type_name=" + type_name + "]";
	}
	
}
