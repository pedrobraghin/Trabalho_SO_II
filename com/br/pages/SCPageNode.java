package com.br.pages;

public class SCPageNode {

    public SCPageNode next;
    public SCPageNode prev;
    private Page data;
    private int referenced;
    private int pageNumber;

    public SCPageNode(char[] data, int pageNumber) {
        this.referenced = 0;
        this.data = new Page(data);
        this.pageNumber = pageNumber;
    }

    public SCPageNode(String data, int pageNumber) {
        this.referenced = 0;
        this.pageNumber = pageNumber;
        this.data = new Page(data);
    }

    public SCPageNode(Page page, int pageNumber) {
        this.referenced = 0;
        this.pageNumber = pageNumber;
        this.data = page;
    }

    public SCPageNode() {
        this.data = null;
        this.referenced = 0;
        this.pageNumber = -1;
    }

    public int getReferenced() {
        return this.referenced;
    }

    public void setReferenced(int referenced) {
        this.referenced = referenced;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public Page getPage() {
        return this.data;
    }

}
