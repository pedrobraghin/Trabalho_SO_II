package com.br.algorithms;
/**
 * @author Pedro Braghin
 */
public abstract class Algorithm {
    public abstract void simulate();
    public abstract String getReport();
    public abstract boolean isRunning();
    public abstract String populatePages();
    public abstract void generatePages();
    public abstract String getResults();
}
