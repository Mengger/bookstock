package com.bookrecovery.test;

import java.security.NoSuchAlgorithmException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bookrecovery.until.HttpRequest;
import com.bookrecovery.until.StringRegexUntil;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration("classpath:/config/applicationContent.xml")
public class TestLoadBean {

	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String url = "http://localhost:8888/this/is/test";
		String queryResult = HttpRequest.sendGet(url,null);
		System.out.println(queryResult);
		
	}
	
	
	public void getImgPath(){
		String url = "http://yezu6.top/L/?1";
		String queryResult = HttpRequest.sendGet(url,null);
		String[] first = StringRegexUntil.regexString(queryResult, "<a href=\"((?!</a>).)*\" target=\"_blank\">((?!</a>).)*</a>");
		for(String sec:first){
			if(sec.contains("[")&&sec.contains("]")){
				String headUrl="http://yezu6.top/";
				try {
					String secUrl = StringRegexUntil.regexString(sec, "href=\"((?!target=\"_blank\">  ).)*\"")[0].replaceAll("\"", "").replace("href=", "");
					int num=Integer.valueOf(sec.substring(sec.indexOf("[")+1, sec.indexOf("]")-1));
					queryResult = HttpRequest.sendGet(headUrl+secUrl,null);
					String a=StringRegexUntil.regexString(queryResult, "<img src=\"http((?!\").)*\"")[0];
					String aa=a.substring(0,a.length()-2);
					for(int i=1;i<num;i++){
						System.out.println(aa+i+"\">");
					}
				} catch (Exception e) {
					System.out.println("****************");
				}
				
			}
		}
	}
}
