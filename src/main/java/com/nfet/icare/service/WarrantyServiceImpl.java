package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.WarrantyMapper;
import com.nfet.icare.pojo.Mgr_Warranty;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyCount;
import com.nfet.icare.pojo.WarrantyPkg;

@Service
public class WarrantyServiceImpl implements WarrantyService {
	@Autowired
	private WarrantyMapper warrantyMapper;

	@Override
	public List<WarrantyPkg> warrantyPkgList() {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyPkgList();
	}

	@Override
	public List<WarrantyPkg> warrantyVisitList() {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyVisitList();
	}

	@Override
	public WarrantyPkg getWarrantyNo(String warrantyContent) {
		// TODO Auto-generated method stub
		return warrantyMapper.getWarrantyNo(warrantyContent);
	}

	@Override
	public void insertWarranty(Warranty warranty) {
		// TODO Auto-generated method stub
		warrantyMapper.insertWarranty(warranty);
	}

	@Override
	public Warranty queryWarranty(String warrantyNo) {
		// TODO Auto-generated method stub
		return warrantyMapper.queryWarranty(warrantyNo);
	}

	@Override
	public void updateDeviceInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		warrantyMapper.updateDeviceInfo(map);
	}

	@Override
	public List<Warranty> warrantyRecord(String deviceNo) {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyRecord(deviceNo);
	}

	@Override
	public void updatePayStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		warrantyMapper.updatePayStatus(map);;
	}

	@Override
	public List<Warranty> visitRecord(String deviceNo) {
		// TODO Auto-generated method stub
		return warrantyMapper.visitRecord(deviceNo);
	}

	@Override
	public List<Mgr_Warranty> warrantyOrder() {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyOrder();
	}

	@Override
	public List<Mgr_Warranty> visitOrder() {
		// TODO Auto-generated method stub
		return warrantyMapper.visitOrder();
	}

	@Override
	public String sumIncome() {
		// TODO Auto-generated method stub
		return warrantyMapper.sumIncome();
	}

	@Override
	public List<WarrantyCount> warrantyCount() {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyCount();
	}

	@Override
	public List<WarrantyCount> visitCount() {
		// TODO Auto-generated method stub
		return warrantyMapper.visitCount();
	}

	@Override
	public String warrantyPayAmts(String typeName) {
		// TODO Auto-generated method stub
		return warrantyMapper.warrantyPayAmts(typeName);
	}

	@Override
	public String visitPayAmts(String typeName) {
		// TODO Auto-generated method stub
		return warrantyMapper.visitPayAmts(typeName);
	}

	@Override
	public List<Mgr_Warranty> mgrWarrantyRecord(String deviceNo) {
		// TODO Auto-generated method stub
		return warrantyMapper.mgrWarrantyRecord(deviceNo);
	}

	@Override
	public List<Mgr_Warranty> mgrVisitRecord(String deviceNo) {
		// TODO Auto-generated method stub
		return warrantyMapper.mgrVisitRecord(deviceNo);
	}

	@Override
	public List<Mgr_Warranty> allWarrantyOrder() {
		// TODO Auto-generated method stub
		return warrantyMapper.allWarrantyOrder();
	}

	@Override
	public List<Mgr_Warranty> allVisitOrder() {
		// TODO Auto-generated method stub
		return warrantyMapper.allVisitOrder();
	}

	@Override
	public List<WarrantyPkg> newWarrantyList(int warrantyType) {
		// TODO Auto-generated method stub
		return warrantyMapper.newWarrantyList(warrantyType);
	}
}
