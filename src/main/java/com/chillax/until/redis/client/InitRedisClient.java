package com.chillax.until.redis.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chillax.until.redis.bean.CfgRedisServerBean;
import com.chillax.until.redis.bean.RedisConstants;
import com.chillax.until.redis.clients.jedis.Jedis;
import com.chillax.until.redis.clients.jedis.JedisPool;
import com.chillax.until.redis.clients.jedis.JedisPoolConfig;
import com.chillax.until.redis.clients.jedis.JedisShardInfo;
import com.chillax.until.redis.clients.jedis.ShardedJedis;
import com.chillax.until.redis.clients.jedis.ShardedJedisPool;
import com.chillax.until.redis.expand.PersistThread;
import com.chillax.until.redis.util.SerializeUtil;

/***********************************************************
 * @Title: RedisClient.java
 * @Description:
 * @author:suizhifa
 * @create_data:2015-2-11 下午01:50:13
 * @version:1.0
 **********************************************************/
public class InitRedisClient {
	
	public static void init(){};
	public static final Map loadInfoCache = new ConcurrentHashMap();
	private static transient Log log = LogFactory.getLog(InitRedisClient.class);
		
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
	
	public static int REDIS_SERVER_SHARED_MAXACTIVE = 20;
	public static int REDIS_SERVER_SHARED_MAXIDLE = 10;
	public static long REDIS_SERVER_SHARED_MAXWAIT = 1000;
	public static boolean REDIS_SERVER_SHARED_TESTONBORROW = false;
	public static boolean REDIS_SERVER_SHARED_NEED_SYN = true;
	
	static {
		//初始化连接池
		try {
			initialShardedPool();
//			startPersistListener(); //开启持久化监听器
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

	public InitRedisClient() throws Exception {
		// initialPool();
		// initialShardedPool();
		// shardedJedis = shardedJedisPool.getResource();
		// Jedis jedis = jedisPool.getResource();
	}

	/**
	 * 初始化切片池
	 */
	private static void initialShardedPool() throws Exception {
		long start = System.nanoTime();
		
		// 池基本配置 
		initRedisConfig();
		JedisPoolConfig config = new JedisPoolConfig(); 
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory.getService(IRedisBaseSV.class);
		config.setMaxActive(REDIS_SERVER_SHARED_MAXACTIVE);
		config.setMaxIdle(REDIS_SERVER_SHARED_MAXIDLE);
		config.setMaxWait(REDIS_SERVER_SHARED_MAXWAIT);
		config.setTestOnBorrow(REDIS_SERVER_SHARED_TESTONBORROW);
		
		// slave链接
//		CFG_REDIS_SERVERBean[] servers = sv.getRedisServer();
		CfgRedisServerBean[] servers=initRedisServer();
		HashMap map = new HashMap();
		for (int i = 0; i < servers.length; i++) {
			map.put(servers[i].getBelongGroup().toUpperCase(), servers[i].getBelongGroup().toUpperCase());
		}
		List list = new ArrayList(map.values());
		readConnectionPools = new ShardedJedisPool[list.size()];
		writeConnectionPools = new ShardedJedisPool[list.size()];
		persistConnectionPools = new ShardedJedisPool[list.size()];
		for (int i = 0; i < list.size(); i++) {
			List<JedisShardInfo> readShards = new ArrayList<JedisShardInfo>();
			List<JedisShardInfo> writeShards = new ArrayList<JedisShardInfo>();
			List<JedisShardInfo> persistShards = new ArrayList<JedisShardInfo>();
			for (int j = 0; j < servers.length; j++) {
				if (servers[j].getBelongGroup().equalsIgnoreCase(
						String.valueOf(list.get(i)))
						&& servers[j].getUseType().equalsIgnoreCase(
								String.valueOf(RedisConstants.READ_ONLY_CONNECTION))) {
					//先测试该连接是否可用
					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
							Integer.parseInt(servers[j].getServerPort()),decryption(servers[j].getRequirepass()));
					if(connectionTest) {
						
						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),Integer.parseInt(servers[j].getServerPort()),servers[j].getRemark());
						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
						readShards.add(jds);
					}
				} else if (servers[j].getBelongGroup().equalsIgnoreCase(
						String.valueOf(list.get(i)))
						&& servers[j].getUseType().equalsIgnoreCase(
								String.valueOf(RedisConstants.WRITE_ONLY_CONNECTION))) {
					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
							Integer.parseInt(servers[j].getServerPort()),decryption(servers[j].getRequirepass()));
					if(connectionTest) {
						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),Integer.parseInt(servers[j].getServerPort()),servers[j].getRemark());
						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
						writeShards.add(jds);
					}
				}else if (servers[j].getBelongGroup().equalsIgnoreCase(
						String.valueOf(list.get(i)))
						&& servers[j].getUseType().equalsIgnoreCase(
								String.valueOf(RedisConstants.PERSIST_ONLYL_CONNECTION))) {
					Boolean connectionTest = TestConnection(servers[j].getServerIp(),
							Integer.parseInt(servers[j].getServerPort()),decryption(servers[j].getRequirepass()));
					if(connectionTest) {
						JedisShardInfo jds = new JedisShardInfo(servers[j].getServerIp(),Integer.parseInt(servers[j].getServerPort()),servers[j].getRemark());
						if(null != servers[j].getRequirepass() && !"".equals(servers[j].getRequirepass().trim())) jds.setPassword(decryption(servers[j].getRequirepass()));
						persistShards.add(jds);
					}
				}
			}

			// 构造池
			readConnectionPools[i] = new ShardedJedisPool(config, readShards);
			writeConnectionPools[i] = new ShardedJedisPool(config, writeShards);
			persistConnectionPools[i] = new ShardedJedisPool(config, persistShards);
			readPoolMap.put(list.get(i), readConnectionPools[i]);
			writePoolMap.put(list.get(i), writeConnectionPools[i]);
			persistPoolMap.put(list.get(i), persistConnectionPools[i]);
		}
		long end = System.nanoTime();
		log.debug("初始化连接池用时：" + (end-start));
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
//	public static IRedisConnection getConnection(String code,
//			int connection_type) throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		CFG_REDIS_SERVERBean server = sv.getServerFromCode(code);
//		if (server == null) {
//			log.error("根据编码：" + code + "不能找到对应的redis服务器资源");
//			throw new Exception("根据编码：" + code + "不能找到对应的redis服务器资源");
//		}
//		IRedisConnection conn;
//
//		if (connection_type == RedisConstants.WRITE_ONLY_CONNECTION) {
//			conn = new WriteConnection(server.getServerIp(), server
//					.getServerPort());
//		} else {
//			conn = new ReadConnection(server.getServerIp(), server
//					.getServerPort());
//		}
//		// RedisConnection conn = new
//		// RedisConnection(server.getServerIp(),(int)server.getServerPort());
//		conn.setConnection_name(code);
//
//		// JedisPool pool = new JedisPool(config, server.getServerIp(),
//		// server.getServerPort(), 100000);
//
//		return conn;
//	}
//	
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key, value);
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(newKey.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(newKey.getBytes(), bm);
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.put(key, value);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.put(key, obj);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
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
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
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
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
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
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        			Map oldData = readBatch(group,redisCode);
        			oldData.remove(key);
        	    	writeHandler.set(redisCode.getBytes(), SerializeUtil.serialize(oldData));
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
        	boolean flag = readHandler.exists(key) || readHandler.exists(key.getBytes());
        	releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
        	return flag;
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
    public void getAll(String group,String key) throws Exception{
    	ShardedJedis readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
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
		ShardedJedis sharedJedis = InitRedisClient.getNewConnection(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.PERSIST_ONLYL_CONNECTION);
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
		if(cipher==null || "".equals(cipher)) return "";
		return cipher;
	}
	
	//创建默认的加载配置缓存
	public static void loadDefaultCache() throws Exception{
		if(!containsKey(RedisConstants.REDIS_BELONG_GROUP ,RedisConstants.REDIS_LOAD_CODE_CONFIG)) {
			write(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.REDIS_LOAD_CODE_CONFIG, new HashMap());
			log.debug("默认缓存不存在，创建默认配置缓存成功:加载组：【" + RedisConstants.REDIS_BELONG_GROUP + "】，加载编码：【" + RedisConstants.REDIS_LOAD_CODE_CONFIG + "】");
		}
	}
	/**
	 * 根据key的值获取value
	 * @param group
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getValueByKeyAndGroup(String group,String key)throws Exception {
    	ShardedJedis readHandler = null;
    	try{
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
    		byte[] bm = readHandler.get(key.getBytes());
        	if(bm == null) return null;
        	return new String(bm);
    	}finally{
    		if(readHandler != null) releaseConnection(readHandler, group, RedisConstants.READ_ONLY_CONNECTION);
    	}
    	
    }
	/**
	 * 根据key写入value值
	 * @param group
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void writeValueByKeyAndGroup(String group,String key,String value)throws Exception {
    	ShardedJedis writeHandler = null;
    	try {
    		if(NEED_WRITE_SYNCHRONIZE){
        		synchronized(InitRedisClient.class){
        			writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	    	writeHandler.set(key.getBytes(), value.getBytes());
        		}
        	}
        	else
        	{
        		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
    	    	writeHandler.set(key.getBytes(), value.getBytes());
        	}
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    	
    }
	//根据key的值获取value，同时进行自增长
    public static long getCountByKeyAndInc(String group,String key) throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	Jedis jd = (Jedis)(writeHandler.getAllShards().toArray()[0]);
        	return jd.incr(key);
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
	}
    //根据key的值获取value，同时进行自减
    public static long getCountByKeyAndDec(String group,String key) throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	Jedis jd = (Jedis)(writeHandler.getAllShards().toArray()[0]);
        	return jd.decr(key);
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
	}
	//根据pattern模糊匹配查询数据库中的所有keys
	public static Set<String> getAllKeys(String group,String pattern)throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	Jedis jd = (Jedis)(writeHandler.getAllShards().toArray()[0]);
        	return jd.keys(pattern);
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
    }
	//根据配置文件redisConfig配置连接池及主从服务器信息
	public static void initRedisConfig() throws Exception {
		try {
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("redisConfig/redisConfig.properties"));
			String MaxActive=p.getProperty("MaxActive");
			String MaxIdle=p.getProperty("MaxIdle");
			String MaxWait=p.getProperty("MaxWait");
			String TestOnBorrow=p.getProperty("TestOnBorrow");
			String NeedWriteSYN=p.getProperty("NeedWriteSYN");
			if(MaxActive!=null && !"".equals(MaxActive)){
				REDIS_SERVER_SHARED_MAXACTIVE=Integer.parseInt(MaxActive);
			}
			if(MaxIdle!=null && !"".equals(MaxIdle)){
				REDIS_SERVER_SHARED_MAXIDLE=Integer.parseInt(MaxIdle);
			}
			if(MaxWait!=null && !"".equals(MaxWait)){
				REDIS_SERVER_SHARED_MAXWAIT=Long.parseLong(MaxWait);
			}
			if(TestOnBorrow!=null && !"".equals(TestOnBorrow)){
				REDIS_SERVER_SHARED_TESTONBORROW=Boolean.parseBoolean(TestOnBorrow);
			}
			if(NeedWriteSYN!=null && !"".equals(NeedWriteSYN)){
				REDIS_SERVER_SHARED_NEED_SYN=Boolean.parseBoolean(NeedWriteSYN);
			}
		} catch (Throwable ex) {
			throw new Exception("根据配置文件redisConfig配置连接池失败", ex);
		}
	}
	//根据配置文件redisConfig配置连接池及主从服务器信息
	public static CfgRedisServerBean[] initRedisServer()throws Exception{
		try{
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("redisConfig/redisConfig.properties"));
			List<CfgRedisServerBean> list=new ArrayList<CfgRedisServerBean>();
			//加载主服务redis
			int m_count=Integer.parseInt(p.getProperty("M_COUNT"));
			for(int i=0;i<m_count;i++){
				CfgRedisServerBean server=new CfgRedisServerBean();
				String servInfo=p.getProperty("M_SERVER_"+i);
				String[] tmpMServ=servInfo.split(",");
				server.setBelongGroup(tmpMServ[0]);//所属分组
				server.setServerIp(tmpMServ[1]);//redis服务器IP
				server.setServerPort(tmpMServ[2]);//redis服务器端口
				server.setRequirepass(tmpMServ[3]);//redis密码
				server.setUseType(tmpMServ[4]);//redis服务器属性1-只读,2-只写
				list.add(server);
			}
			//加载从服务redis
			int s_count=Integer.parseInt(p.getProperty("S_COUNT"));
			for(int i=0;i<s_count;i++){
				CfgRedisServerBean server=new CfgRedisServerBean();
				String servInfo=p.getProperty("S_SERVER_"+i);
				String[] tmpMServ=servInfo.split(",");
				server.setBelongGroup(tmpMServ[0]);//所属分组
				server.setServerIp(tmpMServ[1]);//redis服务器IP
				server.setServerPort(tmpMServ[2]);//redis服务器端口
				server.setRequirepass(tmpMServ[3]);//redis密码
				server.setUseType(tmpMServ[4]);//redis服务器属性1-只读,2-只写
				list.add(server);
			}
			return list.toArray(new CfgRedisServerBean[0]);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("根据配置文件redisConfig配置服务器信息失败", e);
		}
	}
	
	//根据key值设置超时时间，参数级别为秒
    public static long expire(String group,String key,int timeoutSecond) throws Exception {
    	ShardedJedis writeHandler = null;
    	try{
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
        	Jedis jd = (Jedis)(writeHandler.getAllShards().toArray()[0]);
        	return jd.expire(key, timeoutSecond);
    	}finally{
    		if(writeHandler != null) releaseConnection(writeHandler, group, RedisConstants.WRITE_ONLY_CONNECTION);
    	}
	}
	
}
