package com.chillax.until.redis.expand;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chillax.until.redis.bean.RedisConstants;
import com.chillax.until.redis.client.InitRedisClient;
import com.chillax.until.redis.clients.jedis.Jedis;
import com.chillax.until.redis.util.SerializeUtil;


/**
 * ����Ϊ�־û����������߳��࣬���ӳس�ʼ����ɺ���������
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
			log.error("����Channel��" + RedisConstants.PERSIST_CHANNEL + " ����ʧ��");
		}
	}
	
	//�����־û�����
	public void subscribe() throws Exception{
		Jedis jedis = InitRedisClient.getPersistConnection();
		log.debug("����Channel��" + RedisConstants.PERSIST_CHANNEL + " ��������");
		jedis.subscribe(new PersistAsset(), SerializeUtil.serialize(RedisConstants.PERSIST_CHANNEL));
	}
}
