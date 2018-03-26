package com.nfet.icare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.OrderMapper;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyPkg;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Fix> fixList(String userNo) {
		// TODO Auto-generated method stub
		return orderMapper.fixList(userNo);
	}

	@Override
	public Fix fixDetail(String fixId) {
		// TODO Auto-generated method stub
		return orderMapper.fixDetail(fixId);
	}

	@Override
	public List<Warranty> warrantyList(String userNo) {
		// TODO Auto-generated method stub
		return orderMapper.warrantyList(userNo);
	}

	@Override
	public Warranty warrantyDetail(String warrantyNo) {
		// TODO Auto-generated method stub
		return orderMapper.warrantyDetail(warrantyNo);
	}


	@Override
	public WarrantyPkg warrantyPkg(String warrantyNo) {
		// TODO Auto-generated method stub
		return orderMapper.warrantyPkg(warrantyNo);
	}

	@Override
	public WarrantyPkg warrantyVisit(String warrantyNo) {
		// TODO Auto-generated method stub
		return orderMapper.warrantyVisit(warrantyNo);
	}
	
}
