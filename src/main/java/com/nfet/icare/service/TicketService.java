package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import com.nfet.icare.pojo.Ticket;

public interface TicketService {
	//优惠券列表
	public List<Ticket> ticketList(Map<String, Object> map);
	//增加延保券
	public void insertTicket(Ticket ticket);
	//根据设备编码查询延保券
	public Ticket queryTicket(String deviceNo);
	//使用延保券
	public void useTicket(Map<String, Object> map);
}
