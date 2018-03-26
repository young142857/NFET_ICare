package com.nfet.icare.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nfet.icare.dao.TicketMapper;
import com.nfet.icare.pojo.Ticket;

@Service
public class TicketServiceImpl implements TicketService {
	@Autowired
	private TicketMapper ticketMapper;

	@Override
	public List<Ticket> ticketList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ticketMapper.ticketList(map);
	}

	@Override
	public void insertTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		ticketMapper.insertTicket(ticket);
	}

	@Override
	public Ticket queryTicket(String deviceNo) {
		// TODO Auto-generated method stub
		return ticketMapper.queryTicket(deviceNo);
	}

	@Override
	public void useTicket(Map<String, Object> map) {
		// TODO Auto-generated method stub
		ticketMapper.useTicket(map);
	}
}
