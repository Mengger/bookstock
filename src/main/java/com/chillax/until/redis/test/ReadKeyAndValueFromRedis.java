package com.chillax.until.redis.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.chillax.until.redis.load.RedisManager;

/**   
 *      
 * ��������  ��redis�л�ȡ��ǰ��key-valueֵ 
 * �����ˣ�suizhifa   
 * ����ʱ�䣺2015-2-14 ����04:46:42   
 * �޸��ˣ�suizhifa   
 * �޸�ʱ�䣺2015-2-14 ����04:46:42   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class ReadKeyAndValueFromRedis {

	/**
	 * ��һ������ ����ţ� GROUP_0��
	 * �ڶ������� ��ȡ�������� ��0-��ȡȫ����1-��ȡÿ��ʡ����������2-��ȡ�����е�����session��3��ȡ�����е��쳣��Ϣ��
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.out.println("=====read key-value from redis==========");
		Set<String> set=RedisManager.getAllKeys("GROUP_0", "*");
		Iterator<String> it=set.iterator();
		List<String> provList=new ArrayList<String>();
		List<String> sessionList=new ArrayList<String>();
		List<String> excepList=new ArrayList<String>();
		while(it.hasNext()){
			String key=it.next();
			if(key.startsWith("CON")){
				sessionList.add(key);
			}else if(key.startsWith("EXCEP")){
				excepList.add(key);
			}else {
				provList.add(key);
			}
		}
		System.out.println("====current connect count begin==========");
		if(provList!=null && !provList.isEmpty()){
			for(int i=0;i<provList.size();i++){
				System.out.println(provList.get(i)+"---"+RedisManager.getValueByKeyAndGroup("GROUP_0", provList.get(i)));
			}
		}
		System.out.println("====current connect count end==========");
		
		System.out.println("====current session data begin==========");
		if(sessionList!=null && !sessionList.isEmpty()){
			for(int i=0;i<sessionList.size();i++){
				System.out.println(sessionList.get(i)+"---"+RedisManager.getValueByKeyAndGroup("GROUP_0", sessionList.get(i)));
			}
		}
		System.out.println("====current session data end==========");
		
		System.out.println("====current exception data begin==========");
		if(excepList!=null && !excepList.isEmpty()){
			for(int i=0;i<excepList.size();i++){
				System.out.println(excepList.get(i)+"---"+RedisManager.getValueByKeyAndGroup("GROUP_0", excepList.get(i)));
			}
		}
		System.out.println("====current exception data end==========");

		System.out.println("*********************"+RedisManager.getCountByKeyAndInc("GROUP_0","keyssss"));
	}

}
