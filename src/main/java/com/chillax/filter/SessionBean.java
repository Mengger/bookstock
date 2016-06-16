package com.chillax.filter;

import java.util.Map;

public class SessionBean {

	//创建时间
	private Long createTime;
	//修改时间
	private Long lastModifyTime;
	//过期时间  单位:秒
	private Integer maxInactiveInterval;
	
	private Map<String, Object> attribute;

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
