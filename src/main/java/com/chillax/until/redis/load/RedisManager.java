package com.chillax.until.redis.load;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chillax.until.redis.client.InitRedisClient;


/***********************************************************
 * @Title: DataLoadFactory.java
 * @Description:
 * @author:suizhifa
 * @create_data:2015-2-11 上午03:31:40
 * @version:1.0
 **********************************************************/
public class RedisManager {
	private transient static Log log = LogFactory.getLog(RedisDataManager.class);
	/**
	 * 根据key值获取value
	 * @param group 分组GROUP_1
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getValueByKeyAndGroup(String group,String key)throws Exception{
		if(key==null || "".equals(key)){
			log.error("写入key值内容为空，请检查！");
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
	 * 根据key值存储value值
	 * @param group 分组GROUP_1
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setValueByKeyAndGroup(String group,String key,String value)throws Exception{
		if(key==null || "".equals(key)){
			log.error("写入key值内容为空，请检查！");
			return;
		}
		if(value==null || "".equals(value)){
			log.error("写入value值内容为空，请检查！");
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
	 * 删除指定的key
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
	 * 清空缓存，请慎用
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static void removeAllByGroup(String group) throws Exception {
		InitRedisClient.removeAll(group);
	}
	/**
	 * 根据pattern值获取所有的keys
	 * @param group
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllKeys(String group,String pattern) throws Exception{
		return InitRedisClient.getAllKeys(group,pattern);
	}
	/**
	 * 根据key获取value值,同时将value自动加1
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
	 * 根据key获取value值,同时将value自动减1
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
	 *   根据key值设置超时时间，参数级别为秒
	 * @param group
	 * @param key
	 * @param timeoutSecond 超时时间，秒
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
