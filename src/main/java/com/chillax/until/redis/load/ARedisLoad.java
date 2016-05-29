package com.chillax.until.redis.load;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chillax.until.redis.bean.CFG_REDIS_LOADBean;
import com.chillax.until.redis.bean.RedisConstants;
import com.chillax.until.redis.client.RedisClient;

/***********************************************************
 * @Title: ARedisLoad.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-21 ����02:40:23
 * @version:1.0
 **********************************************************/
public abstract class ARedisLoad implements IRedisLoad {
	private transient static Log log = LogFactory.getLog(ARedisLoad.class);

	// �Ƿ��Ѿ���ʼ����
	private Boolean IF_INIT = Boolean.FALSE;
	private Boolean LOCK = Boolean.FALSE;
//	private HashMap cache = new HashMap();
	private String CacheCode = "";
	private String Belong_Group = "";

	public ARedisLoad() {
	}

	/**
	 * ˢ��
	 * 
	 * @throws Exception
	 */
	public void load(CFG_REDIS_LOADBean lb) throws Exception {
		log.error("��ʼ���������飺��"+lb.getBelongGroup()+"��;code����"+CacheCode+"��,�ࣺ��"+this.getClass()+"��");
		//����Ѽ����򸲸�
		HashMap map = getData();
		RedisClient.write(lb.getBelongGroup(), CacheCode, map);
		//����Ĭ�ϵĻ���
//		RedisClient.loadDefaultCache();
		//������������Ϣ�ӵ�Ĭ�ϵĻ�����ȥ��
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
				log.debug("redisCache��:" + this.getClass() + ",�����ӳټ���,���ڿ�ʼ����");
			}
			//load();
		}

		if (LOCK.equals(Boolean.TRUE)) {
			// ����
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// ������ѭ��
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
			// ����
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// ������ѭ��
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
			// ����
			int i = 0;
			while (LOCK.equals(Boolean.TRUE)) {
				if (i < 50) {
					Thread.currentThread().sleep(100);
					// ������ѭ��
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
		String newKey = group + key; //�����keyΪ��������loadCode�����;
		if(!RedisClient.containsKey(group, newKey)) {
			RedisClient.write(group, newKey, lb);
			log.debug("д�뻺����룺��" + key + "����Ӧ��������Ϣ.");
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
