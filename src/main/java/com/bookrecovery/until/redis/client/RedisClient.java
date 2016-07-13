package com.bookrecovery.until.redis.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bookrecovery.until.redis.bean.CfgRedisServerBean;
import com.bookrecovery.until.redis.bean.RedisConstants;
import com.bookrecovery.until.redis.clients.jedis.Jedis;
import com.bookrecovery.until.redis.clients.jedis.JedisPool;
import com.bookrecovery.until.redis.clients.jedis.JedisPoolConfig;
import com.bookrecovery.until.redis.clients.jedis.JedisShardInfo;
import com.bookrecovery.until.redis.clients.jedis.ShardedJedis;
import com.bookrecovery.until.redis.clients.jedis.ShardedJedisPool;
import com.bookrecovery.until.redis.expand.IRedisConnection;
import com.bookrecovery.until.redis.expand.PersistThread;
import com.bookrecovery.until.redis.expand.RedisConnection;
import com.bookrecovery.until.redis.util.SerializeUtil;


/***********************************************************
 * @Title: RedisClient.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-14 上午01:50:13
 * @version:1.0
 **********************************************************/
public class RedisClient {
	
	public static void init(){};
	public static final Map loadInfoCache = new ConcurrentHashMap();
	private static transient Log log = LogFactory.getLog(RedisClient.class);
		
	/**
	 * 非切片链接池
	 */
	private static JedisPool jedisPool;
	/**
	 * 切片客户端链接
	 */
	private static ShardedJedis shardedJedis;
	
	private static int INDEX = 1;

	private static boolean NEED_WRITE_SYNCHRONIZE = false;
	/**
	 * 切片链接池
	 */
	private static ShardedJedisPool[] readConnectionPools;
	private static ShardedJedisPool[] writeConnectionPools;
	private static ShardedJedisPool[] persistConnectionPools;
	private static HashMap readPoolMap = new HashMap();
	private static HashMap writePoolMap = new HashMap();
	private static HashMap persistPoolMap = new HashMap();
	static {
		//初始化连接池
		try {
			initialShardedPool();
			startPersistListener(); //开启持久化监听器
//			loadDefaultCache();  //加载默认缓存配置
		} catch (Exception ex) {
			log.error("初始化REDIS连接池异常",ex);
			throw new RuntimeException(ex);
		}
		//开始加载数据
//		try {
//			loadData();
//		} catch (Exception ex) {
//			log.error("加载REDISE数据异常",ex);
//			throw new RuntimeException(ex);
//		}
	}

	public RedisClient() throws Exception {
		// initialPool();
		// initialShardedPool();
		// shardedJedis = shardedJedisPool.getResource();
		// Jedis jedis = jedisPool.getResource();
	}

	private void initialPool() throws Exception {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		CFG_REDIS_PARAMETERBean[] para = sv.getRedisConfig("DEFAULT");
//		CFG_REDIS_PARAMETERBean[] para;
//		for (int i = 0; i < para.length; i++) {
//			if (para[i].getParameterName().equalsIgnoreCase("MaxActive"))
//				config.setMaxActive(Integer.parseInt(para[i]
//						.getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase("MaxIdle"))
//				config
//						.setMaxIdle(Integer.parseInt(para[i]
//								.getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase("MaxWait"))
//				config
//						.setMaxWait(Integer.parseInt(para[i]
//								.getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase("TestOnBorrow"))
//				config.setTestOnBorrow(Boolean.getBoolean(para[i]
//						.getParameterValue()));
//			
//		}
		// config.setMaxActive(20);
		// config.setMaxIdle(5);
		// config.setMaxWait(1000l);
		// config.setTestOnBorrow(false);

		jedisPool = new JedisPool(config, "localhost", 6379);
	}

	/**
	 * 初始化切片池
	 */
	private static void initialShardedPool() throws Exception {
//		long start = System.nanoTime();
//		// 池基本配置 
//		JedisPoolConfig config = new JedisPoolConfig(); 
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
////		IRedisBaseSV sv = new RedisBaseSVImpl();
//		CFG_REDIS_PARAMETERBean[] para = sv.getRedisConfig("DEFAULT");
//		for (int i = 0; i < para.length; i++) {
//			if (para[i].getParameterName().equalsIgnoreCase(RedisConstants.REDIS_SERVER_SHARED_MAXACTIVE))
//				config.setMaxActive(Integer.parseInt(para[i]
//						.getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase(RedisConstants.REDIS_SERVER_SHARED_MAXIDLE))
//				config
//						.setMaxIdle(Integer.parseInt(para[i]
//								.getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase(RedisConstants.REDIS_SERVER_SHARED_MAXWAIT))
//				config.setMaxWait(Long.parseLong(para[i].getParameterValue()));
//			if (para[i].getParameterName().equalsIgnoreCase(RedisConstants.REDIS_SERVER_SHARED_TESTONBORROW))
//				config.setTestOnBorrow(Boolean.getBoolean(para[i]
//						.getParameterValue()));
//			
//			if (para[i].getParameterName().equalsIgnoreCase(RedisConstants.REDIS_SERVER_SHARED_NEED_SYN))
//				NEED_WRITE_SYNCHRONIZE = Boolean.parseBoolean(para[i].getParameterValue()); // Boolean.getBoolean不对
//		}
//
//		// slave链接
//		CFG_REDIS_SERVERBean[] servers = sv.getRedisServer();
//		HashMap map = new HashMap();
//		for (int i = 0; i < servers.length; i++) {
//			map.put(servers[i].getBelongGroup().toUpperCase(), servers[i].getBelongGroup().toUpperCase());
//		}
//		List list = new ArrayList(map.values());
//		readConnectionPools = new ShardedJedisPool[list.size()];
//		writeConnectionPools = new ShardedJedisPool[list.size()];
//		persistConnectionPools = new ShardedJedisPool[list.size()];
//		for (int i = 0; i < list.size(); i++) {
//			List<JedisShardInfo> readShards = new ArrayList<JedisShardInfo>();
//			List<JedisShardInfo> writeShards = new ArrayList<JedisShardInfo>();
//			List<JedisShardInfo> persistShards = new ArrayList<JedisShardInfo>();
//			for (int j = 0; j < servers.length; j++) {
//				if (servers[j].getBelongGroup().equalsIgnoreCase(
//						String.valueOf(list.get(i)))
//						&& servers[j].getUseType().equalsIgnoreCase(
//								String.valueOf(RedisConstants.READ_ONLY_CONNECTION))) {
//					//先测试该连接是否可用
//					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
//							(int) servers[j].getServerPort(),decryption(servers[j].getRequirepass()));
//					if(connectionTest) {
//						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),(int) servers[j].getServerPort(),servers[j].getRemark());
//						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
//						readShards.add(jds);
//					}
//				} else if (servers[j].getBelongGroup().equalsIgnoreCase(
//						String.valueOf(list.get(i)))
//						&& servers[j].getUseType().equalsIgnoreCase(
//								String.valueOf(RedisConstants.WRITE_ONLY_CONNECTION))) {
//					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
//							(int) servers[j].getServerPort(),decryption(servers[j].getRequirepass()));
//					if(connectionTest) {
//						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),(int) servers[j].getServerPort(),servers[j].getRemark());
//						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
//						writeShards.add(jds);
//					}
//				}else if (servers[j].getBelongGroup().equalsIgnoreCase(
//						String.valueOf(list.get(i)))
//						&& servers[j].getUseType().equalsIgnoreCase(
//								String.valueOf(RedisConstants.PERSIST_ONLYL_CONNECTION))) {
//					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
//							(int) servers[j].getServerPort(),decryption(servers[j].getRequirepass()));
//					if(connectionTest) {
//						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),(int) servers[j].getServerPort(),servers[j].getRemark());
//						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
//						persistShards.add(jds);
//					}
//				}
//			}
//
//			// 构造池
//			readConnectionPools[i] = new ShardedJedisPool(config, readShards);
//			writeConnectionPools[i] = new ShardedJedisPool(config, writeShards);
//			persistConnectionPools[i] = new ShardedJedisPool(config, persistShards);
//			readPoolMap.put(list.get(i), readConnectionPools[i]);
//			writePoolMap.put(list.get(i), writeConnectionPools[i]);
//			persistPoolMap.put(list.get(i), persistConnectionPools[i]);
//		}
//		long end = System.nanoTime();
//		log.debug("初始化连接池用时：" + (end-start));
	}

	public static RedisConnection getConnection(String code) throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		cfgRedisServerBean server = sv.getServerFromCode(code);
		CfgRedisServerBean server=null;
		if (server == null) {
			log.error("根据编码：" + code + "不能找到对应的redis服务器资源");
			throw new Exception("根据编码：" + code + "不能找到对应的redis服务器资源");
		}
		RedisConnection conn = null;
		conn.setConnection_name(code);
		return conn;
	}


	/**
	 * 获取一个读写分离的连接
	 * 
	 * @param code
	 * @param connection_type
	 *            1:只读；2：只写
	 * @return
	 * @throws Exception
	 */
	public static IRedisConnection getConnection(String code,
			int connection_type) throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		CFG_REDIS_SERVERBean server = sv.getServerFromCode(code);
		CfgRedisServerBean server=null;
		if (server == null) {
			log.error("根据编码：" + code + "不能找到对应的redis服务器资源");
			throw new Exception("根据编码：" + code + "不能找到对应的redis服务器资源");
		}
		IRedisConnection conn=null;

//		if (connection_type == RedisConstants.WRITE_ONLY_CONNECTION) {
//			conn = new WriteConnection(server.getServerIp(), server
//					.getServerPort());
//		} else {
//			conn = new ReadConnection(server.getServerIp(), server
//					.getServerPort());
//		}
		// RedisConnection(server.getServerIp(),(int)server.getServerPort());
//		conn.setConnection_name(code);

		// JedisPool pool = new JedisPool(config, server.getServerIp(),
		// server.getServerPort(), 100000);

		return conn;
	}
	
	private static Boolean TestConnection(String host,int port,String passwd){
		Boolean rtn = Boolean.FALSE;
		JedisShardInfo jsd = new JedisShardInfo(host,port);
		if(null != passwd && !"".equals(passwd.trim())) jsd.setPassword(passwd);
		Jedis jd = null;
		try{
			jd = new Jedis(jsd);
			jd.set("AILK_REDIS_CONNECT_TEST", "TRUE");
			rtn = Boolean.TRUE;
		}
		catch(Exception ex){
			if(log.isDebugEnabled()){
				log.error("【调试】,"+host+":"+port+"拒绝连接！"+ex.getMessage());
			}
		}finally{
			if(null != jd) jd.disconnect();
		}
		return rtn;
	}
	private static ShardedJedis getNewConnection(String code, int connection_type)
			throws Exception {
		if (connection_type == RedisConstants.READ_ONLY_CONNECTION) {
			ShardedJedisPool rp = (ShardedJedisPool) readPoolMap.get(code.toUpperCase());
			return rp.getResource();
		} else if(connection_type == RedisConstants.WRITE_ONLY_CONNECTION){
			ShardedJedisPool rp = (ShardedJedisPool) writePoolMap.get(code);
			return rp.getResource();
		}else {
			ShardedJedisPool rp = (ShardedJedisPool) persistPoolMap.get(code);
			return rp.getResource();
		}
	}
		
	
    /**
	 * redis写出
	 * @param key
	 * @param value
	 * @throws Exception
	 */
    public static void write(String group,String key,String value)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
        	if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key, value);
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
            	writeHandler.set(key, value);
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    	
    }
    /**
     * 向group所在组中写入  自定义数据（map） 对应的主键为key
	 * redis写入
	 * @param key
	 * @param value
	 * @throws Exception
	 */
    public static void write(String group,String key,Map map)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		byte[] bm = SerializeUtil.serialize(map);
        	if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		writeHandler.set(key.getBytes(), bm);
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    
    /**
     * 向group所在组中写入  自定义数据（object） 对应的主键为key
	 * redis写入
	 * @param key
	 * @param value
	 * @throws Exception
	 */
    public static void write(String group,String key,Object obj)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		byte[] bm = SerializeUtil.serialize(obj);
        	if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		writeHandler.set(key.getBytes(), bm);
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    
    /**
     * 向group所在组中写入,key与组组合作为新的key  自定义数据（object） 对应的主键为key
	 * redis写入
	 * @param key
	 * @param value
	 * @throws Exception
	 */
    public static void writeObject(String group,String key,Object obj)throws Exception {
    	ShardedJedis writeHandler = null;
    	String newKey = group + key;
    	try{
    		byte[] bm = SerializeUtil.serialize(obj);
        	if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(newKey.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		writeHandler.set(newKey.getBytes(), bm);
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    
    /**
	 * redis写入
	 * @param key
	 * @param value
	 * @throws Exception
	 */
    public static void writeBatch(String group,String redisCode,Map map)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		String newKey = redisCode+"^MICROREDIS";
        	byte[] bm = SerializeUtil.serialize(map);
        	if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(newKey.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		writeHandler.set(newKey.getBytes(), bm);
        	}
    	}finally {
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    /**
     * @param group
     * @param redisCode
     * @param key
     * @param value
     * @throws Exception
     */
    public static void writeByCode(String group,String redisCode,String key,String value)throws Exception {
    	ShardedJedis writeHandler = null;
    	try {
    		if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.put(key, value);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		Map oldData = readBatch(group,redisCode);
    			oldData.put(key, value);
    	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    /**
     * 写入对象
     * @param group
     * @param redisCode
     * @param key
     * @param value
     * @throws Exception
     */
    public static void writeByCode(String group,String redisCode,String key,Object obj)throws Exception {
//    	String newKey = redisCode+RedisConstants.REDIS_CACHE_CODE_SUFFIX;
    	ShardedJedis writeHandler = null;
    	try{
    		if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.put(key, obj);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		Map oldData = readBatch(group,redisCode);
    			oldData.put(key, obj);
    	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    /**
     * redis缓存读取 一个rediscode所缓存的所有数据（REDIS服务端加载在map中）
     * @param key
     * @return
     * @throws Exception
     */
    public static Map readBatch(String group,String redisCode)throws Exception {
    	ShardedJedis readHandler = null;
    	try{
    		readHandler = RedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
    		byte[] bm = readHandler.get(redisCode.getBytes());
        	if(bm == null) return null;
        	Map map = (Map)SerializeUtil.unserialize(bm);
        	return map;
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
    
    
    public static Object readObject(String group,String redisCode)throws Exception {
    	ShardedJedis readHandler = null;
    	try{
    		readHandler = RedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
    		String key = group + redisCode; //组合key
        	byte[] bm = readHandler.get(key.getBytes());
        	if(bm == null) return null;
        	Object obj = SerializeUtil.unserialize(bm);
        	return obj;
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
    
    
    public static String readString(String group,String key)throws Exception {
    	ShardedJedis readHandler = null;
    	try{
    		readHandler = RedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
        	String v = readHandler.get(key);
        	return v;
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
    /**
     * redis对象移除
     * @param group
     * @param key
     * @throws Exception
     */
    public static void remove(String group,String redisCode)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	writeHandler.del(redisCode);
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    
    //删除redisCode对应的Map里面的对象
    public static void removeByCode(String group,String redisCode,String key)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(RedisClient.class){
        			writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.remove(key);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        		Map oldData = readBatch(group,redisCode);
    			oldData.remove(key);
    	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
    
    //清空缓存
    public static void removeAll(String group)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = RedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	Jedis jd = (Jedis)(writeHandler.getAllShards().toArray()[0]);
        	jd.flushDB();
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    }
    /**
     * redis是否包含该对象
     * @param group
     * @param key
     * @return
     * @throws Exception
     */
    public static  boolean containsKey(String group,String key) throws Exception{
    	ShardedJedis readHandler = null;
    	try{
    		readHandler = RedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
        	boolean flag = readHandler.exists(key) || readHandler.exists(key.getBytes());
        	releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
        	return flag;
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
    public void getAll(String group,String key) throws Exception{
    	ShardedJedis readHandler = RedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
//    	return readHandler.
    }
	public static void refresh() throws Exception {
		synchronized (readConnectionPools) {
			synchronized (writeConnectionPools) {
				try {
					initialShardedPool();
				} catch (Exception ex) {
					log.error("刷新异常!",ex);
				}
			}
		}
	}
	
	/**
	 * add by xuzq@2012-11-1
	 * 根据组将连接释放，重新放回连接池,解决多个线程频繁取获连接导致连接池连接数不够用的问题
	 * @para gourp 组名
	 * @para connection_type 连接类型：1.读2.写
	 */
	public static void releaseConnection(ShardedJedis jedis, String group, int connection_type) throws Exception {
		ShardedJedisPool rp = null;
		if(connection_type == RedisConstants.READ_ONLY_CONNECTION) {
			rp = (ShardedJedisPool) readPoolMap.get(group.toUpperCase());
		}else if(connection_type == RedisConstants.WRITE_ONLY_CONNECTION) {
			rp = (ShardedJedisPool) writePoolMap.get(group.toUpperCase());
		}else if(connection_type == RedisConstants.PERSIST_ONLYL_CONNECTION) {
			rp =(ShardedJedisPool)persistPoolMap.get(group.toUpperCase());
		}
		rp.returnResource(jedis);
	}
	
	//从持久化连接池中获得一个持久化连接
	public static Jedis getPersistConnection() throws Exception {
		String key = generateKey();
		ShardedJedis sharedJedis = RedisClient.getNewConnection(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.PERSIST_ONLYL_CONNECTION);
		return sharedJedis.getShard(key);
		
	}
	
	//释放持久化连接
	public static void releaseConnection(Jedis jedis,String group) throws Exception {
		ShardedJedisPool rp = (ShardedJedisPool)persistPoolMap.get(group.toUpperCase());
		rp.returnResourceObject(jedis);
	}
	
	//启动持久化线程
	public static void startPersistListener() {
		Thread persistThread = new Thread(new PersistThread());
		persistThread.setDaemon(true);
		persistThread.start();
	}
	
	public static String generateKey() {
		return String.valueOf(Thread.currentThread().getId() + "_" + (INDEX++));
	}
	
	//RC2解密
	public static String decryption(String cipher) throws Exception{
//		if(StringUtils.isBlank(cipher)) return "";
		return cipher;
	}
	
	//创建默认的加载配置缓存
	public static void loadDefaultCache() throws Exception{
		if(!containsKey(RedisConstants.REDIS_BELONG_GROUP ,RedisConstants.REDIS_LOAD_CODE_CONFIG)) {
			write(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.REDIS_LOAD_CODE_CONFIG, new HashMap());
			log.debug("默认缓存不存在，创建默认配置缓存成功:加载组：【" + RedisConstants.REDIS_BELONG_GROUP + "】，加载编码：【" + RedisConstants.REDIS_LOAD_CODE_CONFIG + "】");
		}
	}
	
}
