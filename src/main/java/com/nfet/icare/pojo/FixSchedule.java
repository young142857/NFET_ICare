package com.nfet.icare.pojo;

public class FixSchedule {
	private String time;
	private String msg;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "FixSchedule [time=" + time + ", msg=" + msg + "]";
	}	
}
