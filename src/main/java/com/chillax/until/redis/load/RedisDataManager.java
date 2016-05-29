package com.chillax.until.redis.load;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.ai.appframe2.service.ServiceFactory;
import com.chillax.until.redis.bean.CFG_REDIS_LOADBean;
import com.chillax.until.redis.bean.CFG_REDIS_PERSISTBean;
import com.chillax.until.redis.bean.RedisConstants;
import com.chillax.until.redis.client.InitRedisClient;
import com.chillax.until.redis.clients.jedis.Jedis;
//import com.chillax.until.redis.service.interfaces.IRedisBaseSV;
//import com.chillax.until.redis.service.interfaces.IRedisPersistSV;
import com.chillax.until.redis.util.SerializeUtil;


/***********************************************************
 * @Title: DataLoadFactory.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-21 上午03:31:40
 * @version:1.0
 **********************************************************/
public class RedisDataManager {
	private transient static Log log = LogFactory.getLog(RedisDataManager.class);


	public static CFG_REDIS_LOADBean getRedisLoadCache(String code)
			throws Exception {
		try{
			Object obj = InitRedisClient.readObject(RedisConstants.REDIS_BELONG_GROUP, code);
			if (obj == null) {
				//log.error("无法获取该缓存编码的配置信息：" + code + "请先刷新缓存！");
				return null;
			}
			return (CFG_REDIS_LOADBean)obj;
		}catch (Exception e) {
			log.error("无法获取该缓存编码的配置信息：" + code + "请先刷新缓存！");
			throw new NullPointerException("无法获取该缓存编码的配置信息：" + code);
		}
	}

	public static boolean containsKey(String redisCode, String key)
			throws Exception {
		CFG_REDIS_LOADBean bean = getRedisLoadCache(redisCode);
		if(null == bean) {
			log.error("无法获取该缓存编码的配置信息：" + redisCode + "请先刷新缓存！");
			return false;
		}
		Map map = InitRedisClient.readBatch(bean.getBelongGroup(), redisCode);
		if (map == null) {
			throw new Exception("无法获得Redis缓存代码:【" + redisCode + "】,对应的缓存");
		}
		return map.containsKey(key);
	}
	
	public static Object get(String redisCode, String key) throws Exception {
		CFG_REDIS_LOADBean bean = getRedisLoadCache(redisCode);
		if(null == bean) {
			log.error("无法获取该缓存编码的配置信息：" + redisCode + "请先刷新缓存！");
			return null;
		}
		Map map = InitRedisClient.readBatch(bean.getBelongGroup(), redisCode);
		if (map == null) {
			log.error("无法获得Redis缓存代码:【" + redisCode + "】,对应的缓存");
			return null;
		}
		return map.get(key);
	}
	
	public static String getString(String group, String redisCode) throws Exception {
		return InitRedisClient.readString(group, redisCode);
	}
	
	public static Object getObject(String group, String redisCode) throws Exception {
		return InitRedisClient.readObject(group, redisCode);
	}
	
	public static void set(String redisCode, String key, String value) throws Exception {
		CFG_REDIS_LOADBean bean = getRedisLoadCache(redisCode);
		if(null == bean) {
			log.error("无法获取该缓存编码的配置信息：" + redisCode + "请先刷新缓存！");
			return;
		}
		InitRedisClient.writeByCode(bean.getBelongGroup(), redisCode, key, value);
	}
	/**
	 * 缓存一个自定义对象
	 * @param redisCode
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void set(String redisCode, String key, Object obj) throws Exception {
		CFG_REDIS_LOADBean bean = getRedisLoadCache(redisCode);
		if(null == bean) {
			log.warn("无法获取该缓存编码的配置信息：" + redisCode + "请先刷新缓存！");
			return ;
		}
		InitRedisClient.writeByCode(bean
				.getBelongGroup(), redisCode, key, obj);
	}
	
	/**
	 * 缓存一个String
	 * @param redisCode
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setString(String group, String redisCode, String value) throws Exception {
		InitRedisClient.write(group, redisCode, value);
	}
	
	/**
	 * 缓存一个自定义对象
	 * @param redisCode
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setObject(String group, String redisCode, Object obj) throws Exception {
		InitRedisClient.writeObject(group, redisCode, obj);
	}
	
	public static Object getAll(String redisCode) throws Exception {
		CFG_REDIS_LOADBean bean = getRedisLoadCache(redisCode);
		if(null == bean) {
			log.warn("无法获取该缓存编码的配置信息：【" + redisCode + "】,请先刷新缓存！");
			return null;
		}
		Map map = InitRedisClient.readBatch(bean.getBelongGroup(), redisCode);
		if (map == null) {
			log.error("无法获得Redis缓存代码:【" + redisCode + "】,对应的缓存");
		}
		return map;
	}
	
	/**
	 * add by xuzq
	 * 删除指定的key
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static void remove(String group, String key) throws Exception {
		InitRedisClient.remove(group, key);
	}
	
	//删除RedisCode对象的Map对象里面的key
	public static void removeByKey(String group, String redisCode, String key) throws Exception {
		InitRedisClient.removeByCode(group, redisCode, key);
	}
	
	/**
	 * add by xuzq
	 * 清空缓存，请慎用
	 * @param redisCode
	 * @param key
	 * @param value
	 * 
	 */
	public static void removeAll(String group) throws Exception {
		InitRedisClient.removeAll(group);
	}
	
	/**
	 * add by xuzq
	 * 发布数据到Redis持久化监听类
	 * @param key
	 * @param bean
	 * @return 持久化的字节长度
	 * @throws Exception
	 */
	public static long publish(String key,CFG_REDIS_PERSISTBean bean) throws Exception{
		long result;
		if("".equals(key) || null == key ){
			log.error("持久化失败,对应的key为空！");
			return 0;
		}
		if(bean == null ){
			log.error("持久化失败,持久化的目标为空");
			return 0;
		}
		Jedis jedis = InitRedisClient.getPersistConnection();
		byte[] channel = SerializeUtil.serialize(RedisConstants.PERSIST_CHANNEL);
		bean.setKey(key);
		byte[] byteBean = SerializeUtil.serialize(bean);
		result = jedis.publish(channel, byteBean);
		InitRedisClient.releaseConnection(jedis, "BASE");
		return result;
	}
	
	//根据key返回持久化对象数组
//	public static CFG_REDIS_PERSISTBean[] getPersistBeanByKey(String key) throws Exception{
//		IRedisPersistSV sv = (IRedisPersistSV)ServiceFactory.getService(IRedisPersistSV.class);
//		return sv.list(key);
//	}
	
	
	/**
	 * 根据月份查持久化分表的所有数据，如month='201211',month=null 表示查当前月
	 * @param month
	 * @return
	 * @throws Exception
	 */
//	public static CFG_REDIS_PERSISTBean[] getAllPersistBean(String month) throws Exception{
//		IRedisPersistSV sv = (IRedisPersistSV)ServiceFactory.getService(IRedisPersistSV.class);
//		return sv.listAll(month);
//	}
	
}
