package com.nfet.icare.service;


import java.util.List;

import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.Mgr_Company;

public interface CompanyService {
	//判断公司是否存在
	public Company checkCompanyName(String name); 
	//插入公司信息
	public void insertCompany(Company company);
	//获取最大公司编号
	public Integer getMaxCompanyId();
	//完善公司信息
	public void updateCompany(Company company);
	//根据编号查找名称
	public String getCompanyName(int companyId);
	//根据编号查找公司信息
	public Company queryCompanyInfo(int companyId);

	// 获取所有公司信息
	public List<CacheInfo> getAllCompany();

	// excel导入公司信息
	public void importCompany(Company company);

	// 后端（PC端）
	// 所有公司列表
	public List<Mgr_Company> companyList();
}
