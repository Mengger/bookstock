package com.bookrecovery.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;

public class JiGuangPushTest {

	private static final Logger logger = LoggerFactory.getLogger(JiGuangPushTest.class);

	 /**
     * 极光 推送 消息发送接口
     * 
     * @param telephones 推送的对象
     * @param title 标题
     * @param content 内容
     * @param apnsProduction 环境(true 正式环境推送，false 测试环境推送)
     * @param isOnlyAndroid 是否只推送android true只推送android false ios android都推送;
     * @param validTime  消息有效时间(过了改时间段，用户不在线，就放弃推送)  如果为null  默认24小时(86400) 单位：秒
     */
    public void JiGuangPush(List<String> telephones, String title, String content,
            boolean apnsProduction, boolean isOnlyAndroid,Long validTime) {

    	if(validTime==null){
    		validTime=86400L;
    	}
        JPushClient jpushClient = new JPushClient("e7318ff1a9b0389c75672506", "beccc651f7d0cdb713228d17");
        Date now = new Date();
        int nowHours = now.getHours();
        PushPayload payload = null;

        Map<String, String> extras = new HashMap<String, String>();
        if (title == null) title = "";
        if (content == null) content = "";
        extras.put("title", title);
        extras.put("content", content);

        // 设置消息通知IOS
        PlatformNotification iosNotification =
                IosNotification.newBuilder().setBadge(1).setAlert(content).setSound("sound.caf")
                        .addExtras(extras).build();
        // 在上午9点到下午9点IOS推送的消息，带声音;其他时间消息静音
        if (!(nowHours >= 9 && nowHours <= 21))
            iosNotification =
                    IosNotification.newBuilder().autoBadge().setAlert(content).addExtras(extras)
                            .build();

        Notification notification =
                Notification.newBuilder()
                        .addPlatformNotification(iosNotification)
                        // ios推送弹窗
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setAlert("").addExtras(extras)
                                        .build())// android推送
                        .build();

        Platform platform = Platform.android_ios();// 推送设备 为ios根android
        if (isOnlyAndroid) {
            platform = Platform.android();// 推送设备仅仅为android
            notification =
                    Notification
                            .newBuilder()
                            .addPlatformNotification(
                                    AndroidNotification.newBuilder().setAlert("").addExtras(extras)
                                            .build())// android推送
                            .build();
        }
        payload =
                PushPayload
                        .newBuilder()
                        .setNotification(notification)
                        .setPlatform(platform)
                        .setOptions(
                                Options.newBuilder().setApnsProduction(apnsProduction)
                                        .setTimeToLive(validTime).build())// 推送附加选项
                                                                      // ApnsProduction=false测试环境
                                                                      // 用户不在线时86400秒内上线后，继续推送
                        .setAudience(Audience.alias(telephones))// 推送目标
                        .build();

        try {
            PushResult result = jpushClient.sendPush(payload);
             System.out.println(result);
        } catch (APIConnectionException e) {
            logger.error("Connection error, should retry later", e);
             e.printStackTrace();
        } catch (APIRequestException e) {
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
             e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	System.out.println("ddddd");
    	JiGuangPushTest a=new JiGuangPushTest();
    	List<String> tt=new ArrayList<String>();
    	tt.add("15168277969");//15168277969
    	//15093976220
a.JiGuangPush(tt, "考勤提醒",
                "【考勤提醒】："+ "离开"
                        + "学校", false, true, null);
	}
}
