package com.nfet.icare.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.nfet.icare.pojo.Fix;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyPkg;

public interface OrderMapper {
	//获取用户报修列表
	public List<Fix> fixList(@Param("userNo") String userNo);
	//根据报修订单号获取报修详情
	public Fix fixDetail(@Param("fixId") String fixId);
	//获取用户延保列表
	public List<Warranty> warrantyList(@Param("userNo") String userNo);
	//获取用户延保套餐内容
	public WarrantyPkg warrantyPkg(@Param("warrantyNo") String warrantyNo);
	//获取用户上门服务卡内容
	public WarrantyPkg warrantyVisit(@Param("warrantyNo") String warrantyNo);
	//根据延保订单号获取延保详情
	public Warranty warrantyDetail(@Param("warrantyNo") String warrantyNo);
}
