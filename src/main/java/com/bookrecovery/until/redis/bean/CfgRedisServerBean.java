package com.bookrecovery.until.redis.bean;

/**   
 *      
 * 类描述：   redis服务属性类
 * 创建人：suizhifa   
 * 创建时间：2015-2-11 下午02:47:22   
 * 修改人：suizhifa   
 * 修改时间：2015-2-11 下午02:47:22   
 * 修改备注：   
 * @version    
 *    
 */
public class CfgRedisServerBean {
	private  String State ;
	  private  String ServerPort ;
	  private  String ChannelId ;
	  private  String UseType ;
	  private  String GroupSort ;
	  private  String RedisServerId ;
	  private  String ServerIp  ;
	  private  String RedisServerCode ;
	  public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getServerPort() {
		return ServerPort;
	}
	public void setServerPort(String serverPort) {
		ServerPort = serverPort;
	}
	public String getChannelId() {
		return ChannelId;
	}
	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}
	public String getUseType() {
		return UseType;
	}
	public void setUseType(String useType) {
		UseType = useType;
	}
	public String getGroupSort() {
		return GroupSort;
	}
	public void setGroupSort(String groupSort) {
		GroupSort = groupSort;
	}
	public String getRedisServerId() {
		return RedisServerId;
	}
	public void setRedisServerId(String redisServerId) {
		RedisServerId = redisServerId;
	}
	public String getServerIp() {
		return ServerIp;
	}
	public void setServerIp(String serverIp) {
		ServerIp = serverIp;
	}
	public String getRedisServerCode() {
		return RedisServerCode;
	}
	public void setRedisServerCode(String redisServerCode) {
		RedisServerCode = redisServerCode;
	}
	public String getBelongGroup() {
		return BelongGroup;
	}
	public void setBelongGroup(String belongGroup) {
		BelongGroup = belongGroup;
	}
	public String getExt1() {
		return Ext1;
	}
	public void setExt1(String ext1) {
		Ext1 = ext1;
	}
	public String getDeployType() {
		return DeployType;
	}
	public void setDeployType(String deployType) {
		DeployType = deployType;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getRequirepass() {
		return Requirepass;
	}
	public void setRequirepass(String requirepass) {
		Requirepass = requirepass;
	}
	private  String BelongGroup ;
	  private  String Ext1 ;
	  private  String DeployType ;
	  private  String Remark ;
	  private  String Requirepass ;
}
