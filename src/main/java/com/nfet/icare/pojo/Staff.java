package com.nfet.icare.pojo;

public class Staff extends BaseBean {
	private String staffNo;
	private String password;
	private String staffName;
	private String staffImage;
	private Role role;
	
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffImage() {
		return staffImage;
	}
	public void setStaffImage(String staffImage) {
		this.staffImage = staffImage;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
