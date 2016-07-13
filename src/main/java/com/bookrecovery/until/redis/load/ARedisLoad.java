package com.bookrecovery.until.redis.load;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bookrecovery.until.redis.bean.CFG_REDIS_LOADBean;
import com.bookrecovery.until.redis.bean.RedisConstants;
import com.bookrecovery.until.redis.client.RedisClient;

/***********************************************************
 * @Title: ARedisLoad.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-21 上午02:40:23
 * @version:1.0
 **********************************************************/
public abstract class ARedisLoad implements IRedisLoad {
	private transient static Log log = LogFactory.getLog(ARedisLoad.class);

	// 是否已经初始化了
	private Boolean IF_INIT = Boolean.FALSE;
	private Boolean LOCK = Boolean.FALSE;
//	private HashMap cache = new HashMap();
	private String CacheCode = "";
	private String Belong_Group = "";

	public ARedisLoad() {
	}

	/**
	 * 刷新
	 * 
	 * @throws Exception
	 */
	public void load(CFG_REDIS_LOADBean lb) throws Exception {
		log.error("开始加载数据组：【"+lb.getBelongGroup()+"】;code：【"+CacheCode+"】,类：【"+this.getClass()+"】");
		//如果已加载则覆盖
		HashMap map = getData();
		RedisClient.write(lb.getBelongGroup(), CacheCode, map);
		//创建默认的缓存
//		RedisClient.loadDefaultCache();
		//将该条配置信息加到默认的缓存中去；
		//RedisClient.loadInfoCache.put(lb.getRedisLoadCode(), lb);
//		RedisClient.writeByCode(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.REDIS_LOAD_CODE_CONFIG, lb.getRedisLoadCode(), lb);
		cacheLoadCode(RedisConstants.REDIS_BELONG_GROUP, lb.getRedisLoadCode(), lb);
	}

	/**
	 * @param key
	 *            Object
	 * @throws Exception
	 * @return Object
	 */
	public Object getObject(Object key) throws Exception {
		if (IF_INIT.equals(Boolean.FALSE)) {
			if (log.isDebugEnabled()) {
				log.debug("redisCache类:" + this.getClass() + ",属于延迟加载,现在开始加载");
			}
			//load();
		}

		if (LOCK.equals(Boolean.TRUE)) {
			// 读锁
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// 以免死循环
					i++;
				} else {
					return null;
				}
			}
		}
		String newKey = CacheCode+"^MICROREDIS";
		Map map =RedisClient.readBatch(this.Belong_Group, newKey);
		return map.get(key);
	}

	/**
	 * @param key
	 *            Object
	 * @throws Exception
	 * @return boolean
	 */
	public boolean existsKey(Object key) throws Exception {
		if (IF_INIT.equals(Boolean.FALSE)) {
			//load();
		}

		if (LOCK.equals(Boolean.TRUE)) {
			// 读锁
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// 以免死循环
					i++;
				} else {
					return false;
				}
			}
		}
		String newKey = CacheCode+"^MICROREDIS";
		Map map =RedisClient.readBatch(this.Belong_Group, newKey);
		return map.containsKey(key);
	}

	/**
	 * @throws Exception
	 * @return HashMap
	 */
	public Map getAllObject() throws Exception {
		if (IF_INIT.equals(Boolean.FALSE)) {
			//load();
		}

		if (LOCK.equals(Boolean.TRUE)) {
			// 读锁
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// 以免死循环
					i++;
				} else {
					return null;
				}
			}
		}
		String newKey = CacheCode+"^MICROREDIS";
		Map map =RedisClient.readBatch(this.Belong_Group, newKey);
		return map;
	}
	
	private void cacheLoadCode(String group, String key , CFG_REDIS_LOADBean lb) throws Exception{
		String newKey = group + key; //存入的key为所属组与loadCode的组合;
		if(!RedisClient.containsKey(group, newKey)) {
			RedisClient.write(group, newKey, lb);
			log.debug("写入缓存编码：【" + key + "】对应的配置信息.");
		}
	}

	/**
	 * @return boolean
	 */
	public boolean haveLoaded() {
		return IF_INIT.booleanValue();
	}
	
	public void setGroup(String g){
		Belong_Group = g;
	}
	
	public void setCacheCode(String code){
		CacheCode = code;
	}
	public abstract HashMap getData() throws Exception;
	
	
}
