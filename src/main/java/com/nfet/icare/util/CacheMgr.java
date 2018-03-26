package com.nfet.icare.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.nfet.icare.pojo.CacheInfo;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.UserServiceImpl;

/**
 * 缓存管理
 * 
 * @author zhoupx
 *
 */
@Component
public class CacheMgr {
	private static Logger logger = LoggerFactory.getLogger(CacheMgr.class);

	private static CacheMgr cachemgr;

	private static Map<String, String> deviceCache;

	private static Map<String, String> phoneCache;
	
	private static Map<String, String> companyCache;
	
	//private static volatile int maxCompanyId;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	

	public void setCompanyServiceImpl(CompanyServiceImpl companyServiceImpl) {
		this.companyServiceImpl = companyServiceImpl;
	}

	public void setDeviceServiceImpl(DeviceServiceImpl deviceServiceImpl) {
		this.deviceServiceImpl = deviceServiceImpl;
	}

	public void setUserServiceImpl(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@PostConstruct
	public void init() {
		cachemgr = this;
		cachemgr.userServiceImpl = this.userServiceImpl;
		cachemgr.deviceServiceImpl = this.deviceServiceImpl;
		cachemgr.companyServiceImpl = this.companyServiceImpl;
		
	}

	private CacheMgr() {
		if (deviceCache == null) {
			logger.info("创建devicecache");
			deviceCache = new ConcurrentHashMap<String, String>();
		}

		if (phoneCache == null) {
			logger.info("创建phoneCache");
			phoneCache = new ConcurrentHashMap<String, String>();
		}
		
		if (companyCache == null) {
			logger.info("创建companyCache");
			companyCache = new ConcurrentHashMap<String, String>();
		}

	}

	public static Map<String, String> getDeviceCache() {
		return deviceCache;
	}

	public static Map<String, String> getCompanyCache() {
		return companyCache;
	}

	public static Map<String, String> getPhoneCache() {
		return phoneCache;
	}

	public static CacheMgr getInstance() {
		logger.info("getInstance实例化配置");
		if (cachemgr == null) {
			synchronized (CacheMgr.class) {
				if (cachemgr == null) {
					cachemgr = new CacheMgr();
				}
			}
		}
		return cachemgr;

	}
	
	
	public void putUser(String key, String value) {
		phoneCache.put(key, value);
	}

	public String isExistPhone(String key) {
		if (phoneCache.containsKey(key)) {
			logger.info("存在手机号:" + key +"，对应的userNo:" + phoneCache.get(key));
			return phoneCache.get(key);
		}
		return null;
	}
	
	
	public void putDevice(String key, String value) {
		
		deviceCache.put(key, value);
	}

	//设备存在且未绑定 
	public Boolean isExistDevice(String key) {
		if (deviceCache.containsKey(key)&&!"".equals(deviceCache.get(key))) {
			return true;
		}
		//这里返回true指设备不可用
		else if(!deviceCache.containsKey(key))
			return true;
		return false;
	}
	
	public void putCompany(String key, String value) {
		companyCache.put(key, value);
	}

	public int isExistCompany(String key) {
		if (companyCache.containsKey(key)) {
			return Integer.parseInt(companyCache.get(key));
		}
		return 0;
	}

	public void LoadCache() {
		logger.info("开始加载全部手机号");
		List<CacheInfo> phoneList = cachemgr.userServiceImpl.getAllPhones();
		logger.info("全部手机号===》" + phoneList.toString());

		for (CacheInfo temp : phoneList) {
			phoneCache.put(temp.getKey(), temp.getValue());
		}

		
		logger.info("开始加载全部绑定的设备");
		List<CacheInfo> deviceBindList = cachemgr.deviceServiceImpl.getDeviceBindInfo();
		for (CacheInfo temp : deviceBindList) {
			if (temp.getValue() == null)
				deviceCache.put(temp.getKey(), "");
			else
				deviceCache.put(temp.getKey(), temp.getValue());

		}
		logger.info("全部设备信息===》" + deviceCache.toString());
		
		logger.info("开始加载公司信息");
		List<CacheInfo> companyList = cachemgr.companyServiceImpl.getAllCompany();
		for (CacheInfo temp : companyList) {
			companyCache.put(temp.getKey(), temp.getValue());
		}
		//maxCompanyId = companyServiceImpl.getMaxCompanyId();
		logger.info("全部公司信息===》" + companyCache.toString());
		

	}


}
