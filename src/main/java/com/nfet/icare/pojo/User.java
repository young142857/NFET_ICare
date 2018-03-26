package com.nfet.icare.pojo;

//用户基本信息
public class User extends BaseBean {
	//用户编号
	private String userNo;
	//用户姓名
	private String name;
	//用户手机
	private String phone;
	//所在公司id
	private int companyId;
	//微信名
	private String wxName;
	//微信图像
	private String wxImg;
	//积分
	private int score;
	//赠送积分
	private int giveScore;
	
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
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
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
	public int getGiveScore() {
		return giveScore;
	}
	public void setGiveScore(int giveScore) {
		this.giveScore = giveScore;
	}
	@Override
	public String toString() {
		return "User [userNo=" + userNo + ", name=" + name + ", phone=" + phone + ", companyId=" + companyId
				+ ", wxName=" + wxName + ", wxImg=" + wxImg + ", score=" + score + "]";
	}
	
	
}
