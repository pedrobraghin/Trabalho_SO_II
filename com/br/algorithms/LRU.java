package com.br.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.br.pages.Page;
import com.br.pages.SCPageNode;

/**
 * @author Pedro Braghin
 */
public class SecondChance extends Algorithm {

    private SCPageNode list;
    private String pagesPath;
    private int[] referenceString;
    private int framesNum;
    private int loadedPages = 0;
    private int uniquePages;
    private int requiredPages;
    private int pageFaults = 0;
    private boolean isRunning;

    public SecondChance(String pagesPath, int framesNum, int uniquePages, int requiredPages) {
        this.pagesPath = pagesPath;
        this.framesNum = framesNum;
        this.uniquePages = uniquePages;
        this.requiredPages = requiredPages;
        this.list = new SCPageNode();
        this.list.next = list;
        this.list.prev = list;
        this.list.setPageNumber(-1);
        this.referenceString = new int[requiredPages];
    }

    @Override
    public void simulate() {
        boolean hitted = false;
        SCPageNode temp = this.list.next;
        Page page;
        generatePages();
        generateReferenceString();
        this.isRunning = true;
        for (int i = 0; i < this.requiredPages; i++) {
            if (temp != null) {
                hitted = searchPage(referenceString[i]);
                if (!hitted) {
                    this.pageFaults++;
                    page = searchPageFile(referenceString[i]);
                    if (page != null) {
                        if (loadedPages < framesNum) {
                            insertPage(page, referenceString[i]);
                            loadedPages++;
                        } else {
                            replacePage(page, referenceString[i]);
                        }
                    } else {
                        System.err.println("Could not find page " + referenceString[i] + " in " + pagesPath);
                    }
                }
            } else {
                if(temp == null) {
                    this.pageFaults++;
                    page = searchPageFile(referenceString[i]);
                    if (page != null) {
                        if (loadedPages < framesNum) {
                            insertPage(page, referenceString[i]);
                            loadedPages++;
                        } else {
                            replacePage(page, referenceString[i]);
                        }
                    } else {
                        System.err.println("Could not find page " + referenceString[i] + " in " + pagesPath);
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException on page " + i + ": " + e.getMessage());
            }
        }
        this.isRunning = false;
    }

    public void replacePage(Page page, int pageNumber) {
        SCPageNode temp = list;
        boolean selectedToRemove = false;

        while (!selectedToRemove) {
            if (temp.getReferenced() == 1) {
                temp.setReferenced(0);
                temp = temp.prev;
            } else {
                selectedToRemove = true;
            }
        }
        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
        insertPage(page, pageNumber);
    }

    public void insertPage(Page page, int pageNumber) {
        SCPageNode newNode = new SCPageNode(page, pageNumber);

        newNode.next = list.next;
        newNode.prev = list.prev;
        this.list.prev.next = newNode;
        this.list.next.prev = newNode;
        this.list.next = newNode;
        // this.head = this.list.prev;
    }

    private void generateReferenceString() {
        this.referenceString = new int[this.requiredPages];
        Random rand = new Random();
        for (int i = 0; i < this.requiredPages; i++) {
            referenceString[i] = rand.nextInt(this.uniquePages - 1);
        }
    }

    private boolean searchPage(int pageNumber) {
        SCPageNode temp = this.list.next;
        boolean hitted = false;
        int count = 0;

        while (temp != null && !hitted && count < this.framesNum) {
            if (temp.getPageNumber() == pageNumber) {
                hitted = true;
            } else {
                temp = temp.next;
            }
            count++;
        }

        return hitted;
    }

    public void generatePages() {
        for (int i = 0; i < uniquePages; i++) {
            File file = new File(this.pagesPath + i + ".pag");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(populatePages());
                writer.close();
            } catch (IOException e) {
                System.err.println("Error generating page " + i + ": " + e.getMessage());
            }
        }
    }

    public String populatePages() {
        String content = "";
        Random rand = new Random();
        int number;
        for (int i = 0; i < 10; i++) {
            number = rand.nextInt(122);
            if (number < 65 && number > 57) {
                if ((Math.random()) < 0.5) {
                    number = 65;
                } else {
                    number = 48;
                }
            } else if (number < 48) {
                if ((Math.random()) < 0.5)
                    number = 56;
                else
                    number = 52;
            } else if (number > 90 && number < 97) {
                if ((Math.random()) < 0.5)
                    number = 83;
                else
                    number = 75;
            }
            content += (char) number;
        }
        return content;
    }

    private Page searchPageFile(int pageNumber) {
        Page page = null;
        String path = pagesPath + "\\" + pageNumber + ".pag";
        try {
            FileReader reader = new FileReader(path);
            char[] buffer = new char[10];
            reader.read(buffer);
            reader.close();
            page = new Page(buffer);
        } catch (FileNotFoundException e) {
            System.err.println("Error reading page " + pageNumber + ": Page not found!");
        } catch (IOException e) {
            System.err.println("Error reading page " + pageNumber + ": IOException");
        }

        return page;
    }

    @Override
    public String getRelatory() {
        String relatory = "Frame\tPágina\tConteúdo\n";
        SCPageNode temp = list.next;
        for(int i = 0; i < framesNum; i++) {
            if(temp != null && temp.getPageNumber() != -1){
                relatory += i + "\t" + temp.getPageNumber() + "\t" + temp.getPage().toString() + "\n";
                temp = temp.next;
            } else {
                relatory += "" + i + "\t-" + "\t----------\n";    
            }
        }
        relatory += "Algoritmo de substituição de páginas: Second Chance\n";
        relatory += "Sequência de Requisição: ";
        for(int i = 0; i < this.referenceString.length; i++) {
            relatory += this.referenceString[i] + " ";
        }
        relatory += "\nTotal de Falhas de Página: " + this.pageFaults;

        return relatory;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

}

