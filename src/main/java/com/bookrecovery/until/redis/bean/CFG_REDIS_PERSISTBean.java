package com.bookrecovery.until.redis.bean;

/**   
 *      
 * 类描述：   
 * 创建人：suizhifa   
 * 创建时间：2015-2-11 下午05:30:23   
 * 修改人：suizhifa   
 * 修改时间：2015-2-11 下午05:30:23   
 * 修改备注：   
 * @version    
 *    
 */
public class CFG_REDIS_PERSISTBean {
	private  String MaxInvokedNum = "MAX_INVOKED_NUM";
	  private  String InterfaceName = "INTERFACE_NAME";
	  private  String CreateTime = "CREATE_TIME";
	  private  String Key = "KEY";
	  private  String HasInvokedNum = "HAINVOKED_NUM";
	  private  String Id = "ID";
	  private  String BaseTime = "BASE_TIME";
	public String getMaxInvokedNum() {
		return MaxInvokedNum;
	}
	public void setMaxInvokedNum(String maxInvokedNum) {
		MaxInvokedNum = maxInvokedNum;
	}
	public String getInterfaceName() {
		return InterfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		InterfaceName = interfaceName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getHasInvokedNum() {
		return HasInvokedNum;
	}
	public void setHasInvokedNum(String hasInvokedNum) {
		HasInvokedNum = hasInvokedNum;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getBaseTime() {
		return BaseTime;
	}
	public void setBaseTime(String baseTime) {
		BaseTime = baseTime;
	}

}
