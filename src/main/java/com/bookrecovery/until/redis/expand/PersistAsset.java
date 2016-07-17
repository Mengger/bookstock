package com.bookrecovery.until.redis.expand;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.ai.appframe2.service.ServiceFactory;

import com.bookrecovery.until.redis.bean.RedisConstants;
import com.bookrecovery.until.redis.clients.jedis.BinaryJedisPubSub;

import com.bookrecovery.until.redis.util.SerializeUtil;


/**
 * 持久化监听器的回调类，该类负责将Redis返回的数据持久化到数据库
 * @author xuzq
 * @date 2012-11-08;
 *
 */
public class PersistAsset extends BinaryJedisPubSub {

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onMessage(byte[], byte[])
	 */
	@Override
	public void onMessage(byte[] channel, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onPMessage(byte[], byte[], byte[])
	 */
	@Override
	public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onPSubscribe(byte[], int)
	 */
	@Override
	public void onPSubscribe(byte[] pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onPUnsubscribe(byte[], int)
	 */
	@Override
	public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onSubscribe(byte[], int)
	 */
	@Override
	public void onSubscribe(byte[] channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.chillax.until.redis.clients.jedis.BinaryJedisPubSub#onUnsubscribe(byte[], int)
	 */
	@Override
	public void onUnsubscribe(byte[] channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		
	}
//	private static transient Log log = LogFactory.getLog(PersistAsset.class);
//	@Override
////	public void onMessage(byte[] channel, byte[] message){
////		String c = (String)SerializeUtil.unserialize(channel);
////		log.debug("Channel:" + c + "取得订阅消息...");
////		Object o = SerializeUtil.unserialize(message);
////		IRedisPersistSV sv =(IRedisPersistSV)ServiceFactory.getService(IRedisPersistSV.class);
////		try{
////			if(o instanceof CFG_REDIS_PERSISTBean) {
////				CFG_REDIS_PERSISTBean bean = (CFG_REDIS_PERSISTBean)o;
////				sv.save(bean);
////			}
////		}catch(Exception e) {
////			e.printStackTrace();
////		}
////	}
//
//	@Override
//	public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
//		log.debug("取得按表达式订阅消息,pattern: " + pattern + ",channel: " +channel + ",message: " + message);
//	}
//
//	@Override
//	public void onPSubscribe(byte[] pattern, int subscribedChannels) {
//		log.debug("初始化按表达式订阅频道...");
//
//	}
//
//	@Override
//	public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
//		log.debug("取消按表达式订阅频道...");
//
//	}
//
//	@Override
//	public void onSubscribe(byte[] channel, int subscribedChannels) {
//		log.debug("初始化频道中...");
//
//	}
//
//	@Override
//	public void onUnsubscribe(byte[] channel, int subscribedChannels) {
//		log.debug("取消频道中...");
//
//	}

}
