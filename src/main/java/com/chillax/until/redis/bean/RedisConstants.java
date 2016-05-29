package com.chillax.until.redis.bean;

/***********************************************************
 * @Title: RedisConstants.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-20 ����08:59:44 
 * @version:1.0
 **********************************************************/
public class RedisConstants {
	public static final  int READ_ONLY_CONNECTION = 1;
	public static final  int WRITE_ONLY_CONNECTION = 2;
	public static final  int PERSIST_ONLYL_CONNECTION = 3;
	public static final  boolean NEED_WRITE_SYNCHRONIZE = false;
	public static final  String REDIS_SERVER_SHARED_MAXACTIVE = "MaxActive";
	public static final  String REDIS_SERVER_SHARED_MAXIDLE = "MaxIdle";
	public static final  String REDIS_SERVER_SHARED_MAXWAIT = "MaxWait";
	public static final  String REDIS_SERVER_SHARED_TESTONBORROW = "TestOnBorrow";
	public static final  String REDIS_SERVER_SHARED_NEED_SYN = "NeedWriteSYN";
	public static final  String REDIS_CACHE_CODE_SUFFIX = "^MICROREDIS";
	public static final  String PERSIST_CHANNEL = "PERSIST_CHANNEL";
	public static final String PERSIST_SEPARATOR = "#";
	public static final String REDIS_BELONG_GROUP = "BASE";
	
	public static final String REDIS_LOAD_CODE_CONFIG = "CFG_REDIS_LOAD";
}
