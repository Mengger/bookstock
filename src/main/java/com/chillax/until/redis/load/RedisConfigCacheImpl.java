package com.chillax.until.redis.load;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Ĭ�ϻ���ʵ��
 * ���ػ���������Ϣ,keyΪRedisCode
 * @author xuzq
 *
 */
public class RedisConfigCacheImpl extends ARedisLoad{
	private static transient Log log = LogFactory.getLog(RedisConfigCacheImpl.class);
	
	@Override
	public HashMap getData() throws Exception {
		HashMap data = new HashMap();
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory.getService(IRedisBaseSV.class);
//		CFG_REDIS_LOADBean[] caches = sv.getRedisCache();
//		for (int i = 0; i < caches.length; i++) {
//			data.put(caches[i].getRedisLoadCode(), caches[i]);
//		}
		return data;
	}
	
}
