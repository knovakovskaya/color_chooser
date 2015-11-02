package ru.ifmo.thesis.gui;

import ru.ifmo.thesis.gui.ColorPanel;
import ru.ifmo.thesis.gui.util.ColorChoosenActionListener;
import ru.ifmo.thesis.gui.util.ImagePanel;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.ValidationEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;


public class ChooseColorsApplication extends JFrame {

    private ImagePanel ImgPane;
    private ColorPanel ColorPane;

    public JButton Load;
    public JButton Save;


    private final int DEFAULT_WIDTH = 1024;
    private final int DEFAULT_HEIGHT = 600;

    private String filename;

    ChooseColorsApplication(){
        filename = "";
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Choose colors application");
        setContentPane(createContentPane());
        setVisible(true);
        revalidate();
    }

    private JPanel createContentPane(){
        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        ImgPane = new ImagePanel(null, 20);
        ImgPane.setBorder(BorderFactory.createLineBorder(Color.black));

        ColorPane = new ColorPanel(colorWasChoosenActionListener);

        JPanel MenuPane = new JPanel();
        Load = new JButton("Open new image");
        Load.addActionListener(openImageActionListener);
        Save = new JButton("Save colors");
        Save.addActionListener(saveImageActionListener);
        Save.setEnabled(false);

        MenuPane.add(Load, BorderLayout.LINE_START);
        MenuPane.add(Save, BorderLayout.LINE_END);

        contentPane.add(ImgPane, getYPercentGBC(70,0));
        contentPane.add(ColorPane, getYPercentGBC(28,1));
        contentPane.add(MenuPane, getYPercentGBC(2,2));
        return contentPane;
    }

    private GridBagConstraints getYPercentGBC(int yperc, int posy){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = posy;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = yperc;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        return gbc;
    }


    private static BufferedImage loadImage(String filename) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println(e.toString() + " Image '" + filename + "' not found.");
        }
        return result;
    }


    public ActionListener openImageActionListener = new ActionListener(  ) {
        public void actionPerformed(ActionEvent event) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif");
            JFileChooser chooser = new JFileChooser(Paths.get(filename).toAbsolutePath().toString());
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filename = chooser.getSelectedFile().getAbsolutePath();
            }
            if (filename.length() != 0) {
                ImgPane.setImage(loadImage(filename));
            } else {
                ImgPane.setImage(null);
            }
            filename = "";
            ColorPane.clearAll();
            updateChildren();
        }
    };

    public ActionListener saveImageActionListener = new ActionListener(  ) {
        public void actionPerformed(ActionEvent event) {
            if (!ColorPane.validateChoosenColors())
                return;
            String SaveFilename = "";
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showSaveDialog(getParent());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                SaveFilename = chooser.getSelectedFile().getAbsolutePath();
            }
            try{
                PrintWriter writer = new PrintWriter(SaveFilename, "UTF-8");
                writer.print(ColorPane.toString());
                writer.close();
            } catch (IOException e){
                System.err.println("Can't save results. Got exception: " + e.toString() );
            }
            updateChildren();
        }
    };

    private ColorChoosenActionListener colorWasChoosenActionListener = new ColorChoosenActionListener() {
        public void actionPerformed(ActionEvent event) {
            ColorPane.colorWasChoosen = true;
        }

        public void buttonCheck(){
            Save.setEnabled(ColorPane.validateChoosenColors());
        }
    };

    private void updateChildren(){
        revalidate();
        repaint();
    }


    public static void main(String[] args) throws Exception{
        ChooseColorsApplication main = new ChooseColorsApplication();
    }

}
