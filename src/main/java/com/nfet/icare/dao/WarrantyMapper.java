package com.nfet.icare.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.Mgr_Warranty;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyCount;
import com.nfet.icare.pojo.WarrantyPkg;

public interface WarrantyMapper {
	//设备可选择的套餐列表
	public List<WarrantyPkg> warrantyPkgList(); 
	//上门服务卡列表
	public List<WarrantyPkg> warrantyVisitList(); 
	//根据套餐内容获取套餐
	public WarrantyPkg getWarrantyNo(@Param("warrantyContent") String warrantyContent);
	//增加延保记录
	public void insertWarranty(@Param("warranty") Warranty warranty);
	//根据延保订单号查询订单信息
	public Warranty queryWarranty(@Param("warrantyNo") String warrantyNo);
	//将延保订单置为已支付
	public void updatePayStatus(@Param("map") Map<String, Object> map);
	//更新设备信息(此次延保时长，延保结束，上门截止)
	public void updateDeviceInfo(@Param("map") Map<String, Object> map);
	//根据机器编码查询延保记录
	public List<Warranty> warrantyRecord(@Param("deviceNo") String deviceNo);
	//根据机器编码查询上门卡记录
	public List<Warranty> visitRecord(@Param("deviceNo") String deviceNo);
	
	//添加更多设备信息
	//最新的延保套餐列表
	public List<WarrantyPkg> newWarrantyList(@Param("warrantyType") int warrantyType);
	
	//后端（PC端）
	//所有延保订单
	public List<Mgr_Warranty> warrantyOrder();
	//所有上门订单
	public List<Mgr_Warranty> visitOrder();
	//延保总收入
	public String sumIncome();
	//延保套餐数量
	public List<WarrantyCount> warrantyCount();
	//上门套餐数量
	public List<WarrantyCount> visitCount();
	//某延保套餐收入
	public String warrantyPayAmts(@Param("typeName") String typeName);
	//某上门套餐收入
	public String visitPayAmts(@Param("typeName") String typeName);
	//根据机器编码查询所有延保记录
	public List<Mgr_Warranty> mgrWarrantyRecord(@Param("deviceNo") String deviceNo);
	//根据机器编码查询所有上门记录
	public List<Mgr_Warranty> mgrVisitRecord(@Param("deviceNo") String deviceNo);
	//客户延保订单
	public List<Mgr_Warranty> allWarrantyOrder();
	//客户上门订单
	public List<Mgr_Warranty> allVisitOrder();
}
