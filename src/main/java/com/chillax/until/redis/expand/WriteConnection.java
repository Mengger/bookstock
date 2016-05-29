package com.chillax.until.redis.expand;

import com.chillax.until.redis.clients.jedis.Jedis;

/***********************************************************
 * @Title: WriteConnection.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-18 ÉÏÎç02:56:20 
 * @version:1.0
 **********************************************************/
public class WriteConnection  extends Jedis implements IRedisConnection{
    public String connection_name = "";
	/**
	 * @param host
	 */
	public WriteConnection(String host,long port) {
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
	
	public void write(String key,String value){
		this.set(key, value);
	}
	public Object read(String key)throws Exception{
		throw new Exception("Î´ï¿½ï¿½ï¿½ï¿½Ä·ï¿½ï¿½ï¿?");
	}
	
//	public boolean isValiate(){
//	}
}

