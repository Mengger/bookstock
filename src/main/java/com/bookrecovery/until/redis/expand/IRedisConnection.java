package com.bookrecovery.until.redis.expand;

/***********************************************************
 * @Title: IRedisConnection.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-18 上午03:10:06 
 * @version:1.0
 **********************************************************/
public interface IRedisConnection {
	public String connection_name = "";
	/**
	 * @return the connection_name
	 */
	public String getConnection_name() ;
	/**
	 * @param connectionName the connection_name to set
	 */
	public void setConnection_name(String connectionName) ;
	/**
	 * @param key value
	 * @param value
	 */
	public void write(String key,String value)throws Exception;
	/**
	 * @param key
	 * @return
	 */
	public Object read(String key)throws Exception;
	
//	public boolean isValiate() ;
}
