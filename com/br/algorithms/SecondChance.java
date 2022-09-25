package com.br.algorithms;

import java.util.Random;

import com.br.pages.Page;
import com.br.pages.SCPageNode;

/**
 * @author Pedro Braghin
 */
public class SecondChance extends Algorithm {

    private SCPageNode list;
    private int[] referenceString;
    private int framesNum;
    private int loadedPages = 0;
    private int requiredPages;
    private int pageFaults = 0;
    private boolean isRunning;
    private String report;

    public SecondChance(String pagesPath, int framesNum, int uniquePages, int requiredPages) {
        super(pagesPath, uniquePages);
        this.pagesPath = pagesPath;
        this.framesNum = framesNum;
        this.uniquePages = uniquePages;
        this.requiredPages = requiredPages;
        this.list = new SCPageNode();
        this.list.next = null;
        this.list.prev = null;
        this.list.setPageNumber(-1);
        this.referenceString = new int[requiredPages];
    }

    @Override
    public void simulate() {
        boolean hitted = false;
        Page page;
        loadedPages = 0;
        generatePages();
        generateReferenceString();
        this.isRunning = true;
        for (int i = 0; i < this.requiredPages; i++) {
            hitted = searchPage(referenceString[i]);
            if (!hitted) {
                this.pageFaults++;
                page = searchPageFile(referenceString[i]);
                if (page != null) {
                    if (loadedPages < framesNum) {
                        insertPage(page, referenceString[i]);
                    } else {
                        replacePage(page, referenceString[i]);
                    }
                } else {
                    System.err.println("Could not find page " + referenceString[i] + " in " + pagesPath);
                }
            }

            try {
                Thread.sleep(threadWait);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException on page " + i + ": " + e.getMessage());
            }
        }
        this.isRunning = false;
        report += "Algoritmo de substituição de páginas: Second Chance\n";
        report += "Sequência de Requisição: ";
        for (int i = 0; i < this.referenceString.length; i++) {
            report += this.referenceString[i] + " ";
        }
        report += "\nTotal de Falhas de Página: " + this.pageFaults;
    }

    /**
     * Looks por the most oldest page in the list. If the reference bit of the page
     * is 1, so he is setted to 0 and the page is moved to front of the list.
     * If the bit of the page is 0, so the page is removed and a new page is added.
     * 
     * @param page
     * @param pageNumber
     */
    public void replacePage(Page page, int pageNumber) {
        SCPageNode temp = list.next;
        boolean selectedToRemove = false;
        while (!selectedToRemove) {
            if (temp.getReferenced() == 1) {
                temp.setReferenced(0);
                temp = temp.next;
            } else {
                selectedToRemove = true;
            }
        }
        if (temp == list.next) {
            list.next = temp.next;
            list.next.prev = temp.prev;
        } else if (temp == list.prev) {
            list.prev = temp.prev;
            list.prev.next = temp.next;
        } else {
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
        }
        insertPage(page, pageNumber);
    }

    /**
     * Inserts a new page into the list pages.
     * 
     * @param page
     * @param pageNumber
     */
    public void insertPage(Page page, int pageNumber) {
        SCPageNode newNode = new SCPageNode(page, pageNumber);
        SCPageNode temp = list;

        if (loadedPages == 0) {
            newNode.prev = newNode;
            newNode.next = newNode;
            temp.next = newNode;
        } else {
            newNode.prev = list.prev;
            newNode.next = list.next;
            temp.prev.next = newNode;
            temp.next.prev = newNode;
        }
        temp.prev = newNode;
        if (loadedPages < framesNum)
            loadedPages++;
    }

    /**
     * Generates a reference String limited by the number of unique pages.
     * The string reference is used to simulate a pages request.
     */
    private void generateReferenceString() {
        this.referenceString = new int[this.requiredPages];
        Random rand = new Random();
        for (int i = 0; i < this.requiredPages; i++) {
            referenceString[i] = rand.nextInt(this.uniquePages);
        }
    }

    /**
     * Searches for a page in the pages list. If the page is not found, the fault
     * variable is incremented. If the page is found, the reference bit is updated to 1.
     * 
     * @param pageNumber
     * @return
     */
    private boolean searchPage(int pageNumber) {
        SCPageNode temp = list.next;
        boolean hitted = false;
        int count = 0;

        while ((temp != null) && (!hitted) && (count < this.loadedPages)) {
            if (temp.getPageNumber() == pageNumber) {
                hitted = true;
                temp.setReferenced(1);
            } else {
                temp = temp.next;
            }
            count++;
        }

        return hitted;
    }

    @Override
    public String getReport() {
        report = "Frame\tPágina\tReferenciado\tConteúdo\n";
        SCPageNode temp = list.next;
        for (int i = 0; i < loadedPages; i++) {
            report += i + "\t" + temp.getPageNumber() + "\t" + temp.getReferenced() + "\t" + temp.getPage().toString()
                    + "\n";
            temp = temp.next;
        }

        return report;
    }

    @Override
    public String getResults() {
        return this.report;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

}
