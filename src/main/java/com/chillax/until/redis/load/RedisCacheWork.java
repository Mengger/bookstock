package com.chillax.until.redis.load;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.ai.appframe2.service.ServiceFactory;
import com.chillax.until.redis.bean.CFG_REDIS_LOADBean;
//import com.chillax.until.redis.service.interfaces.IRedisBaseSV;


/***********************************************************
 * @Title: RedisCacheWork.java 
 * @Description:����redis�Զ��建������
 * @author:xuehao
 * @create_data:2012-9-24 ����01:50:04 
 * @version:1.0
 **********************************************************/
public class RedisCacheWork {
	private transient static Log log = LogFactory.getLog(RedisCacheWork.class);
	private static Map all_redis_caches = new HashMap();
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			loadConfig(); //���ػ���������Ϣ
//			loadData(null); //���س�ʼ������
//		}catch (Exception ex) {
//			log.error("Redis�����ʼ������");
//			ex.printStackTrace();
//		}
//	}
	
	/**
	 * �������ݿ����ñ�cfg_redis_load���õı���������ݣ�����Ϊnull��ʾĬ�ϼ�������
	 * xuzq
	 * @param redisLoadCodes
	 */
//	public static void loadData(String[] redisLoadCodes) {
//		try {
//			if (redisLoadCodes == null||redisLoadCodes.length==0) {
//				System.out.println("δ����[���ػ������]��Ĭ�ϼ���ȫ�����û��棡");
//				loadAll();
//			} else if (redisLoadCodes.length != 0) {
//				String loadCode = "";
//				for (int i = 0; i < redisLoadCodes.length; i++) {
//					loadCode = redisLoadCodes[i];
//					load(loadCode);
//				}
//			}
//		} catch (Throwable ex) {
//			log.error("װ������ʧ��", ex);
//			throw new RuntimeException(ex);
//		}
//		System.out.println("Redis�����ʼ��������");
//	}
	
//	public static void loadAll() throws Exception {
//		Set set = all_redis_caches.keySet();
//		Iterator it = set.iterator();
//		while (it.hasNext()) {
//			String code = (String) it.next();
//			CFG_REDIS_LOADBean ld = (CFG_REDIS_LOADBean) all_redis_caches
//					.get(code);
//			if (ld.getIfInit() != 0) {
//				load(code);
//			}
//		}
//	}

	public static void load(String code) throws Exception {		
		if (all_redis_caches.containsKey(code)) {
			CFG_REDIS_LOADBean lb = (CFG_REDIS_LOADBean) all_redis_caches
					.get(code);
			IRedisLoad ncache = (IRedisLoad) Class.forName(
					lb.getRedisLoadImpl()).newInstance();
			ncache.setCacheCode(code);
			ncache.setGroup(lb.getBelongGroup());
			ncache.load(lb);
		} else {
			log.error("û�����ô��룺��" + code + "����Ӧ�Ļ���򲻿��ã�");
		}
	}
	
//	public static void loadConfig() throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		CFG_REDIS_LOADBean[] caches = sv.getRedisCache();
//		for (int i = 0; i < caches.length; i++) {
//			all_redis_caches.put(caches[i].getRedisLoadCode(), caches[i]);
//		}
//	}
	
}
