package com.br.frames;

import com.br.pages.PageNode;

public class FrameMap {
    private int key;
    private PageNode pageNode;

    public FrameMap(int key, PageNode pageNode) {
        this.key = key;
        this.pageNode = pageNode;
    }

    public FrameMap() {
        this.pageNode = null;
    }

    public PageNode getPageNode() {
        return this.pageNode;
    }

    public void setPageNode(PageNode pageNode) {
        this.pageNode = pageNode;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
