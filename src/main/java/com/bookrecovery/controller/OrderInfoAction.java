package com.bookrecovery.controller;

import java.util.Date;

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
	
	@RequestMapping(method = RequestMethod.GET,value="/getOrders")
	@ResponseBody
	public PageResultDO<GetOrder> getOrdersList(Integer inputLoginId,Long beginTime,Long endTime, int pageNum, int pageSize) throws Exception{
		PageRequest pageRequest = null;
		GetOrder getOrder = null;
		PageResultDO<GetOrder> rtn=new PageResultDO<GetOrder>(pageRequest);
		try {
			pageRequest = new PageRequest(pageNum, pageSize);
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
}
