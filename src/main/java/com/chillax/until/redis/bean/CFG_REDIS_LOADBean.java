package com.chillax.until.redis.bean;

/**   
 *      
 * ��������   
 * �����ˣ�suizhifa   
 * ����ʱ�䣺2015-2-11 ����05:28:16   
 * �޸��ˣ�suizhifa   
 * �޸�ʱ�䣺2015-2-11 ����05:28:16   
 * �޸ı�ע��   
 * @version    
 *    
 */
public class CFG_REDIS_LOADBean {
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getRedisLoadCode() {
		return RedisLoadCode;
	}
	public void setRedisLoadCode(String redisLoadCode) {
		RedisLoadCode = redisLoadCode;
	}
	public String getRedisLoadId() {
		return RedisLoadId;
	}
	public void setRedisLoadId(String redisLoadId) {
		RedisLoadId = redisLoadId;
	}
	public String getIfInit() {
		return IfInit;
	}
	public void setIfInit(String ifInit) {
		IfInit = ifInit;
	}
	public String getRedisLoadDesc() {
		return RedisLoadDesc;
	}
	public void setRedisLoadDesc(String redisLoadDesc) {
		RedisLoadDesc = redisLoadDesc;
	}
	public String getBelongGroup() {
		return BelongGroup;
	}
	public void setBelongGroup(String belongGroup) {
		BelongGroup = belongGroup;
	}
	public String getRedisLoadRemark() {
		return RedisLoadRemark;
	}
	public void setRedisLoadRemark(String redisLoadRemark) {
		RedisLoadRemark = redisLoadRemark;
	}
	public String getRedisLoadImpl() {
		return RedisLoadImpl;
	}
	public void setRedisLoadImpl(String redisLoadImpl) {
		RedisLoadImpl = redisLoadImpl;
	}
	private  String State = "STATE";
	  private  String RedisLoadCode = "REDILOAD_CODE";
	  private  String RedisLoadId = "REDILOAD_ID";
	  private  String IfInit = "IF_INIT";
	  private  String RedisLoadDesc = "REDILOAD_DESC";
	  private  String BelongGroup = "BELONG_GROUP";
	  private  String RedisLoadRemark = "REDILOAD_REMARK";
	  private  String RedisLoadImpl = "REDILOAD_IMPL";

}
