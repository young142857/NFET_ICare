package com.nfet.icare.service;

import com.nfet.icare.pojo.Evaluate;

public interface EvaluateService {
	//获取最大评价编号
	public Integer getMaxEvaluateNo();
	//新增评价
	public void insertEvaluate(Evaluate evaluate);
	//查询评价
	public Evaluate queryEvaluate(String fixNo);
}
