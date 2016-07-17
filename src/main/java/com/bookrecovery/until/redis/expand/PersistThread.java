package com.bookrecovery.until.redis.expand;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bookrecovery.until.redis.bean.RedisConstants;
import com.bookrecovery.until.redis.client.InitRedisClient;
import com.bookrecovery.until.redis.clients.jedis.Jedis;
import com.bookrecovery.until.redis.util.SerializeUtil;


/**
 * 该类为持久化监听器的线程类，连接池初始化完成后启动该类
 * @author xuzq
 * @date 2012-11-09
 *
 */
public class PersistThread implements Runnable {
	private static transient Log log = LogFactory.getLog(PersistThread.class);
	public void run() {
		try {
			subscribe();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("监听Channel：" + RedisConstants.PERSIST_CHANNEL + " 启动失败");
		}
	}
	
	//开启持久化监听
	public void subscribe() throws Exception{
		Jedis jedis = InitRedisClient.getPersistConnection();
		log.debug("监听Channel：" + RedisConstants.PERSIST_CHANNEL + " 正在启动");
		jedis.subscribe(new PersistAsset(), SerializeUtil.serialize(RedisConstants.PERSIST_CHANNEL));
	}
}
