package com.bookrecovery.until.redis.load;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.ai.appframe2.service.ServiceFactory;
import com.bookrecovery.until.redis.bean.CFG_REDIS_LOADBean;
//import com.chillax.until.redis.service.interfaces.IRedisBaseSV;


/***********************************************************
 * @Title: RedisCacheWork.java 
 * @Description:加载redis自定义缓存数据
 * @author:xuehao
 * @create_data:2012-9-24 上午01:50:04 
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
//			loadConfig(); //加载缓存配置信息
//			loadData(null); //加载初始化数据
//		}catch (Exception ex) {
//			log.error("Redis缓存初始化出错");
//			ex.printStackTrace();
//		}
//	}
	
	/**
	 * 根据数据库配置表cfg_redis_load配置的编码加载数据，参数为null表示默认加载所有
	 * xuzq
	 * @param redisLoadCodes
	 */
//	public static void loadData(String[] redisLoadCodes) {
//		try {
//			if (redisLoadCodes == null||redisLoadCodes.length==0) {
//				System.out.println("未传入[加载缓存编码]，默认加载全部配置缓存！");
//				loadAll();
//			} else if (redisLoadCodes.length != 0) {
//				String loadCode = "";
//				for (int i = 0; i < redisLoadCodes.length; i++) {
//					loadCode = redisLoadCodes[i];
//					load(loadCode);
//				}
//			}
//		} catch (Throwable ex) {
//			log.error("装载数据失败", ex);
//			throw new RuntimeException(ex);
//		}
//		System.out.println("Redis缓存初始化结束！");
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
			log.error("没有配置代码：【" + code + "】对应的缓存或不可用！");
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
