package com.chillax.until.redis.load;

import java.util.HashMap;
import java.util.Map;

import com.chillax.until.redis.bean.CFG_REDIS_LOADBean;

/***********************************************************
 * @Title: IRedisLoad.java 
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-21 ����02:05:34 
 * @version:1.0
 **********************************************************/
public interface IRedisLoad {
	/**
	   * ˢ��
	   * @throws Exception
	   */
	  public void load(CFG_REDIS_LOADBean lb) throws Exception;

	  /**
	   * @param key Object
	   * @throws Exception
	   * @return Object
	   */
	  public Object getObject(Object key) throws Exception;

	  /**
	   * @param key Object
	   * @throws Exception
	   * @return boolean
	   */
	  public boolean existsKey(Object key) throws Exception;

	  /**
	   * @throws Exception
	   * @return HashMap
	   */
	  public Map getAllObject() throws Exception;

	  /**
	   * @return boolean
	   */
	  public boolean haveLoaded();
	  
	  public void setCacheCode(String code);
	  public void setGroup(String g);
}
