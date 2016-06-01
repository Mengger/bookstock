package com.chillax.until.redis.test;

import com.chillax.until.redis.load.RedisManager;
/**
 * 
*      
* ��������   redis������
* �����ˣ�suizhifa   
* ����ʱ�䣺2015-2-12 ����10:27:02   
* �޸��ˣ�suizhifa   
* �޸�ʱ�䣺2015-2-12 ����10:27:02   
* �޸ı�ע��   
* @version    
*
 */
public class RedisPersistTest {
	
	public static void main(String[] args) throws Exception{
		try {
			//����key����ֵ
			System.out.println("����key suizf suizhifa");
			RedisManager.setValueByKeyAndGroup("GROUP_0", "suizf", "suizhifa");
			RedisManager.setValueByKeyAndGroup("GROUP_0", "szf", "suizhifa");
			//����key��ȡֵ
			String str=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			System.out.println("��ȡkey suizf----"+str);
			//����keyɾ��ֵ
			RedisManager.removeByKeyAndGroup("GROUP_0", "suizf");
			String delstr=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			String nodelstr=RedisManager.getValueByKeyAndGroup("GROUP_0", "szf");
			System.out.println("ɾ��֮��key suizf----"+delstr);
			System.out.println("ɾ��֮��key szf----"+nodelstr);
			
			//������еĻ���
		//	RedisManager.removeAllByGroup("GROUP_0");
			String removeSuizf=RedisManager.getValueByKeyAndGroup("GROUP_0", "suizf");
			String removeSzf=RedisManager.getValueByKeyAndGroup("GROUP_0", "szf");
			System.out.println("��ȡkey suizf----"+removeSuizf);
			System.out.println("��ȡkey szf----"+removeSzf);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
