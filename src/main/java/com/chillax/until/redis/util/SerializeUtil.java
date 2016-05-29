package com.chillax.until.redis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 

/***********************************************************
 * @Title: SerializeUtil.java
 * @Description:
 * @author:xuehao
 * @create_data:2012-9-21 ����07:54:01
 * @version:1.0
 **********************************************************/
public class SerializeUtil {
	 private transient static Log log = LogFactory.getLog(SerializeUtil.class);

	/**
	 * �������л�  ������̳�java.io.Serializable
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// ���л�
			baos = new ByteArrayOutputStream();

			oos = new ObjectOutputStream(baos);

			oos.writeObject(object);

			byte[] bytes = baos.toByteArray();

			return bytes;

		} catch (Exception e) {
            log.error("���л�ʧ��",e);
		}

		return null;

	}

	public static Object unserialize(byte[] bytes) {

		ByteArrayInputStream bais = null;

		try {

			// �����л�

			bais = new ByteArrayInputStream(bytes);

			ObjectInputStream ois = new ObjectInputStream(bais);

			return ois.readObject();

		} catch (Exception e) {
			log.error("�����л�ʧ��",e);
		}

		return null;

	}

}
