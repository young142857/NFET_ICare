package com.nfet.icare.pojo;

//公司产品信息
public class DeviceInfo extends BaseBean {
	//机器编码
	private String deviceNo;
	//产品名称
	private String name;
	//产品系列
	private String type;
	//产品机型
	private String model;
	//产品图片地址
	private String image;
	//产品生产日期
	private String mfgDate;
	
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMfgDate() {
		return mfgDate;
	}
	public void setMfgDate(String mfgDate) {
		this.mfgDate = mfgDate;
	}	
}
