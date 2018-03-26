package com.nfet.icare.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.CompanyMapper;
import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.Mgr_Company;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public Company checkCompanyName(String name) {
		// TODO Auto-generated method stub
		return companyMapper.checkCompanyName(name);
	}

	@Override
	public void insertCompany(Company company) {
		// TODO Auto-generated method stub
		companyMapper.insertCompany(company);
	}

	@Override
	public Integer getMaxCompanyId() {
		// TODO Auto-generated method stub
		return companyMapper.getMaxCompanyId();
	}

	@Override
	public void updateCompany(Company company) {
		// TODO Auto-generated method stub
		companyMapper.updateCompany(company);
	}

	@Override
	public String getCompanyName(int companyId) {
		// TODO Auto-generated method stub
		return companyMapper.getCompanyName(companyId);
	}

	@Override
	public Company queryCompanyInfo(int companyId) {
		// TODO Auto-generated method stub
		return companyMapper.queryCompanyInfo(companyId);
	}

	@Override
	public List<Mgr_Company> companyList() {
		// TODO Auto-generated method stub
		return companyMapper.companyList();
	}
	
	@Override
	public List<CacheInfo> getAllCompany() {
		// TODO Auto-generated method stub
		List<CacheInfo> companyList = new ArrayList<CacheInfo>();
		companyList = companyMapper.getAllCompany();
		return companyList;
	}

	@Override
	public void importCompany(Company company) {
		// TODO Auto-generated method stub
		companyMapper.importCompany(company);
	}

	
	
}
