package com.nfet.icare.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.Area;
import com.nfet.icare.pojo.City;
import com.nfet.icare.pojo.Province;

public interface ZoneMapper {
	//获取所有省份
	public List<Province> provinceList();
	//根据省份id获取城市
	public List<City> cityList(@Param("provinceId") int provinceId);
	//根据城市id获取地区
	public List<Area> areaList(@Param("cityId") int cityId);
	//根据省份id获取省份名称
	public String queryProvinceName(@Param("provinceId") int provinceId);
	//根据城市id获取城市名称
	public String quertCityName(@Param("cityId") int cityId);
	//根据地区id获取地区名称
	public String queryAreaName(@Param("areaId") int areaId);
}
