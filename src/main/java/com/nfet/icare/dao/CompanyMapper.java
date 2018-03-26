package com.nfet.icare.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.Mgr_Company;

public interface CompanyMapper {
	//判断公司是否存在
	public Company checkCompanyName(@Param("name") String name); 
	//插入公司信息
	public void insertCompany(@Param("company") Company company);
	//获取最大公司编号
	public Integer getMaxCompanyId();
	//完善公司信息
	public void updateCompany(@Param("company") Company company);
	//根据编号查找名称
	public String getCompanyName(@Param("companyId") int companyId);
	//根据编号查找公司信息
	public Company queryCompanyInfo(@Param("companyId") int companyId);
	
	//后端（PC端）
	//客户公司列表
	public List<Mgr_Company> companyList();
	
	//获取所有公司信息
	public List<CacheInfo> getAllCompany();
	//插入公司信息
    public void importCompany(@Param("company") Company company);
}
