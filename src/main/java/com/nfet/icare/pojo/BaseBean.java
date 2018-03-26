package com.nfet.icare.pojo;

import java.sql.Timestamp;

public abstract class BaseBean {
	//主键id
	private String uid;
	//删除标志
	private int deleteFlag;
	//创建时间
	private Timestamp createTime;
	//最后修改时间
	private Timestamp lastModifyTime;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	
}
