package com.br.gui;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.br.algorithms.Algorithm;
import com.br.algorithms.FIFO;
import com.br.algorithms.LRU;
import com.br.algorithms.SecondChance;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

public class Window extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel bottomPanel;
    private JPanel selectFolderPanel;
    private JPanel radioButtonsPanel;

    private JButton selectFolderButton;
    private JButton runButton;

    private JLabel titleLabel;
    private JLabel frameLabel;
    private JLabel uniquePagesLabel;
    private JLabel requiredPagesLabel;
    private JLabel statusLabel;
    private JLabel loadLabel;

    private JSpinner frameSpinner;
    private JSpinner uniquePagesSpinner;
    private JSpinner requiredPagesSpinner;

    private JRadioButton fifoAlgorithmRadioButton;
    private JRadioButton lruAlgorithmradioButton;
    private JRadioButton scAlgorithmRadioButton;

    private ButtonGroup radioGroup;

    private JFileChooser chooser;

    private JTextField pathField;

    private RelatoryWindow relatoryWindow;

    private boolean pathSelected = false;
    private boolean isRunning = false;

    private Algorithm algorithm;

    public Window() {
        this.initWindow();
        this.initComponent();
        this.pack();
    }

    public void initWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 500));
        this.setPreferredSize(new Dimension(400, 500));
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.setResizable(false);
        this.setTitle("Algoritmos de simulação de paginação");

        // create a empty panel to put a padding inside the frame
        JPanel contentPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        contentPanel.setBorder(padding);
        this.setContentPane(contentPanel);
        this.setIconImage(new ImageIcon(getClass().getResource("/assets/icon.png")).getImage());
    }

    public void initComponent() {

        // radio buttons
        this.fifoAlgorithmRadioButton = new JRadioButton("FIFO", true);
        this.fifoAlgorithmRadioButton.setFocusable(false);
        this.lruAlgorithmradioButton = new JRadioButton("LRU", false);
        this.lruAlgorithmradioButton.setFocusable(false);
        this.scAlgorithmRadioButton = new JRadioButton("Second Chance", false);
        this.scAlgorithmRadioButton.setFocusable(false);

        this.fifoAlgorithmRadioButton.addItemListener(radioAlgorithmListener);
        this.lruAlgorithmradioButton.addItemListener(radioAlgorithmListener);
        this.scAlgorithmRadioButton.addItemListener(radioAlgorithmListener);

        // buttons groups
        this.radioGroup = new ButtonGroup();
        this.radioGroup.add(fifoAlgorithmRadioButton);
        this.radioGroup.add(lruAlgorithmradioButton);
        this.radioGroup.add(scAlgorithmRadioButton);

        // Buttons
        this.selectFolderButton = new JButton("Selecionar Pasta");
        this.selectFolderButton.addActionListener(selectFolderListener);
        this.selectFolderButton.setSize(new Dimension(150, 20));
        this.selectFolderButton.setPreferredSize(new Dimension(150, 20));
        this.selectFolderButton.setBackground(Color.decode("#5EAD79"));
        this.selectFolderButton.setForeground(Color.WHITE);

        this.runButton = new JButton("Executar");
        this.runButton.addActionListener(runListener);
        this.runButton.setEnabled(false);
        this.runButton.setBackground(Color.decode("#5EAD79"));
        this.runButton.setForeground(Color.WHITE);

        // Labels
        this.titleLabel = new JLabel("<html>Preencha os campos para simular a execução dos algoritmos</html>");
        this.titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.frameLabel = new JLabel("N° de frames");
        this.uniquePagesLabel = new JLabel("N° de páginas únicas");
        this.requiredPagesLabel = new JLabel("N° de páginas requeridas");
        this.statusLabel = new JLabel("");
        this.loadLabel = new JLabel();

        // Spinners
        SpinnerModel model1 = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        this.frameSpinner = new JSpinner();
        this.frameSpinner.setModel(model1);

        SpinnerModel model2 = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        this.uniquePagesSpinner = new JSpinner();
        this.uniquePagesSpinner.setModel(model2);

        SpinnerModel model3 = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        this.requiredPagesSpinner = new JSpinner();
        this.requiredPagesSpinner.setModel(model3);

        // Panel
        this.topPanel = new JPanel(new GridLayout(2, 1));
        this.middlePanel = new JPanel(new GridLayout(3, 2));
        this.bottomPanel = new JPanel(new GridLayout(3, 1));
        this.radioButtonsPanel = new JPanel(new GridLayout(1, 3));

        // fields
        this.pathField = new JTextField();
        this.pathField.setEditable(false);
        this.pathField.setPreferredSize(new Dimension(180, 20));
        this.pathField.setSize(new Dimension(180, 20));

        // topPanel
        this.selectFolderPanel = new JPanel();
        this.selectFolderPanel.add(this.pathField);
        this.selectFolderPanel.add(this.selectFolderButton);

        this.topPanel.add(this.titleLabel);
        this.topPanel.add(this.selectFolderPanel);

        // middlePanel
        this.middlePanel.add(frameLabel);
        this.middlePanel.add(frameSpinner);
        this.middlePanel.add(uniquePagesLabel);
        this.middlePanel.add(uniquePagesSpinner);
        this.middlePanel.add(requiredPagesLabel);
        this.middlePanel.add(requiredPagesSpinner);

        // radioButtonsPanel
        this.radioButtonsPanel.add(fifoAlgorithmRadioButton);
        this.radioButtonsPanel.add(lruAlgorithmradioButton);
        this.radioButtonsPanel.add(scAlgorithmRadioButton);

        // bottomPanel
        this.bottomPanel.add(this.statusLabel);
        this.bottomPanel.add(this.loadLabel);
        this.bottomPanel.add(runButton);

        // mainPanel
        this.mainPanel = new JPanel(new GridLayout(4, 1));
        this.mainPanel.setPreferredSize(new Dimension(350, 400));
        this.mainPanel.add(this.topPanel);
        this.mainPanel.add(this.middlePanel);
        this.mainPanel.add(this.radioButtonsPanel);
        this.mainPanel.add(this.bottomPanel);

        this.add(mainPanel);
    }

    public void selectDestinyFolder() {
        this.chooser = new JFileChooser();
        // setting the default folder for file chooser
        this.chooser.requestFocusInWindow();
        this.chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.chooser.setPreferredSize(new Dimension(400, 600));
        this.chooser.setDialogTitle("Informe a pasta de destino para as páginas:");
        this.chooser.setSize(400, 600);
        this.chooser.showOpenDialog(null);

        // validates if a folder was selected
        if (this.chooser.getSelectedFile() != null) {
            this.pathField.setText(this.chooser.getSelectedFile().getPath());
            this.pathSelected = true;
            this.runButton.setEnabled(true);
        } else {
            this.pathField.setText("");
        }
    }

    public void run() {
        String path = this.pathField.getText() + "\\";
        int frameNum, uniquePages, requiredPages;
        ThreadWindow threadWindow;

        if(this.isRunning){
            this. relatoryWindow.setVisible(true);
            return;
        }
        frameNum = (int) this.frameSpinner.getValue();
        uniquePages = (int) this.uniquePagesSpinner.getValue();
        requiredPages = (int) this.requiredPagesSpinner.getValue();
        
        this.isRunning = true;
        this.relatoryWindow = new RelatoryWindow(this);
        this.relatoryWindow.setText("");
        this.statusLabel.setText("Gerando resultados . . . Tempo mínimo de espera: " + requiredPages + "s");

        if(this.fifoAlgorithmRadioButton.isSelected()) {
            this.algorithm = new FIFO(path, frameNum, uniquePages, requiredPages);
            this.relatoryWindow.setTitle("FIFO");
            threadWindow = new ThreadWindow(new RelatoryWindowRunnable());
            threadWindow.start();
        }  
        if(this.lruAlgorithmradioButton.isSelected()) {
            this.algorithm = new LRU(path, frameNum, uniquePages, requiredPages);
            this.relatoryWindow.setTitle("LRU");
            threadWindow = new ThreadWindow(new RelatoryWindowRunnable());
            threadWindow.start();
        } 
        if(this.scAlgorithmRadioButton.isSelected()) {
            this.algorithm = new SecondChance(path, frameNum, uniquePages, requiredPages);
            this.relatoryWindow.setTitle("Second Chance");
            threadWindow = new ThreadWindow(new RelatoryWindowRunnable());
            threadWindow.start();
        } 

        this.revalidate();
    }

    public void enableButtons() {
        this.runButton.setEnabled(true);
        this.fifoAlgorithmRadioButton.setEnabled(true);
        this.lruAlgorithmradioButton.setEnabled(true);
        this.scAlgorithmRadioButton.setEnabled(true);
        this.selectFolderButton.setEnabled(true);
    }

    public void disableButtons() {
        this.runButton.setEnabled(false);
        this.fifoAlgorithmRadioButton.setEnabled(false);
        this.lruAlgorithmradioButton.setEnabled(false);
        this.scAlgorithmRadioButton.setEnabled(false);
        this.selectFolderButton.setEnabled(false);
        this.relatoryWindow.setVisible(true);
    }

    private class ThreadWindow extends Thread {
        public ThreadWindow(RelatoryWindowRunnable runnable) {
            super(runnable);
        }
    }

    private class RelatoryWindowRunnable implements Runnable {
        @Override
        public void run() {
            disableButtons();
            Thread executionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    algorithm.simulate();
                }
            });
            executionThread.start();
            do {
                try {
                    relatoryWindow.setText(algorithm.getRelatory() + "\n\n");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("InterruptedException in updateRelatory: " + e.getMessage());
                }
            } while (algorithm.isRunning());
            statusLabel.setText("Execução finalizada");
            enableButtons();
            isRunning = false;
        }
    }

    // Listeners to handle with buttons events
    private ActionListener runListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            run();
        }
    };

    private ActionListener selectFolderListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectDestinyFolder();
        }
    };

    private ItemListener radioAlgorithmListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if ((!fifoAlgorithmRadioButton.isSelected() && !lruAlgorithmradioButton.isSelected() && !scAlgorithmRadioButton.isSelected()) || (!pathSelected)) {
                runButton.setEnabled(false);
            } else {
                if (!isRunning)
                {
                    runButton.setEnabled(true);
                }
            }
        }
    };
}
