package com.br.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.br.frames.FrameMap;
import com.br.pages.PageNode;
import com.br.pages.Page;

/**
 * @author Pedro Braghin
 */
public class FIFO extends Algorithm{
    private PageNode head;
    private PageNode tail;
    private FrameMap[] framesMap;
    private int requiredPages;
    private int maxSize;
    private int pageFaults;
    private int mostOldestPageIndex = 0;
    private int frameNum;
    private int[] referenceString;
    
    private boolean isRunning;

    public FIFO(String pagesPath, int framesNum, int uniquePages, int requiredPages) {
        super(pagesPath, uniquePages);
        this.framesMap = new FrameMap[framesNum];
        this.frameNum = framesNum;
        this.maxSize = framesNum;
        this.requiredPages = requiredPages;

        // connecting the tail of the list with the head of the list
        this.head = new PageNode();
        this.tail = new PageNode();
        this.head.prev = null;
        this.head.next = null;
        this.tail.prev = null;
        this.tail.next = null;
        this.isRunning = false;
        this.referenceString = new int[requiredPages];
    }

    /**
     * Method to simulate a FIFO replacement page algorithm.
     * @return void
     */
    @Override
     public void simulate() {
        Page page;
        int pageIndex = -1;
        this.isRunning = true;
        this.framesMap = new FrameMap[this.frameNum];
        this.head = new PageNode();
        this.tail = new PageNode();
        generatePages();
        generateReferenceString();

        for (int i = 0; i < requiredPages; i++) {
            pageIndex = searchPage(referenceString[i]);
            if (pageIndex == -1) {
                pageFaults++;
                page = searchPageFile(referenceString[i]);
                if (page != null) {
                    addPage(page, referenceString[i]);  
                } else {
                    System.err.println("Could not find page " + referenceString[i] + " in " + pagesPath);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException on page " + i + ": " + e.getMessage());
            }
        }
        this.isRunning = false;
    }

    /**
     * Generates a referenceString of size requiredPagesCount. The generated values
     * range is limited by uniquePages count.
     * 
     * @param requiredPagesCount
     * @return String
     */
    private void generateReferenceString() {
        Random rand = new Random();
        this.referenceString = new int[this.requiredPages];
        for (int i = 0; i < this.requiredPages; i++) {
            this.referenceString[i] = rand.nextInt(this.uniquePages - 1);
        }
    }

    /**
     * Method to print the frameMap vector content.
     * 
     * @return void
     */
    protected void printNodes() {
        for (int i = 0; i < framesMap.length; i++) {
            if (framesMap[i] != null) {
                System.out.println("Chave: " + framesMap[i].getKey());
                System.out.println("Número da página:" + framesMap[i].getPageNode().getPageNumber());
                String data = new String(framesMap[i].getPageNode().getPage().getData());
                System.out.println("Dados da página: " + data);
            } else {
                System.out.println(framesMap.length);
                System.out.println(framesMap[i]);
            }
        }
        System.out.println();
    }

    /**
     * Method to print the PageLlist content.
     * 
     * @return void
     */
    protected void printList() {
        PageNode temp = head.next;
        int count = 0;
        System.out.println("Head:\n");
        while (temp != null) {
            System.out.println("Indice: " + count);
            System.out.println("Número da página:" + temp.getPageNumber());
            String data = new String(temp.getPage().getData());
            System.out.println("Dados da página: " + data);
            temp = temp.next;
            count++;
        }
        temp = tail.prev;
        count = 0;
        System.out.println("\nTail");
        while (temp != null) {
            System.out.println("Indice: " + count);
            System.out.println("Número da página:" + temp.getPageNumber());
            String data = new String(temp.getPage().getData());
            System.out.println("Dados da página: " + data);
            temp = temp.next;
            count++;
        }
        System.out.println("==============================");
    }

    /**
     * Search a page in framesMap array using pageNumber parameter.
     * 
     * @param pageNumber
     * @return int : the index of the page in framesMap array or -1 if no page is
     *         found.
     */
    private int searchPage(int pageNumber) {
        for (int i = 0; i < maxSize; i++) {
            if ((framesMap[i] != null) && (framesMap[i].getKey() == pageNumber)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds a new page to framesMap array.
     * 
     * @param page
     * @param pageNumber
     */
    private void addPage(Page page, int pageNumber) {
        int count = 0;

        // verify if the page is already exists and founds a free position in the frames
        // vector.
        while ((count < maxSize)) {
            if (this.framesMap[count] == null) {
                break;
            }
            count++;
        }

        // if found a free position, a new page is allocated to that frame
        if (count < maxSize) {
            framesMap[count] = new FrameMap(pageNumber, insertPage(page, pageNumber));
        } else {
            // otherwise, select the position that contatins the most oldest page added to
            // the frames vector
            framesMap[mostOldestPageIndex] = new FrameMap(pageNumber, replacePage(page, pageNumber));
        }
    }

    /**
     * Adds a new page in the beginning of the list.
     * 
     * @param page       the page to insert
     * @param pageNumber the identifier of the page to insert
     */
    private PageNode insertPage(Page page, int pageNumber) {
        if (head.next == null) {
            head.next = new PageNode(page, pageNumber);
            tail.prev = head.next;
        } else {
            PageNode temp = head.next;
            head.next = new PageNode(page, pageNumber);
            head.next.next = temp;
            temp.prev = head.next;
        }
        return head.next;
    }

    /**
     * Removes the last page of the list and adds a new page in the beginning of the
     * list.
     * 
     * @param page
     * @param pageNumber
     */
    private PageNode replacePage(Page page, int pageNumber) {
        PageNode temp;
        if (maxSize == 1) {
            tail.prev = head.next;
        } else {
            temp = tail.prev.prev;
            temp.next = null;
            tail.prev = temp;
        }
        tail.next = null;

        // saves the identifier of the most oldest page. It will be used to replace the
        // new page when the vector is full.
        mostOldestPageIndex = searchPage(tail.prev.getPageNumber());
        return insertPage(page, pageNumber);
    }

    /**
     * Returns the updated string that contains all informations saved by
     * {@link #updateRelatory()} function.
     * 
     * @return String : the relatory of all iterations.
     */
    @Override
    public String getRelatory() {
        String temp = "Frame\tPágina\tConteúdo\n";
        for (int i = 0; i < framesMap.length; i++) {
            if (framesMap[i] != null) {
                String data = new String(framesMap[i].getPageNode().getPage().getData());
                temp += "" + i + "\t" + framesMap[i].getPageNode().getPageNumber() + "\t" + data + "\n";
            } else {
                temp += "" + i + "\t-" + "\t----------\n";
            }
        }
        temp += "Algoritmo de Substituição de Páginas: FIFO\n";
        temp += "Sequência de Requisição: "; 
        for(int i = 0; i < this.referenceString.length; i++) {
            temp += this.referenceString[i] + " ";
        }
        temp += "\nTotal de Falhas de Página: " + this.pageFaults;
        return temp;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

}
