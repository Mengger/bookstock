package com.bookrecovery.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookrecovery.entry.GetOrder;
import com.bookrecovery.entry.enums.ErrorCodeEnum;
import com.bookrecovery.entry.result.PageRequest;
import com.bookrecovery.entry.result.PageResultDO;
import com.bookrecovery.service.IGetOrderService;

@Controller
@RequestMapping
public class OrderInfoAction {

	
	private static final Logger log = LoggerFactory.getLogger(WechatVerifyAction.class);
	
	@Resource
	private IGetOrderService getOrderService;
	
	/**
	 * 获取订单详情列表
	 * @param inputLoginId 用户id
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 * @param pageNum		第几页（从第0页开始）
	 * @param pageSize		每页订单数量
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET,value="/getOrders")
	@ResponseBody
	public PageResultDO<GetOrder> getOrdersList(Integer inputLoginId,Long beginTime,Long endTime, int pageNum, int pageSize) throws Exception{
		PageRequest pageRequest = null;
		GetOrder getOrder = null;
		PageResultDO<GetOrder> rtn=null;
		try {
			pageRequest = new PageRequest(pageNum, pageSize);
			rtn=new PageResultDO<GetOrder>(pageRequest);
			getOrder=new GetOrder();
			getOrder.setPageRequest(pageRequest);
			getOrder.setEmployeeId(inputLoginId);
			getOrder.setCreateBeginAfter(new Date(endTime));
			getOrder.setCreateBeginBefore(new Date(beginTime));
		} catch (Exception e) {
			rtn.setSuccess(false);
			rtn.setErrorCode(ErrorCodeEnum.Error_input.getErrorCode());
			rtn.setErrorDesc(ErrorCodeEnum.Error_input.getErrorMessage());
			return rtn;
		}
		rtn.setTotalElements(getOrderService.getGetOrderCount(getOrder));
		rtn.setResult(getOrderService.getOrderList(getOrder));
		return rtn;
	}
	
	public void bookrecoveryInfo(List<String> bookCode,List<Integer> bookNum){
		
	}
}
