package com.chillax.until.redis.load;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chillax.until.redis.client.InitRedisClient;


/***********************************************************
 * @Title: DataLoadFactory.java
 * @Description:
 * @author:suizhifa
 * @create_data:2015-2-11 ����03:31:40
 * @version:1.0
 **********************************************************/
public class RedisManager {
	private transient static Log log = LogFactory.getLog(RedisDataManager.class);
	/**
	 * ����keyֵ��ȡvalue
	 * @param group ����GROUP_1
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getValueByKeyAndGroup(String group,String key)throws Exception{
		if(key==null || "".equals(key)){
			log.error("д��keyֵ����Ϊ�գ����飡");
			return null;
		}
		try{
			return InitRedisClient.getValueByKeyAndGroup(group,  key);	
		}catch(Exception e){
			log.error("redis exception getValue by key"+key);
			e.printStackTrace();
		}
		return "0";
		
	}
	/**
	 * ����keyֵ�洢valueֵ
	 * @param group ����GROUP_1
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setValueByKeyAndGroup(String group,String key,String value)throws Exception{
		if(key==null || "".equals(key)){
			log.error("д��keyֵ����Ϊ�գ����飡");
			return;
		}
		if(value==null || "".equals(value)){
			log.error("д��valueֵ����Ϊ�գ����飡");
			return;
		}
		try{
			InitRedisClient.writeValueByKeyAndGroup(group,  key, value);	
		}catch(Exception e){
			log.error("redis exception writeValue by key-value:"+key+"-"+value);
			e.printStackTrace();
		}
		
	}
	/**
	 * ɾ��ָ����key
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static void removeByKeyAndGroup(String group, String key) throws Exception {
		try{
			InitRedisClient.remove(group, key);	
		}catch(Exception e){
			log.error("redis exception removeValue by key:"+key);
			e.printStackTrace();
		}
		
		
	}
	/**
	 * ��ջ��棬������
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static void removeAllByGroup(String group) throws Exception {
		InitRedisClient.removeAll(group);
	}
	/**
	 * ����patternֵ��ȡ���е�keys
	 * @param group
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllKeys(String group,String pattern) throws Exception{
		return InitRedisClient.getAllKeys(group,pattern);
	}
	/**
	 * ����key��ȡvalueֵ,ͬʱ��value�Զ���1
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static long getCountByKeyAndInc(String group,String key) throws Exception {
		try{
			return InitRedisClient.getCountByKeyAndInc(group, key);
		}catch(Exception e){
			log.error("Redis error getCountByKeyAndInc");
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * ����key��ȡvalueֵ,ͬʱ��value�Զ���1
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static long getCountByKeyAndDec(String group,String key) throws Exception {
		try{
			return InitRedisClient.getCountByKeyAndDec(group, key);
		}catch(Exception e){
			log.error("Redis error getCountByKeyAndDec");
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 *   ����keyֵ���ó�ʱʱ�䣬��������Ϊ��
	 * @param group
	 * @param key
	 * @param timeoutSecond ��ʱʱ�䣬��
	 * 
	 */
	  public static long expire(String group,String key,int timeoutSecond) {
		try{
			return InitRedisClient.expire(group, key,timeoutSecond);
		}catch(Exception e){
			log.error("Redis error expire");
			e.printStackTrace();
		}
		return 0;
	}
}
