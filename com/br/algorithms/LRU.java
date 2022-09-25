package com.br.algorithms;

import com.br.pages.Page;
import com.br.frames.Frame;
import com.br.frames.Memory;

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
        this.pagesPath = pagesPath;
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
                Thread.sleep(threadWait);
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

    /**
     * Método para desenfileirar o próximo frame de memória disponível
     * @return
     */
    public int dequeueMemory() {
        int pos = this.inicioFilaFrames;
        this.inicioFilaFrames++;
        return pos;
    }

    /**
     * Método para desempilhar a pilha de frames ocupados
     * @return
     */
    public int unstackFrame() {
        int pos = -1;

        if (this.topo_frames != -1) {
            pos = pilhaFrames[this.topo_frames];
            this.topo_frames--;
        }
        return pos;
    }

    /*
     * Método para empilhar um frame na pilha de frames ocupados
     * @param x
    */
    public void stackFrame(int x) {

        if (this.topo_frames != this.numFrames - 1) {
            this.topo_frames++;
            this.pilhaFrames[topo_frames] = x;
        }

    }

    /**
     * Método para desempilhar a pilha auxiliar de frames ocupados
     * @return
     */
    public int unstackAux() {
        int pos = -1;

        if (this.topoAux != -1) {
            pos = pilhaAux[this.topoAux];
            this.topoAux--;
        }

        return pos;
    }

    /*
     * Método para empilhar um frame na pilha auxiliar de frames ocupados
     * @param x
    */
    public void stackAux(int x) {

        if (this.topoAux != this.numFrames - 1) {
            this.topoAux++;
            this.pilhaAux[topoAux] = x;
        }

    }

    /**
     * Método para atualizar uma página que um frame guarda
     * @param nFrame
     * @param numPag
     * @param pagina
     */
    public void updateFrame(int nFrame, int numPag, Page pagina) {
        memory.getMemory()[nFrame].setNumber(numPag);
        memory.getMemory()[nFrame].setPage(pagina);
    }

    /**
     * Método para adicionar uma nova página a um frame desocupado
     * @param numPage
     * @param pagina
     */
    public void addPage(int numPage, Page pagina) {
        int nFrame = dequeueMemory();

        stackFrame(nFrame);
        updateFrame(nFrame, numPage, pagina);
    }

    /**
     * Método para atualizar a pilha de frames ocupados, o frame recém acessado vai para o topo da fila
     * @param x
     */
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

    /**
     * Método responsável por trocar a página de um frame. O frame que terá sua página trocada será aquele que estiver no 
     * inicio da pilha de frames ocupados, ou seja, o que não foi acessado a mais tempo.
     * @param numPage
     * @param page
     */
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

    /*
     * Método para gerar a sequência aleatória de páginas requeridas
     */
    public void generatePageRequest() {
        Random random = new Random();
        for (int i = 0; i < this.numPagReq; i++) {
            PagReq[i] = random.nextInt(this.uniquePages);
        }
        this.isRunning = true;
    }

    /**
     * Busca a página passada como argumento na pilha de frames ocupados.
     * Se a página estiver alocada em algum frame, é retornada a posição desse frame na memória, senão, -1.
     * @param pagina
     * @return
     */
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

    /*
     * Instância um frame para cada posição da memória, um array de frames.
     */
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
