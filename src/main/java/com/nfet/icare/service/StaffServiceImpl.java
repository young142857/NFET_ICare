package com.nfet.icare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.StaffMapper;
import com.nfet.icare.pojo.Staff;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffMapper staffMapper;
	
	@Override
	public Staff queryStaff(String staffNo) {
		// TODO Auto-generated method stub
		return staffMapper.queryStaff(staffNo);
	}
	
}
