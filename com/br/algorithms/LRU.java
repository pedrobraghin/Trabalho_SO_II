package com.br.algorithms;

import com.br.pages.Page;
import com.br.frames.Frame;
import com.br.frames.Memory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo
 */
public class LRU extends Algorithm {

    private Memory memory;
    
    private int numPagReq;
    private int numFrames;
    private int[] PagReq;
    private int inicioFilaFrames = 0;
    private int[] pilhaFrames;
    private int topo_frames = -1;
    private int[] pilhaAux;
    private int topoAux = -1;
    private int nFalhas = 0;
    private boolean isRunning;
    private String report;

    public LRU(String pagesPath, int numFrames, int numPagUnicas, int numPagReq) {
        super(pagesPath, numPagUnicas);
        this.memory = new Memory(numFrames);
        this.pagesPath = pagesPath + "\\";
        this.numPagReq = numPagReq;
        this.uniquePages = numPagUnicas;
        this.numFrames = numFrames;
        this.memory = new Memory(numFrames);
        pilhaFrames = new int[numFrames];
        pilhaAux = new int[numFrames];
        PagReq = new int[numPagReq];
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void simulate() {
        Page page;
        loadMemory();
        generatePages();
        generatePageRequest();
        for (int i = 0; i < this.numPagReq; i++) {
            if (this.inicioFilaFrames != numFrames) {
                if (pilhaFrames.length == 0) {
                    page = searchPageFile(PagReq[i]);
                    addPage(PagReq[i], page);
                    nFalhas++;
                } else {
                    int busca = searchPage(PagReq[i]);
                    if (busca == -1) {
                        page = searchPageFile(PagReq[i]);
                        addPage(PagReq[i], page);
                        nFalhas++;
                    } else {
                        updateStacks(PagReq[i]);
                    }
                }
            } else {
                int busca = searchPage(PagReq[i]);

                if (busca == -1) {
                    page = searchPageFile(PagReq[i]);
                    changePage(PagReq[i], page);
                    nFalhas++;
                } else {
                    updateStacks(PagReq[i]);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LRU.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.isRunning = false;
        report += "\nAlgoritmo de Substituição de Páginas: LRU\n";
        report += "Sequência de Requisição: ";
        for (int i : PagReq) {
            report += i + " ";
        }
        report += "\n";
        report += "Total de Falhas de Página: " + nFalhas;
    }

    @Override
    public String getReport() {
        report = "";
        report += memory.toString();
        return report;
    }

    public int dequeueMemory() {
        int pos = this.inicioFilaFrames;
        this.inicioFilaFrames++;
        return pos;
    }

    public int unstackFrame() {
        int pos = -1;

        if (this.topo_frames != -1) {
            pos = pilhaFrames[this.topo_frames];
            this.topo_frames--;
        }
        return pos;
    }

    public void stackFrame(int x) {

        if (this.topo_frames != this.numFrames - 1) {
            this.topo_frames++;
            this.pilhaFrames[topo_frames] = x;
        }

    }

    public int unstackAux() {
        int pos = -1;

        if (this.topoAux != -1) {
            pos = pilhaAux[this.topoAux];
            this.topoAux--;
        }

        return pos;
    }

    public void stackAux(int x) {

        if (this.topoAux != this.numFrames - 1) {
            this.topoAux++;
            this.pilhaAux[topoAux] = x;
        }

    }

    public void updateFrame(int nFrame, int numPag, Page pagina) {
        memory.getMemory()[nFrame].setNumber(numPag);
        memory.getMemory()[nFrame].setPage(pagina);
    }

    public void addPage(int numPage, Page pagina) {
        int nFrame = dequeueMemory();

        stackFrame(nFrame);
        updateFrame(nFrame, numPage, pagina);
    }

    public void updateStacks(int x) {

        int temp = unstackFrame();

        while (memory.getMemory()[temp].getNumber() != x) {
            stackAux(temp);
            temp = unstackFrame();
        }

        while (topoAux != -1) {
            stackFrame(unstackAux());
        }

        stackFrame(temp);
    }

    public void changePage(int numPage, Page page) {
        while (topo_frames != 0) {
            stackAux(unstackFrame());
        }
        int nFrame = unstackFrame();
        while (topoAux != -1) {
            stackFrame(unstackAux());
        }
        stackFrame(nFrame);
        updateFrame(nFrame, numPage, page);
    }

    public void generatePageRequest() {
        Random random = new Random();
        for (int i = 0; i < this.numPagReq; i++) {
            PagReq[i] = random.nextInt(this.uniquePages);
        }
        this.isRunning = true;
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

    public void generatePages() {
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

    public int searchPage(int pagina) {
        int frame = -1;
        if (topo_frames == -1) {
            return frame;
        }
        for (int i = 0; i <= topo_frames; i++) {
            if (memory.getMemory()[pilhaFrames[i]].getNumber() == pagina) {
                return i;
            }
        }
        return frame;
    }

    public void loadMemory() {
        for (int i = 0; i < numFrames; i++) {
            memory.getMemory()[i] = new Frame();
        }
    }

    @Override
    public String getResults() {
        return this.report;
    }
}
