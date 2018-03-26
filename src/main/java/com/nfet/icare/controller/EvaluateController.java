package com.nfet.icare.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.Evaluate;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.EvaluateServiceImpl;
import com.nfet.icare.util.GlobalConstants;
import com.nfet.icare.util.GlobalMethods;
/**
 * 用户评价功能
 * 1、跳转至评价
 * 2、新增评价 
 *  
 */
@Controller
public class EvaluateController {
	private  Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private EvaluateServiceImpl evaluateServiceImpl;
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${xxtUrl}")
	private String xxtUrl;	
	
	//跳转至评价
	@RequestMapping("/skipToEvaluate")
	@ResponseBody
	public ModelAndView skipToEvaluate(@RequestParam(value="fixNo",required=true) String fixNo,
			@RequestParam(value="deviceNo",required=true) String deviceNo){
		ModelAndView mav = new ModelAndView("estimate");
		Device device = deviceServiceImpl.bindOrNot(deviceNo);
		mav.addObject("fixNo", fixNo);
		mav.addObject("device", device);
		
		return mav;
	}
	
	//新增评价
	@RequestMapping("/insertEvaluate")
	@ResponseBody
	public String insertEvaluate(@RequestParam(value="fixNo",required=true) String fixNo,
			@RequestParam(value="evaluateDevice",required=true) int evaluateDevice,
			@RequestParam(value="evaluateService",required=true) int evaluateService,
			@RequestParam(value="evaluateRepair",required=true) int evaluateRepair,
			@RequestParam(value="evaluateDesc",required=false) String evaluateDesc,
			@RequestParam(value="evaluateAnonymous",required=true) int evaluateAnonymous){
		Evaluate evaluate = new Evaluate();
		Map<String, Object> evaluateMap = new HashMap<>();
		//调用信讯通接口
		evaluateMap.put("fix_no", fixNo);
		evaluateMap.put("score", evaluateDevice);
		evaluateMap.put("comment", evaluateDesc);
		String json = GlobalMethods.parseString(evaluateMap);
		restTemplate.getForObject(xxtUrl+"fixComment/{json}", String.class,json);
		
		//只能评价一次
		if (evaluateServiceImpl.queryEvaluate(fixNo) == null) {
			//评价编号
			if (evaluateServiceImpl.getMaxEvaluateNo() == null) {
				logger.info("the maxEvaluateNo is null");
				//数据库当前没有数据
				evaluate.setEvaluateNo(1);
			}
			else {
				logger.info("the maxEvaluateNo is not null");
				evaluate.setEvaluateNo(evaluateServiceImpl.getMaxEvaluateNo()+1);
			}
			evaluate.setFixNo(fixNo);
			evaluate.setEvaluateDevice(evaluateDevice);
			evaluate.setEvaluateService(evaluateService);
			evaluate.setEvaluateRepair(evaluateRepair);
			evaluate.setEvaluateDesc(evaluateDesc);
			evaluate.setEvaluateAnonymous(evaluateAnonymous);
			evaluate.setCreateTime(new Timestamp(System.currentTimeMillis()));
			evaluate.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			evaluateServiceImpl.insertEvaluate(evaluate);
			logger.info("save the data into data_evaluate");
			
			return GlobalConstants.ERROR_CODE_SUCCESS;
		}
		else {
			return GlobalConstants.ERROR_CODE_FAIL;
		}		
	}
	
}
