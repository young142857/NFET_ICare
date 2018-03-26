package com.nfet.icare.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nfet.icare.pojo.Mgr_Warranty;
import com.nfet.icare.pojo.Warranty;
import com.nfet.icare.pojo.WarrantyCount;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.WarrantyServiceImpl;
import com.nfet.icare.util.GlobalMethods;

/**
 * 延保统计
 * 1、延保统计
 * 2、客户延保订单
 * 
 */

//延保统计
@Controller
@RequestMapping("/mgr")
public class Mgr_WarrantyController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WarrantyServiceImpl warrantyServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	
	//延保统计
	@RequestMapping("/warrantyStatistics")
	@ResponseBody
	public Map<String, Object> warrantyStatistics(){
		Map<String, Object> warrantyMap = new HashMap<>();
		int [] warrantyArr = new int[7];
		int [] visitArr = new int[7];
		String [] nameStr = new String[10];
		int allCount = 0;
		WarrantyCount total = new WarrantyCount();
		List<Mgr_Warranty> warrantyOrder = new ArrayList<>();
		List<Mgr_Warranty> visitOrder = new ArrayList<>();
		List<WarrantyCount> warrantyCount = new ArrayList<>();
		List<WarrantyCount> visitCount = new ArrayList<>();
		List<WarrantyCount> sumCount = new ArrayList<>();
		List<WarrantyCount> totalCount = new ArrayList<>();
		
		//延保总收入
		String sumIncome = warrantyServiceImpl.sumIncome();
		//折线统计
		warrantyOrder = warrantyServiceImpl.warrantyOrder();
		logger.info("get all warranty orders");
		visitOrder = warrantyServiceImpl.visitOrder();
		logger.info("get all visit orders");
		String[] weekStr = GlobalMethods.weekDays();
		for (int i = 0; i < 7; i++) {
			//一周延保订单
			for (Mgr_Warranty warranty : warrantyOrder) {
				if (warranty.getOrderTime().toString().indexOf(weekStr[i]) != -1) {
					warrantyArr[i] ++;
				}
			}
			//一周上门订单
			for (Mgr_Warranty warranty : visitOrder) {
				if (warranty.getOrderTime().toString().indexOf(weekStr[i]) != -1) {
					visitArr[i] ++;
				}
			}			
		}
		
		//扇形统计
		warrantyCount = warrantyServiceImpl.warrantyCount();  //延保套餐数量
		logger.info("get the count of warranty");
		visitCount = warrantyServiceImpl.visitCount();  //上门套餐数量
		logger.info("get the count of visit");
		
		//计算每种套餐收入
		for (WarrantyCount count : warrantyCount) {
			count.setPayAmts(warrantyServiceImpl.warrantyPayAmts(count.getName()));;
		}
		for (WarrantyCount count : visitCount) {
			count.setPayAmts(warrantyServiceImpl.visitPayAmts(count.getName()));;
		}
		//套餐总数量
		sumCount.addAll(warrantyCount); 
		sumCount.addAll(visitCount);
		//按套餐数量排序
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("value"),true);//true从大到小  false从小到大		
		Collections.sort(sumCount,chain);
		//套餐名称
		for (int i = 0; i < sumCount.size(); i++) {
			nameStr[i] = sumCount.get(i).getName();
			allCount += sumCount.get(i).getValue();
		}
		totalCount.addAll(sumCount);
		total.setName("总计");
		total.setValue(allCount);
		total.setPayAmts(sumIncome);
		//计算套餐总数和总金额
		totalCount.add(total);
		
//		//将一周的时间格式从年月日改为月日
//		for (int i = 0; i < weekStr.length; i++) {
//			String string = weekStr[i].substring(5);
//			weekStr[i] = string; 
//		}
		
		warrantyMap.put("weekStr", weekStr);
		warrantyMap.put("warrantyArr", warrantyArr);
		warrantyMap.put("visitArr", visitArr);		
		warrantyMap.put("nameStr", nameStr);		
		warrantyMap.put("sumCount", sumCount);		
		warrantyMap.put("totalCount", totalCount);		
		
		return warrantyMap;
	}
	
	//客户延保订单
	@RequestMapping("/allWarrantyOrder")
	@ResponseBody
	public List<Mgr_Warranty> allWarrantyOrder() throws ParseException{
		List<Mgr_Warranty> allWarrantyOrder = new ArrayList<>();
		List<Mgr_Warranty> warrantyOrders = new ArrayList<>();
		List<Mgr_Warranty> visitOrders = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
		
		int num = 1;
		warrantyOrders = warrantyServiceImpl.allWarrantyOrder();
		logger.info("get all warrantyOrders");
		visitOrders = warrantyServiceImpl.allVisitOrder();
		logger.info("get all visitOrders");
		for (Mgr_Warranty mgr_Warranty : visitOrders) {
			mgr_Warranty.setWarrantyFrom(mgr_Warranty.getVisitFrom());
			mgr_Warranty.setWarrantyTo(mgr_Warranty.getVisitTo());
		}
		//将两种订单合并
		allWarrantyOrder.addAll(warrantyOrders);
		allWarrantyOrder.addAll(visitOrders);
		//根据下单时间排序
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("orderTime"),true);//true从大到小  false从小到大		
		Collections.sort(allWarrantyOrder,chain);
		for (Mgr_Warranty mgr_Warranty : allWarrantyOrder) {
			//加序号
			mgr_Warranty.setNum(num);
			num ++;
			//更改下单时间显示格式
			mgr_Warranty.setOrderTimeStr(sdf4.format(sdf3.parse(mgr_Warranty.getOrderTime().toString().substring(0,19))));
			//延保期显示方式
			mgr_Warranty.setWarrantyFrom(sdf2.format(sdf1.parse(mgr_Warranty.getWarrantyFrom())));
			mgr_Warranty.setWarrantyTo(sdf2.format(sdf1.parse(mgr_Warranty.getWarrantyTo())));
			mgr_Warranty.setWarrantyDate(mgr_Warranty.getWarrantyFrom()+"-"+mgr_Warranty.getWarrantyTo());
			//延保期限(月)
			if (mgr_Warranty.getWarrantyPkgNo().getWarrantyPkgPeriod() == 90) {
				mgr_Warranty.setWarrantyPeriod("3");
			}			
			else {
				mgr_Warranty.setWarrantyPeriod(mgr_Warranty.getWarrantyPkgNo().getWarrantyPkgPeriod()*12+"");
			}
			//获取公司名称、行业
			mgr_Warranty.setCompanyName(companyServiceImpl.queryCompanyInfo(mgr_Warranty.getUserNo().getCompanyId()).getCompanyName());
			mgr_Warranty.setIndustry(companyServiceImpl.queryCompanyInfo(mgr_Warranty.getUserNo().getCompanyId()).getIndustry());
		}		
		
		return allWarrantyOrder;
	}
}
