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
	
	/**
	 * 批量保存书本回收信息
	 * @param bookInfo		书本编号
	 * @param bookNum		回收数量
	 * @param employeeId	雇员id
	 * @return
	 */
	public boolean saveBookOrderList(List<String> bookIdList,List<Integer> bookCountList,Integer employeeId);
}
