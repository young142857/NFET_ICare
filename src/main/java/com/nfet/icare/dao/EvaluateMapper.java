package com.nfet.icare.dao;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.Evaluate;

//用户评价
public interface EvaluateMapper {
	//获取最大评价编号
	public Integer getMaxEvaluateNo();
	//新增评价
	public void insertEvaluate(@Param("evaluate") Evaluate evaluate);
	//查询评价
	public Evaluate queryEvaluate(@Param("fixNo") String fixNo);
}
