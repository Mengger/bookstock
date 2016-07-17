package com.bookrecovery.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class SessionBean {

	//sessionId
	private String sessionId;
	//创建时间
	private Long createTime;
	//修改时间
	private Long lastModifyTime;
	//过期时间  单位:秒  默认半小时
	private Integer maxInactiveInterval=60*30;
	
	private Map<String, Object> attribute=new HashMap<String, Object>();

	public HttpSession getSession(HttpSession session){
		for(String key:attribute.keySet()){
			session.setAttribute(key, attribute.get(key));
		}
		session.setMaxInactiveInterval(maxInactiveInterval);
		return session;
	}
	
	public SessionBean UpdateSessionBean (HttpSession session){
		for(String key:session.getValueNames()){
			attribute.put(key, session.getAttribute(key));
		}
		maxInactiveInterval=session.getMaxInactiveInterval();
		lastModifyTime=new Date().getTime();
		return this;
	}
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Integer getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(Integer maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}
	
	public void setAttribute(String key,Object value) {
		this.attribute.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attribute.get(key);
	}
}
