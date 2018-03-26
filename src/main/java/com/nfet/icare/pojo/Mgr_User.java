package com.nfet.icare.pojo;

//用户基本信息
public class Mgr_User extends BaseBean {
	//序号
	private int num;
	//用户编号
	private String userNo;
	//用户姓名
	private String name;
	//用户手机
	private String phone;
	//所在公司
	private Mgr_Company company;
	//微信名
	private String wxName;
	//微信图像
	private String wxImg;
	//积分
	private int score;
	//注册时间
	private String regTime;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Mgr_Company getCompany() {
		return company;
	}
	public void setCompany(Mgr_Company company) {
		this.company = company;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public String getWxImg() {
		return wxImg;
	}
	public void setWxImg(String wxImg) {
		this.wxImg = wxImg;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
}
