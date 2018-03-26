package com.nfet.icare.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nfet.icare.pojo.Ticket;

public interface TicketMapper {
	//优惠券列表
	public List<Ticket> ticketList(@Param("map") Map<String, Object> map);
	//增加延保券
	public void insertTicket(@Param("ticket") Ticket ticket);
	//根据设备编码查询延保券
	public Ticket queryTicket(@Param("deviceNo") String deviceNo);
	//使用延保券
	public void useTicket(@Param("map") Map<String, Object> map);
}
