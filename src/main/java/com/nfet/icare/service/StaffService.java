package com.nfet.icare.service;

import com.nfet.icare.pojo.Staff;

public interface StaffService {
	//根据工号查询员工
	public Staff queryStaff(String staffNo);
}
