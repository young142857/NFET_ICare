package com.nfet.icare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.EvaluateMapper;
import com.nfet.icare.pojo.Evaluate;

@Service
public class EvaluateServiceImpl implements EvaluateService {
	
	@Autowired
	private EvaluateMapper evaluateMapper;

	@Override
	public void insertEvaluate(Evaluate evaluate) {
		// TODO Auto-generated method stub
		evaluateMapper.insertEvaluate(evaluate);
	}

	@Override
	public Evaluate queryEvaluate(String fixNo) {
		// TODO Auto-generated method stub
		return evaluateMapper.queryEvaluate(fixNo);
	}

	@Override
	public Integer getMaxEvaluateNo() {
		// TODO Auto-generated method stub
		return evaluateMapper.getMaxEvaluateNo();
	}
}
