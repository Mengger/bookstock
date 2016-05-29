package com.chillax.until.redis.expand;

import com.chillax.until.redis.clients.jedis.Jedis;

/***********************************************************
 * @Title: ReadConnection.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-18 上午02:56:03 
 * @version:1.0
 **********************************************************/
public class ReadConnection  extends Jedis implements IRedisConnection{
    public String connection_name = "";
	/**
	 * @param host
	 */
	public ReadConnection(String host,long port) {
		super(host,(int)port);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the connection_name
	 */
	public String getConnection_name() {
		return connection_name;
	}
	/**
	 * @param connectionName the connection_name to set
	 */
	public void setConnection_name(String connectionName) {
		connection_name = connectionName;
	}
	
	public Object read(String key){
		return this.get(key);
	}
	public void write(String key,String value) throws Exception{
		throw new Exception("未定义的方法");
	}
}
