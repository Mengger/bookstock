package bookstock.com.chillax.test.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.chillax.service.IbookInfoService;

import bookstock.com.chillax.test.TestLoadBean;

public class BookInfoServiceTest extends TestLoadBean{

	@Resource
	private IbookInfoService bookInfoService;

	@Test
	public void testQueryDBInternet(){
		System.out.println(JSON.toJSON(bookInfoService.queryDBThenInterNet("9787111212508")));
	}
}
