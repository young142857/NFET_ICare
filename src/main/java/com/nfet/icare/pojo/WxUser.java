package com.nfet.icare.pojo;

//存储微信用户的openId和phone
public class WxUser extends BaseBean {
	private String openId;
	private String phone;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
