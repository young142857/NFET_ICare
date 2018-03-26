package com.nfet.icare.dao;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.Staff;

public interface StaffMapper {
	//根据工号查询员工
	public Staff queryStaff(@Param("staffNo") String staffNo);
}
