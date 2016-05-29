package com.chillax.entry.result;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 基础对象
 *
 */
public class BaseDO implements Serializable {
	
	private static final long serialVersionUID = 4976398103651159073L;

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
