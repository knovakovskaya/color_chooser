package ru.ifmo.thesis.gui;

import ru.ifmo.thesis.gui.util.ColorChoosenActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

public class ColorPanel extends JPanel {
    final int N = 5;
    public boolean colorWasChoosen;
    public Color colors[];
    public double percentage[];
    public ColorChoosenActionListener occac;

    public ColorPanel(ColorChoosenActionListener onChooseColor){
        colors = new Color[N];
        percentage = new double[N];
        occac = onChooseColor;
        for (int i = 0; i < N; ++i){
            colors[i] = null;
            percentage[i] = 0;
        }
        colorWasChoosen = false;
        setLayout(new GridLayout(0, N));
        setVisible(true);
        colorize();
    }

    public boolean validateChoosenColors(){
        double perc = 0;
        for (int i = 0; i < N; ++i){
            if (colors[i] == null)
                return false;
            perc += percentage[i];
        }
        return perc >= 0 && perc <= 100;
    }

    public void clearAll(){
        removeAll();
        for (int i = 0; i < N; ++i){
            colors[i] = null;
            percentage[i] = 0;
        }
        colorize();
        occac.buttonCheck();
    }

    public String toString(){
        String result = "";
        for (int i = 0; i < N; ++i){
            result += String.format("%02X", colors[i].getRed()) +
                      String.format("%02X", colors[i].getGreen()) +
                      String.format("%02X", colors[i].getBlue());
            result += " " + Double.toString(percentage[i]) + "\n";
        }
        return result;
    }

    private void setColorBoxes(){
        final int MAX_IN_LINE = 10;
        removeAll();
        if (N <= MAX_IN_LINE){
            setLayout(new GridLayout(0, N));
        } else {
            int rows = (N-1)/MAX_IN_LINE+1;
            for (int i = 0; i < MAX_IN_LINE+1; ++i){
                if (((N+rows-1)/(i+1) == rows) &&
                        (N-rows*(i+1)+1 < 2)){
                    setLayout(new GridLayout(0, i + 1));
                    return;
                }
            }
        }
    }

    public void colorize(){
        if (colors != null){
            setColorBoxes();
            for (int i = 0; i < N; ++i) {
                SingleColorPanel rectangle = new SingleColorPanel(colors[i], percentage[i], i);
                rectangle.addMouseListener(new ColorPaneMouseListener());
                add(rectangle);
            }
            revalidate();
            repaint();
        }
    }

    private class SingleColorPanel extends JPanel {
        public final int index;
        private final Color color;
        private final double percentage;

        public SingleColorPanel(final Color color, double p, int ind) {
            this.color = color;
            this.percentage = p;
            this.index = ind;
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        public Color getTextColor() {
            if (color != null && color.getBlue() + color.getGreen() + color.getRed() < 255)
                return Color.white;
            return Color.black;
        }

        public Dimension getTextPosition() {
            int dx = (percentage < 10) ? 3 : 7;
            if (getWidth() / 2 < 20 + dx)
                return new Dimension(dx, getHeight() + 2);
            if (color == null)
                return new Dimension(getWidth() / 2 - 10, getHeight() / 2 + 2);
            return new Dimension(getWidth() / 2 - 20 - dx, getHeight() / 2 + 2);
        }


        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, getWidth(), getHeight());
            if (color != null) {
                g.setColor(color);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            double round = (double) ((int) (percentage * 100)) / 100;
            String ps = String.valueOf(round) + "%";
            if (round == 0)
                ps = "0.00%";
            if (getWidth() > 60) {
                g.setColor(getTextColor());
                g.setFont(new Font("default", Font.BOLD, 16));
                if (color != null) {
                    g.drawString(ps, getTextPosition().width, getTextPosition().height);
                } else {
                    g.drawString("+", getTextPosition().width, getTextPosition().height);
                }
            }
        }
    }


    private class ColorPaneMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent event) {
            SingleColorPanel me = (SingleColorPanel)event.getSource();
            CustomColorChooser jcc = new CustomColorChooser();
            jcc.createDialog(me, "Choose color", true, jcc, occac, null).setVisible(true);
            if (colorWasChoosen){
                colors[me.index] = new Color(jcc.getColor().getRGB());
                percentage[me.index] = jcc.getPerc();
                colorWasChoosen = false;
            }
            occac.buttonCheck();
            colorize();
        }
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
        public void mousePressed(MouseEvent event) {}
        public void mouseReleased(MouseEvent event) {}
    }




}
