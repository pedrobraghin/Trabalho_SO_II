package com.br.pages;

public class PageNode {
    public PageNode next = null;
    public PageNode prev = null;
    private Page page = null;
    private int pageNumber;

    public PageNode(Page page, int pageNumber) {
        this.page = page;
        this.pageNumber = pageNumber;
    }

    public PageNode() {
    }

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

}
