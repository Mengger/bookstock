package com.bookrecovery.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

public class SpringRedisCache implements Cache{
	
	private Logger logger = LoggerFactory.getLogger(SpringRedisCache.class);
	
	private RedisTemplate<String, Object> redisTemplate;
	private String name;
    private Long expire;
    
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate){
    	this.redisTemplate=redisTemplate;
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    public void setExpire(Long expire){
    	this.expire=expire;
    }
    private static final String DELETE_SCRIPT_IN_LUA = "local list={%s} "
            + "for _,key in pairs(list) do " + "    local keys = redis.call('keys', key) "
            + "    for i,k in ipairs(keys) do " + "        redis.call('del', k) " + "    end "
            + "end";
    private static final String MUTILKEYS = "^'[0-9a-zA-Z\\-_(\\*)*]+'(,'[0-9a-zA-Z\\-_(\\*)*]+')*$";
    
	public String getName() {
		return this.name;
	}

	public Object getNativeCache() {
		return this.redisTemplate;
	}

	public ValueWrapper get(Object key) {
        final String keyf = (String) key;
        Object object = null;
        try {
            object = redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] key = keyf.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return toObject(value);

                }
            });
            return (object != null ? new SimpleValueWrapper(object) : null);
        } catch (Exception e) {
            return null;
        }
    }

	public void put(Object key, Object value) {
        final String keyf = (String) key;
        final Object valuef = value;
        try {
            redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyb = keyf.getBytes();
                    byte[] valueb = toByteArray(valuef);
                    connection.set(keyb, valueb);
                    if (expire != null && expire > 0) {
                        connection.expire(keyb, expire);
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

	public void evict(Object key) {
        try {
            if (key instanceof String) {
                final String keyf = (String) key;
                if (keyf.matches(MUTILKEYS)) {
                    redisTemplate.execute(new RedisCallback<Long>() {
                        public Long doInRedis(RedisConnection connection)
                                throws DataAccessException {
                            Jedis jedis = (Jedis) connection.getNativeConnection();
                            jedis.eval(String.format(DELETE_SCRIPT_IN_LUA, keyf));
                            logger.error("evict babys");
                            return 1L;
                        }
                    });
                } else if (keyf.contains("*")) {
                    redisTemplate.execute(new RedisCallback<Long>() {
                        public Long doInRedis(RedisConnection connection)
                                throws DataAccessException {
                            Set<byte[]> keys = connection.keys(keyf.getBytes());
                            if(keys == null || keys.size() == 0)
                                return 1L;
                            byte[][] rawKeys = new byte[keys.size()][];
                            int i = 0;
                            for (byte[] key : keys) {
                                rawKeys[i++] = key;
                            }
                            connection.del(rawKeys);
                            return 1L;
                        }
                    });
                } else if(keyf.matches("[0-9a-zA-Z\\-_]+")) {
                    redisTemplate.execute(new RedisCallback<Long>() {
                        public Long doInRedis(RedisConnection connection)
                                throws DataAccessException {
                            return connection.del(keyf.getBytes());
                        }
                    });
                }else {
                    logger.error(keyf);
                    throw new Exception("key's formattor error");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

	public void clear() {
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }
    /**
     * 描述 : <byte[]转Object>. <br>
     * <p>
     * <使用方法说明>
     * </p>
     * 
     * @param bytes
     * @return
     */
    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }   
    /**
     * 描述 : <Object转byte[]>. <br>
     * <p>
     * <使用方法说明>
     * </p>
     * 
     * @param obj
     * @return
     */
    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    

	public <T> T get(Object key, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}
}
