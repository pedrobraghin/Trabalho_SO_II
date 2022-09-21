package com.br.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;


public class RelatoryWindow extends JFrame{
    
    private JPanel mainPanel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Window window;

    public RelatoryWindow(Window window) {
        this.initWindow();
        this.initComponent();
        this.pack();
        this.window = window;
    }

    public void initWindow() {
        this.setPreferredSize(new Dimension(500, 600));
        this.setSize(new Dimension(500, 600));
        this.setLocationRelativeTo(window);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    public void close() {
        this.setVisible(false);
    }

    public void initComponent() {
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(450, 600));
        this.textArea = new JTextArea();
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setEditable(false);

        this.scrollPane = new JScrollPane(textArea);
        this.scrollPane.setPreferredSize(new Dimension(450, 500));
        this.mainPanel.add(scrollPane);
        this.add(mainPanel);
    }

    public void setText(String text) {
        this.textArea.append(text);
        this.mainPanel.revalidate();        
        this.scrollPane.getVerticalScrollBar().setValue(this.scrollPane.getVerticalScrollBar().getMaximum());
    }

}
