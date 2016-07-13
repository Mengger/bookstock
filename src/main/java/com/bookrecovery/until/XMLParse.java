/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.bookrecovery.until;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.bookrecovery.until.AesException;

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {

	/**
	 * 提取出xml数据包中的加密消息
	 * @param xmltext 待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException 
	 */
	public static Object[] extract(String xmltext) throws AesException     {
		Object[] result = new Object[3];
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("ToUserName");
			result[0] = 0;
			result[1] = nodelist1.item(0).getTextContent();
			result[2] = nodelist2.item(0).getTextContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
	
	/**
	 * 生成xml消息  未加密前的消息
	 * @param ToUserName	接受者
	 * @param FromUserName	发送者
	 * @param CreateTime	发送时间
	 * @param MsgType		消息类型
	 * @param Content		消息内容
	 * @return
	 */
	public static String generateMessage(String ToUserName,String FromUserName,String CreateTime,String MsgType,String Content){
		
		String formate="<xml>\n"+"<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
				+"<FromUserName><![CDATA[%2$s]]></FromUserName>\n"
				+"<CreateTime>%3$s</CreateTime>\n"
				+"<MsgType><![CDATA[%4$s]]></MsgType>\n"
				+"<Content><![CDATA[%5$s]]></Content>\n"
				+"</xml>\n";		
		return String.format(formate,ToUserName, FromUserName, CreateTime, MsgType, Content);
	}
	
	public static Map<String, String> parseXml(InputStream stream) throws Exception {  
	    // 将解析结果存储在HashMap中  
	    Map<String, String> map = new HashMap<String, String>();
	    // 读取输入流  
	    SAXReader reader = new SAXReader();  
	    org.dom4j.Document document = reader.read(stream);  
	    // 得到xml根元素  
	    org.dom4j.Element root = document.getRootElement();  
	    // 得到根元素的所有子节点  
	    List<org.dom4j.Element> elementList = root.elements();  
	    // 遍历所有子节点  
	    for (org.dom4j.Element e : elementList)  
	        map.put(e.getName(), e.getText());  
	  	  
	    return map;  
	}  
}
