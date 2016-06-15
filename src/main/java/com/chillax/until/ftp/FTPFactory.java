package com.chillax.until.ftp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPFactory {

	private static final Logger log = LoggerFactory.getLogger(FTPFactory.class);
	
	public static FTPBean FTPUploadDefult;
	
	public static String FTPDefultConfig;
	
	public static Map<String, FTPBean> FTPBeans=new HashMap<String, FTPBean>();
	
	static{
		loadConfigUploadFTP();
		FTPUploadDefult=FTPBeans.get(FTPDefultConfig);
	}
	
	public static void loadConfigUploadFTP(){
		try {
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("ftpload.properties"));
			FTPDefultConfig=p.getProperty("FTPUploadDefult");
			int count=Integer.valueOf(p.getProperty("ftpCount"));
			for (int i = 1; i < count+1; i++) {
				String FTPConfig="FTPConfig"+i;
				String ftpConfigPath=p.getProperty(FTPConfig);
				FTPBeans.put(FTPConfig, loadFTPConfigBean(ftpConfigPath));
			}
		} catch (IOException e) {
			log.error("load ftp config error",e);
		}
	}
	
	public static FTPBean loadFTPConfigBean(String fileName){
		try {
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
			FTPBean ftpConfig=new FTPBean();
			ftpConfig.setHostIp(p.getProperty("hostIp"));
			ftpConfig.setPort(Integer.valueOf(p.getProperty("port")));
			ftpConfig.setUserName(p.getProperty("userName"));
			ftpConfig.setPassword(p.getProperty("password"));
			ftpConfig.setLocalPath(p.getProperty("localPath"));
			ftpConfig.setLocalPathTemp(p.getProperty("localPathTemp"));
			ftpConfig.setRemotePath(p.getProperty("remotePath"));
			ftpConfig.setRemotePathHis(p.getProperty("remotePathHis"));
			return ftpConfig;
		} catch (IOException e) {
			log.error("load FTP Bean to Redis error",e);
		}
		return null;
	}
}
