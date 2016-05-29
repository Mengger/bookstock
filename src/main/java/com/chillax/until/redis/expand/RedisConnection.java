package com.chillax.until.redis.expand;

import com.chillax.until.redis.clients.jedis.Jedis;

/***********************************************************
 * @Title: RedisConnection.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-18 ÉÏÎç01:21:00 
 * @version:1.0
 **********************************************************/
public class RedisConnection extends Jedis{
    public String connection_name = "";
	/**
	 * @param host
	 */
	public RedisConnection(String host,long port) {
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
	
	public void write(){
		
	}
	public void read(){
		
	}

}
