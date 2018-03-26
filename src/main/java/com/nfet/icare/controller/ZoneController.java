package com.nfet.icare.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nfet.icare.pojo.Area;
import com.nfet.icare.pojo.City;
import com.nfet.icare.pojo.Province;
import com.nfet.icare.service.ZoneServiceImpl;

/**
 * 省市区三级联动
 * 1、省份列表
 * 2、城市列表
 * 3、地区列表
 * 
 */

@Controller
public class ZoneController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ZoneServiceImpl zoneServiceImpl;
	
	//获取省份列表
	@RequestMapping("/provinceList")
	@ResponseBody
	public List<Province> provinceList(){
		List<Province> provinceList = new ArrayList<>();
		provinceList = zoneServiceImpl.provinceList();
		logger.info("The number of province : " + provinceList.size());
		return provinceList;
	}
	
	//获取城市列表
	@RequestMapping("/cityList")
	@ResponseBody
	public List<City> cityList(@RequestParam(value="provinceId",required=true) int provinceId){
		List<City> cityLsit = new ArrayList<>();
		cityLsit = zoneServiceImpl.cityList(provinceId);
		logger.info("The number of city : " + cityLsit.size());
		return cityLsit;
	}
	
	//获取地区列表
	@RequestMapping("/areaList")
	@ResponseBody
	public List<Area> areaList(@RequestParam(value="cityId",required=true) int cityId){
		List<Area> areaLsit = new ArrayList<>();
		areaLsit = zoneServiceImpl.areaList(cityId);
		logger.info("The number of area : " + areaLsit.size());
		return areaLsit;
	}
}
