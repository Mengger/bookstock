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
 * @create_data:2015-2-11 ����01:50:13
 * @version:1.0
 **********************************************************/
public class InitRedisClient {
	
	public static void init(){};
	public static final Map loadInfoCache = new ConcurrentHashMap();
	private static transient Log log = LogFactory.getLog(InitRedisClient.class);
		
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
	
	public static int REDIS_SERVER_SHARED_MAXACTIVE = 20;
	public static int REDIS_SERVER_SHARED_MAXIDLE = 10;
	public static long REDIS_SERVER_SHARED_MAXWAIT = 1000;
	public static boolean REDIS_SERVER_SHARED_TESTONBORROW = false;
	public static boolean REDIS_SERVER_SHARED_NEED_SYN = true;
	
	static {
		//��ʼ�����ӳ�
		try {
			initialShardedPool();
//			startPersistListener(); //�����־û�������
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

	public InitRedisClient() throws Exception {
		// initialPool();
		// initialShardedPool();
		// shardedJedis = shardedJedisPool.getResource();
		// Jedis jedis = jedisPool.getResource();
	}

	/**
	 * ��ʼ����Ƭ��
	 */
	private static void initialShardedPool() throws Exception {
		long start = System.nanoTime();
		
		// �ػ������� 
		initRedisConfig();
		JedisPoolConfig config = new JedisPoolConfig(); 
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory.getService(IRedisBaseSV.class);
		config.setMaxActive(REDIS_SERVER_SHARED_MAXACTIVE);
		config.setMaxIdle(REDIS_SERVER_SHARED_MAXIDLE);
		config.setMaxWait(REDIS_SERVER_SHARED_MAXWAIT);
		config.setTestOnBorrow(REDIS_SERVER_SHARED_TESTONBORROW);
		
		// slave����
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
					//�Ȳ��Ը������Ƿ����
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

			// �����
			readConnectionPools[i] = new ShardedJedisPool(config, readShards);
			writeConnectionPools[i] = new ShardedJedisPool(config, writeShards);
			persistConnectionPools[i] = new ShardedJedisPool(config, persistShards);
			readPoolMap.put(list.get(i), readConnectionPools[i]);
			writePoolMap.put(list.get(i), writeConnectionPools[i]);
			persistPoolMap.put(list.get(i), persistConnectionPools[i]);
		}
		long end = System.nanoTime();
		log.debug("��ʼ�����ӳ���ʱ��" + (end-start));
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
//	public static IRedisConnection getConnection(String code,
//			int connection_type) throws Exception {
//		IRedisBaseSV sv = (IRedisBaseSV) ServiceFactory
//				.getService(IRedisBaseSV.class);
//		CFG_REDIS_SERVERBean server = sv.getServerFromCode(code);
//		if (server == null) {
//			log.error("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
//			throw new Exception("���ݱ��룺" + code + "�����ҵ���Ӧ��redis��������Դ");
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
     * redis�����ȡ һ��rediscode��������������ݣ�REDIS����˼�����map�У�
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
    		readHandler = InitRedisClient.getNewConnection(group, RedisConstants.READ_ONLY_CONNECTION);
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
    		writeHandler = InitRedisClient.getNewConnection(group, RedisConstants.WRITE_ONLY_CONNECTION);
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
    
    //��ջ���
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
     * redis�Ƿ�����ö���
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
		ShardedJedis sharedJedis = InitRedisClient.getNewConnection(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.PERSIST_ONLYL_CONNECTION);
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
		if(cipher==null || "".equals(cipher)) return "";
		return cipher;
	}
	
	//����Ĭ�ϵļ������û���
	public static void loadDefaultCache() throws Exception{
		if(!containsKey(RedisConstants.REDIS_BELONG_GROUP ,RedisConstants.REDIS_LOAD_CODE_CONFIG)) {
			write(RedisConstants.REDIS_BELONG_GROUP, RedisConstants.REDIS_LOAD_CODE_CONFIG, new HashMap());
			log.debug("Ĭ�ϻ��治���ڣ�����Ĭ�����û���ɹ�:�����飺��" + RedisConstants.REDIS_BELONG_GROUP + "�������ر��룺��" + RedisConstants.REDIS_LOAD_CODE_CONFIG + "��");
		}
	}
	/**
	 * ����key��ֵ��ȡvalue
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
	 * ����keyд��valueֵ
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
	//����key��ֵ��ȡvalue��ͬʱ����������
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
    //����key��ֵ��ȡvalue��ͬʱ�����Լ�
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
	//����patternģ��ƥ���ѯ���ݿ��е�����keys
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
	//���������ļ�redisConfig�������ӳؼ����ӷ�������Ϣ
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
			throw new Exception("���������ļ�redisConfig�������ӳ�ʧ��", ex);
		}
	}
	//���������ļ�redisConfig�������ӳؼ����ӷ�������Ϣ
	public static CfgRedisServerBean[] initRedisServer()throws Exception{
		try{
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("redisConfig/redisConfig.properties"));
			List<CfgRedisServerBean> list=new ArrayList<CfgRedisServerBean>();
			//����������redis
			int m_count=Integer.parseInt(p.getProperty("M_COUNT"));
			for(int i=0;i<m_count;i++){
				CfgRedisServerBean server=new CfgRedisServerBean();
				String servInfo=p.getProperty("M_SERVER_"+i);
				String[] tmpMServ=servInfo.split(",");
				server.setBelongGroup(tmpMServ[0]);//��������
				server.setServerIp(tmpMServ[1]);//redis������IP
				server.setServerPort(tmpMServ[2]);//redis�������˿�
				server.setRequirepass(tmpMServ[3]);//redis����
				server.setUseType(tmpMServ[4]);//redis����������1-ֻ��,2-ֻд
				list.add(server);
			}
			//���شӷ���redis
			int s_count=Integer.parseInt(p.getProperty("S_COUNT"));
			for(int i=0;i<s_count;i++){
				CfgRedisServerBean server=new CfgRedisServerBean();
				String servInfo=p.getProperty("S_SERVER_"+i);
				String[] tmpMServ=servInfo.split(",");
				server.setBelongGroup(tmpMServ[0]);//��������
				server.setServerIp(tmpMServ[1]);//redis������IP
				server.setServerPort(tmpMServ[2]);//redis�������˿�
				server.setRequirepass(tmpMServ[3]);//redis����
				server.setUseType(tmpMServ[4]);//redis����������1-ֻ��,2-ֻд
				list.add(server);
			}
			return list.toArray(new CfgRedisServerBean[0]);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("���������ļ�redisConfig���÷�������Ϣʧ��", e);
		}
	}
	
	//����keyֵ���ó�ʱʱ�䣬��������Ϊ��
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
