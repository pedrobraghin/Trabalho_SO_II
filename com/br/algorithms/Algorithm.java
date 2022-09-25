package com.br.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import com.br.pages.Page;

/**
 * @author Pedro Braghin
 */
public abstract class Algorithm {

    protected String pagesPath;
    protected int uniquePages;

    public abstract void simulate();

    public abstract boolean isRunning();

    public Algorithm(String pagesPath, int uniquePages) {
        this.pagesPath = pagesPath;
        this.uniquePages = uniquePages;
    }

    /**
     * Generates aletory numbers and convert to integers values that can be numbers
     * between 0 and 9, characters from a-z or A-Z, and generates a string that
     * represent a content of a page. The integer value is converted to char for
     * represent one character of the ASCII table. Also, to avoid look alike
     * characters generate, a balance factor has been added putting an aletatory
     * chance to generate some specfic characters. This factor was added because if
     * the random
     * generate function genearates a number out of the range sayied before, the
     * most low value of the interval that are more close of the generated invalid
     * value is setted to prevent charaters out of the interval. This could causes
     * repeat page contents, and we want to avoid this.
     * 
     * @return String representing the content of the page.
     */
    protected String populatePages() {
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

    /**
     * Generates unique pages limited by pages num parameter in the folder given by
     * path parameter.
     * 
     * @param path
     * @param pagesNum
     */
    protected void generatePages() {
        for (int i = 0; i < this.uniquePages; i++) {
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

    /**
     * Searches a page named by "pageNumber" parameter in the path given by
     * parameter. Returns the page content if found. Otherwise, return null.
     * 
     * @param path
     * @param pageNumber
     * @return
     */

    protected Page searchPageFile(int pageNumber) {
        Page page = null;
        String path = pagesPath + pageNumber + ".pag";
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
    public abstract String getReport();
    public abstract String getResults();
}
