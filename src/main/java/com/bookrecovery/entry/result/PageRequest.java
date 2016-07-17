package com.bookrecovery.entry.result;

public class PageRequest {

	/**
	 * 页面数码 [0...totalPages-1]
	 */
	private Integer page;

	/**
	 * 页面大小
	 */
	private Integer size;

	public PageRequest(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getOffset() {
		return page * size;
	}
}
