package com.bookrecovery.test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration("classpath:/config/applicationContent.xml")
public class TestLoadBean {

	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		QueryResult<HashMap<String,ArrayList<GPRS>>> a=JSONObject.parseObject("{\"status\": \"0\",\"message\": \"正确\",\"result\": [{\"gprs\": [ {\"total\": \"100\",\"prodid\": \"I00010100029\",\"left\": \"20\",\"prodname\": \"GPRS10 元套餐\",\"used\": \"80\"},{\"total\": \"200\",\"prodid\": \"I00010100030\",\"left\": \"20\",\"prodname\": \"GPRS20 元套餐\",\"used\": \"180\"}]}]}",new QueryResult<HashMap<String,ArrayList<GPRS>>>().getClass());
		System.out.println(a.getMessage());
		List<HashMap<String,ArrayList<GPRS>>> b=(List<HashMap<String, ArrayList<GPRS>>>) a.getResult();
		System.out.println(JSON.toJSONString(b));
		
		GPRS g1=new GPRS();
		g1.setLeft("200");
		g1.setProdid("455252222222222");
		g1.setProdname("套餐1");
		g1.setTotal("500");
		g1.setUsed("300");
		GPRS g2=new GPRS();
		g2.setLeft("400");
		g2.setProdid("455252111111111");
		g2.setProdname("套餐2");
		g2.setTotal("800");
		g2.setUsed("400");
		ArrayList<GPRS> ig=new ArrayList<GPRS>();
		ig.add(g2);
		ig.add(g1);
		Map<String , ArrayList<GPRS>> hg=new HashMap<String , ArrayList<GPRS>>();
		hg.put("gprs", ig);
		
		
		HashMap<String, ArrayList<GPRS>> c=b.get(0);
		System.out.println(JSON.toJSONString(c));
	}
}
