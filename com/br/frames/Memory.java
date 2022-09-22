package com.br.frames;

/**
 *
 * @author murilo
 */
public class Memory {
    private Frame[] frames;

    public Frame[] getMemory() {
        return frames;
    }

    public void setMemory(Frame[] memory) {
        this.frames = memory;
    }

    public Memory(int numFrames) {
        this.frames = new Frame[numFrames];
    }

    @Override
    public String toString() {
        String relatorio = "Frame\tPágina\tConteúdo\n";

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] != null) {
                
                if (frames[i].getPage() == null) {
                    relatorio += i + "\t-" + "\t----------\n";
                } else {
                    relatorio += i + "\t" + frames[i].getNumber() + "\t";
                    relatorio += frames[i].getPage().toString() + "\n";
                }
            } else {
                relatorio += i + "\t-" + "\t----------\n";
            }
        }
        return relatorio;
    }

}
