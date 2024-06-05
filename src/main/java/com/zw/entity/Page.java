package com.zw.entity;

import java.util.List;

/**
 * 分页实体类
 *
 * @param <T> 分页数据类型
 */
public class Page<T> {
    // 存储分页页码，从前台传输
    private int num;
    // 每页条数，从前台传输
    private int size;
    // 总条数，从数据库查询出来
    private int count;
    // 总页码，自己计算出来
    private int pages;
    // 上一页，自己计算出来
    private int prev;
    // 下一页，自己计算出来
    private int next;
    // 存储分页数据，从数据库查询出来
    private List<T> data;

    public Page(int num, int size, int count, List<T> data) {
        this.num = num;
        this.size = size;
        this.count = count;
        this.data = data;

        //计算总页码
        this.pages = count % size != 0 ? count / size + 1 : count / size;
        //计算上一页
        this.prev = num <= 1 ? 1 : num - 1;
        //计算下一页
        this.next = num >= pages ? pages : num + 1;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
