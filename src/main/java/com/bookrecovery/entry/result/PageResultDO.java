package com.bookrecovery.entry.result;

import java.util.List;

/**
 * 单页结果返回
 *
 * @param <T>
 */
public class PageResultDO<T> extends BaseResultDO {

    private static final long serialVersionUID = 2089514013080281267L;

    /**
     * 结果
     */
    private List<T> result;

    /**
     * 当前页数 [0...totalPages-1]
     */
    private Integer number;

    /**
     * 当前页元素数量
     */
    private Integer numberOfElements;

    /**
     * 页大小
     */
    private Integer size;

    /**
     * 元素总数
     */
    private Long totalElements;

    /**
     * 页总数
     */
    private Integer totalPages;

    public PageResultDO(PageRequest pageRequest) {
        this.number = pageRequest.getPage();
        this.size = pageRequest.getSize();
    }

    public PageResultDO() {
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
        this.numberOfElements = result.size();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
        this.totalPages = (int) (totalElements / size + (totalElements % size == 0 ? 0 : 1));
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
