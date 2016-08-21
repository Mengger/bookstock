package com.bookrecovery.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bookrecovery.dao.GetOrderDao;
import com.bookrecovery.entry.GetOrder;
import com.bookrecovery.service.IGetOrderService;

@Service(value="getOrderService")
public class GetOrderServiceImpl implements IGetOrderService {

	@Resource
	private GetOrderDao getOrderDao;
	@Override
	public Long getGetOrderCount(GetOrder getOrder) {
		// TODO Auto-generated method stub
		return getOrderDao.queryGetOrderCount(getOrder);
	}

	@Override
	public List<GetOrder> getOrderList(GetOrder getOrder) {
		// TODO Auto-generated method stub
		return getOrderDao.queryGetOrderList(getOrder);
	}

}
