package com.nfet.icare.service;

import java.util.List;

import com.nfet.icare.pojo.Area;
import com.nfet.icare.pojo.City;
import com.nfet.icare.pojo.Province;

public interface ZoneService {
	//获取所有省份
	public List<Province> provinceList();
	//根据省份id获取城市
	public List<City> cityList(int provinceId);
	//根据城市id获取地区	
	public List<Area> areaList(int cityId);
	//根据省份id获取省份名称
	public String queryProvinceName(int provinceId);
	//根据城市id获取城市名称
	public String quertCityName(int cityId);
	//根据地区id获取地区名称
	public String queryAreaName(int areaId);
}
