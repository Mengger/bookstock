package com.bookrecovery.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bookrecovery.dao.BookInfoDao;
import com.bookrecovery.dao.GetOrderDao;
import com.bookrecovery.entry.BookInfo;
import com.bookrecovery.entry.GetOrder;
import com.bookrecovery.service.IGetOrderService;

@Service(value="getOrderService")
public class GetOrderServiceImpl implements IGetOrderService {

	@Resource
	private GetOrderDao getOrderDao;
	
	@Resource
	private BookInfoDao bookInfoDao;
	
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

	@Override
	public boolean saveBookOrderList(List<String> bookIdList, List<Integer> bookCountList, Integer employeeId) {
		// TODO Auto-generated method stub
		int index=0;
		for(String bookId:bookIdList){
			GetOrder getOrder = new GetOrder();
			getOrder.setBookId(bookId);
			getOrder.setEmployeeId(employeeId);
			getOrder.setBookCount(bookCountList.get(index));
			getOrder.setCreateBeginAfter(new Date());
			getOrder.setModifyTime(new Date());
			BookInfo bookInfo = new BookInfo();
			bookInfo.setBookId(bookId);
			List<BookInfo> bookInfoList = bookInfoDao.queryBookInfoList(bookInfo);
			if(bookInfoList!=null&&bookInfoList.size()>0){
				BookInfo book = bookInfoList.get(0);
				getOrder.setTip(book.getUsedPrices());
				getOrder.setBookPrices(book.getOrderPrices());
				
			}else{
				return false;
			}
			index++;
		}
		//getOrderDao.saveGetOrder(getOrder);
		return false;
	}

}
