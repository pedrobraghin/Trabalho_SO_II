
package com.br.frames;

import com.br.pages.Page;
/**
 *
 * @author murilo
 */
public class Frame {
    
    private Page page;
    private int number;

    public Frame(Page page, int number) {
        this.page = page;
        this.number = number;
    }

    public Frame() {
        this.number = -1;
        this.page = null;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
}

