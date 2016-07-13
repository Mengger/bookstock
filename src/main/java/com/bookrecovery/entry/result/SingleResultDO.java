package com.bookrecovery.entry.result;

public class SingleResultDO<T> extends BaseResultDO {
	 private static final long serialVersionUID = -943558346236174497L;

	    private T result;

	    private Long totelNum;

	    public T getResult() {
	        return result;
	    }

	    public void setResult(T result) {
	        this.result = result;
	    }

	    public Long getTotelNum() {
	        return totelNum;
	    }

	    public void setTotelNum(Long totelNum) {
	        this.totelNum = totelNum;
	    }


	}

