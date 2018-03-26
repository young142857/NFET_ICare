package com.nfet.icare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.ZoneMapper;
import com.nfet.icare.pojo.Area;
import com.nfet.icare.pojo.City;
import com.nfet.icare.pojo.Province;

@Service
public class ZoneServiceImpl implements ZoneService {
	@Autowired
	private ZoneMapper ZoneMapper;
	
	@Override
	public List<Province> provinceList() {
		// TODO Auto-generated method stub
		return ZoneMapper.provinceList();
	}

	@Override
	public List<City> cityList(int provinceId) {
		// TODO Auto-generated method stub
		return ZoneMapper.cityList(provinceId);
	}

	@Override
	public List<Area> areaList(int cityId) {
		// TODO Auto-generated method stub
		return ZoneMapper.areaList(cityId);
	}

	@Override
	public String queryProvinceName(int provinceId) {
		// TODO Auto-generated method stub
		return ZoneMapper.queryProvinceName(provinceId);
	}

	@Override
	public String quertCityName(int cityId) {
		// TODO Auto-generated method stub
		return ZoneMapper.quertCityName(cityId);
	}

	@Override
	public String queryAreaName(int areaId) {
		// TODO Auto-generated method stub
		return ZoneMapper.queryAreaName(areaId);
	}
	
}
