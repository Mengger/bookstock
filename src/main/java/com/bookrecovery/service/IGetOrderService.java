package com.bookrecovery.service;

import java.util.List;

import com.bookrecovery.entry.GetOrder;

public interface IGetOrderService {

	/**
	 * 获取总数
	 * @param getOrder
	 * @return
	 */
	public Long getGetOrderCount(GetOrder getOrder);
	
	/**
	 * 获取订单list
	 * @param getOrder
	 * @return
	 */
	public List<GetOrder> getOrderList(GetOrder getOrder);
}
