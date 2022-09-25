package com.br.pages;

public class Page {

    private char[] data = new char[10];

    public Page(String data) {
        for (int i = 0; i < data.length(); i++) {
            this.data[i] = data.charAt(i);
        }
    }

    public Page(char[] data) {
        this.data = data;
    }

    public void setData(String data) {
        for (int i = 0; i < data.length(); i++) {
            this.data[i] = data.charAt(i);
        }
    }

    public void setData(char[] data) {
        this.data = data;
    }
    
    public char[] getData() {
        return this.data;
    }

    public String toString() {
        return "" + String.valueOf(this.data);
    }

}
