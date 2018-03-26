package com.nfet.icare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nfet.icare.util.CacheMgr;

@Component
public class Startload implements CommandLineRunner{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		
        logger.info("项目启动...数据加载中");
		CacheMgr cacheMgr = CacheMgr.getInstance();
		cacheMgr.LoadCache();
	}
	
}
