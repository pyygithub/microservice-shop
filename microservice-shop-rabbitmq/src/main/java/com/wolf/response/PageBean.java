package com.wolf.response;


import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * PageBean 分页信息对象
 *
 * @author: itw_panyy
 * @date: 2018年6月15日10:58:46
 */
public class PageBean<T> implements Serializable{
    /**
     * 总记录数
     */
    private long total;
    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 分页尺寸
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 当前页数量 <= pageSize,该属性来自于ArrayList的size属性
     */
    private int size;
    /**
     * 结果集
     */
    private List<T> list;

    /**
     * 包装Page对象
     * @param list
     */
    public PageBean(List<T> list) {
        if(list instanceof Page) {
            Page<T> page = (Page<T>)list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
            this.list = page;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
