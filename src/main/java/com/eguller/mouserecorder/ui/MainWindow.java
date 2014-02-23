package com.eguller.mouserecorder.ui;

import com.eguller.mouserecorder.player.PlayerImpl;
import com.eguller.mouserecorder.player.api.Player;
import com.eguller.mouserecorder.recorder.BaseRecorder;
import com.eguller.mouserecorder.recorder.api.Recorder;
import com.eguller.mouserecorder.ui.action.ExitAction;
import com.eguller.mouserecorder.ui.state.ButtonStates;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * User: eguller
 * Date: 11/24/13
 * Time: 3:28 PM
 */
public class MainWindow extends JFrame implements Observer {
    JMenuBar menuBar;
    JButton playButton;
    JButton recordButton;
    JButton stopButton;


    JMenu fileMenu;
    JMenuItem saveItem;
    JMenuItem openItem;
    JSeparator separator;
    JMenuItem exitItem;

    JMenu aboutMenu;
    JMenu aboutItem;
    JMenu helpItem;

    JLabel statusBar;

    Image startImage;
    Image recordImage;
    Image stopImage;

    JPanel buttonPanel;

    Recorder recorder;
    Player player;

    public MainWindow(Recorder recorder, Player player) {
        addComponentstoPane(getContentPane());
        this.setResizable(false);
        this.recorder = recorder;
        this.player = player;
        player.addObserver(this);
    }

    private void addComponentstoPane(Container container) {
        loadImages();
        playButton = new JButton(new ImageIcon(startImage));
        playButton.setEnabled(false);
        playButton.addActionListener(new PlayAction());

        recordButton = new JButton(new ImageIcon(recordImage));
        recordButton.addActionListener(new RecordAction());

        stopButton = new JButton(new ImageIcon(stopImage));
        stopButton.addActionListener(new StopAction());
        stopButton.setEnabled(false);


        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveItem = new JMenuItem("Save");
        openItem = new JMenuItem("Open");
        separator = new JSeparator();
        exitItem = new JMenuItem("Exit");


        fileMenu.add(saveItem);
        fileMenu.add(openItem);
        fileMenu.add(separator);
        fileMenu.add(exitItem);

        aboutMenu = new JMenu("About");
        aboutItem = new JMenu("About");
        helpItem = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);

        statusBar = new JLabel();
        statusBar.setFont(statusBar.getFont().deriveFont(10.0f));
        statusBar.setHorizontalAlignment(SwingConstants.RIGHT);

        buttonPanel = new JPanel();
        buttonPanel.add(playButton, BorderLayout.WEST);
        buttonPanel.add(stopButton, BorderLayout.CENTER);
        buttonPanel.add(recordButton, BorderLayout.EAST);

        buttonPanel.setBorder(new EmptyBorder(3, 10, 3, 10));

        ButtonStates.PRERECORD.apply(this);

        container.add(menuBar, BorderLayout.PAGE_START);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(statusBar, BorderLayout.PAGE_END);
        setResizable(false);
        setSize(512, 512);
        setTitle("Mouse BaseRecorder");

        addActionListeners();
    }

    public void addActionListeners(){
        exitItem.addActionListener(new ExitAction());
    }

    public void loadImages() {
        try {
            startImage = ImageIO.read(getClass().getResourceAsStream("/start.png"));
            recordImage = ImageIO.read(getClass().getResourceAsStream("/record.png"));
            stopImage = ImageIO.read(getClass().getResourceAsStream("/stop.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Observable observable, Object o) {
        ButtonStates.POSTPLAY.apply(MainWindow.this);

    }

    /**
     * Record button action listener.
     */
    public class RecordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ButtonStates.RECORDING.apply(MainWindow.this);
            recorder.record();
        }
    }

    public class PlayAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ButtonStates.PLAYING.apply(MainWindow.this);
            player.play(recorder.getRecord());

        }
    }

    public class StopAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ButtonStates.POSTRECORD.apply(MainWindow.this);
            recorder.stop();
        }
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getRecordButton() {
        return recordButton;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

}
