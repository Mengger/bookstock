package com.chillax.until.redis.test;

import com.chillax.until.redis.load.RedisManager;
/**
 * 
*      
* 类描述：   redis测试类
* 创建人：suizhifa   
* 创建时间：2015-2-12 上午10:27:02   
* 修改人：suizhifa   
* 修改时间：2015-2-12 上午10:27:02   
* 修改备注：   
* @version    
*
 */
public class RedisPersistTest {
	
	public static void main(String[] args) throws Exception{
		try {
			//根据key设置值
			System.out.println("设置key suizf suizhifa");
			RedisManager.setValueByKeyAndGroup("GROUP_0", "suizf", "suizhifa");
			RedisManager.setValueByKeyAndGroup("GROUP_0", "szf", "suizhifa");
			//根据key获取值
			String str=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			System.out.println("获取key suizf----"+str);
			//根据key删除值
			RedisManager.removeByKeyAndGroup("GROUP_0", "suizf");
			String delstr=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			String nodelstr=RedisManager.getValueByKeyAndGroup("GROUP_0", "szf");
			System.out.println("删除之后key suizf----"+delstr);
			System.out.println("删除之后key szf----"+nodelstr);
			
			//清空所有的缓存
		//	RedisManager.removeAllByGroup("GROUP_0");
			String removeSuizf=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			String removeSzf=RedisManager.getValueByKeyAndGroup("GROUP_0", "szf");
			System.out.println("获取key suizf----"+removeSuizf);
			System.out.println("获取key szf----"+removeSzf);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
