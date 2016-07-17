package com.bookrecovery.until;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DicSort {
	private final static Log log = LogFactory.getLog(DicSort.class);

	public static String getSHA(String[] needList) {
		int size=3;
		for (int i = 0; i < size; i++) {
			for (int j = size - i; j < size; j++) {
				if (compare(needList[j - 1] , needList[j])) {
					String swap = needList[j];
					needList[j] = needList[j - 1];
					needList[j - 1] = swap;
				}
			}
		}
		String rtn="";
		for(String re:needList){
			rtn=rtn+re;
		}
		return SHA1(rtn);
	}
	
	
	public static boolean compare(String a1,String a2){
		int rtn=a1.compareTo(a2);
		boolean condition=true;
		if(rtn>0){
			condition=true;
		}else{
			condition=false;
		}
		return condition;
	}

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
