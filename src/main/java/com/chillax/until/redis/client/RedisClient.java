package com.chillax.until.redis.client;

import java.util.HashMap;
import java.util.Map;
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
import com.chillax.until.redis.expand.IRedisConnection;
import com.chillax.until.redis.expand.PersistThread;
import com.chillax.until.redis.expand.RedisConnection;
import com.chillax.until.redis.util.SerializeUtil;


/***********************************************************
 * @Title: RedisClient.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-14 ����01:50:13
 * @version:1.0
 **********************************************************/
public class RedisClient {
	
	public static void init(){};
	public static final Map loadInfoCache = new ConcurrentHashMap();
	private static transient Log log = LogFactory.getLog(RedisClient.class);
		
	/**
	 * ����Ƭ���ӳ�
	 */
	private static JedisPool jedisPool;
	/**
	 * ��Ƭ�ͻ�������
	 */
	private static ShardedJedis shardedJedis;
	
	private static int INDEX = 1;

	private static boolean NEED_WRITE_SYNCHRONIZE = false;
	/**
	 * ��Ƭ���ӳ�
	 */
	private static ShardedJedisPool[] readConnectionPools;
	private static ShardedJedisPool[] writeConnectionPools;
	private static ShardedJedisPool[] persistConnectionPools;
	private static HashMap readPoolMap = new HashMap();
	private static HashMap writePoolMap = new HashMap();
	private static HashMap persistPoolMap = new HashMap();
	static {
		//��ʼ�����ӳ�
		try {
			initialShardedPool();
			startPersistListener(); //�����־û�������
//			loadDefaultCache();  //����Ĭ�ϻ�������
		} catch (Exception ex) {
			log.error("��ʼ��REDIS���ӳ��쳣",ex);
			throw new RuntimeException(ex);
		}
		//��ʼ��������
//		try {
//			loadData();
//		} catch (Exception ex) {
//			log.error("����REDISE�����쳣",ex);
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
		// �ػ�������
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
	 * ��ʼ����Ƭ��
	 */
	private static void initialShardedPool() throws Exception {
//		long start = System.nanoTime();
//		// �ػ������� 
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
//				NEED_WRITE_SYNCHRONIZE = Boolean.parseBoolean(para[i].getParameterValue()); // Boolean.getBoolean����
//		}
//
//		// slave����
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
//					//�Ȳ��Ը������Ƿ����
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
//			// �����
//			readConnectionPools[i] = new ShardedJedisPool(config, readShards);
//			writeConnectionPools[i] = new ShardedJedisPool(config, writeShards);
//			persistConnectionPools[i] = new ShardedJedisPool(config, persistShards);
//			readPoolMap.put(list.get(i), readConnectionPools[i]);
//			writePoolMap.put(list.get(i), writeConnectionPools[i]);
//			persistPoolMap.put(list.get(i), persistConnectionPools[i]);
//		}
//		long end = System.nanoTime();
//		log.debug("��ʼ�����ӳ���ʱ��" + (end-start));
	}

	public static RedisConnection getConnection(String code) throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		cfgRedisServerBean server = sv.getServerFromCode(code);
		CfgRedisServerBean server=null;
		if (server == null) {
			log.error("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
			throw new Exception("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
		}
		RedisConnection conn = null;
		conn.setConnection_name(code);
		return conn;
	}


	/**
	 * ��ȡһ����д���������
	 * 
	 * @param code
	 * @param connection_type
	 *            1:ֻ����2��ֻд
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
			log.error("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
			throw new Exception("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
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
				log.error("�����ԡ�,"+host+":"+port+"�ܾ����ӣ�"+ex.getMessage());
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
	 * redisд��
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
     * ��group��������д��  �Զ������ݣ�map�� ��Ӧ������Ϊkey
	 * redisд��
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
     * ��group��������д��  �Զ������ݣ�object�� ��Ӧ������Ϊkey
	 * redisд��
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
     * ��group��������д��,key���������Ϊ�µ�key  �Զ������ݣ�object�� ��Ӧ������Ϊkey
	 * redisд��
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
	 * redisд��
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
     * д�����
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
     * redis�����ȡ һ��rediscode��������������ݣ�REDIS����˼�����map�У�
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
    		String key = group + redisCode; //���key
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
     * redis�����Ƴ�
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
    
    //ɾ��redisCode��Ӧ��Map����Ķ���
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
    
    //��ջ���
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
     * redis�Ƿ�����ö���
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
					log.error("ˢ���쳣!",ex);
				}
			}
		}
	}
	
	/**
	 * add by xuzq@2012-11-1
	 * �����齫�����ͷţ����·Ż����ӳ�,�������߳�Ƶ��ȡ�����ӵ������ӳ������������õ�����
	 * @para gourp ����
	 * @para connection_type �������ͣ�1.��2.д
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
	
	//�ӳ־û����ӳ��л��һ���־û�����
	public static Jedis getPersistConnection() throws Exception {
		String key = generateKey();
		ShardedJedis sharedJedis = RedisClient.getNewConnection(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.PERSIST_ONLYL_CONNECTION);
		return sharedJedis.getShard(key);
		
	}
	
	//�ͷų־û�����
	public static void releaseConnection(Jedis jedis,String group) throws Exception {
		ShardedJedisPool rp = (ShardedJedisPool)persistPoolMap.get(group.toUpperCase());
		rp.returnResourceObject(jedis);
	}
	
	//�����־û��߳�
	public static void startPersistListener() {
		Thread persistThread = new Thread(new PersistThread());
		persistThread.setDaemon(true);
		persistThread.start();
	}
	
	public static String generateKey() {
		return String.valueOf(Thread.currentThread().getId() + "_" + (INDEX++));
	}
	
	//RC2����
	public static String decryption(String cipher) throws Exception{
//		if(StringUtils.isBlank(cipher)) return "";
		return cipher;
	}
	
	//����Ĭ�ϵļ������û���
	public static void loadDefaultCache() throws Exception{
		if(!containsKey(RedisConstants.REDIS_BELONG_GROUP ,RedisConstants.REDIS_LOAD_CODE_CONFIG)) {
			write(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.REDIS_LOAD_CODE_CONFIG, new HashMap());
			log.debug("Ĭ�ϻ��治���ڣ�����Ĭ�����û���ɹ�:�����飺��" + RedisConstants.REDIS_BELONG_GROUP + "�������ر��룺��" + RedisConstants.REDIS_LOAD_CODE_CONFIG + "��");
		}
	}
	
}
