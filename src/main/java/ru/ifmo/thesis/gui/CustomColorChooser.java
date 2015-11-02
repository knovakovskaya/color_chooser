package ru.ifmo.thesis.gui;


import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class CustomColorChooser extends JColorChooser {

    public CustomColorChooser(){
        for (AbstractColorChooserPanel panel: getChooserPanels()) {
            if (!panel.getDisplayName().equals("HSV")) {
                removeChooserPanel(panel);
            } else {
                try {
                    Field f1 = panel.getClass().getDeclaredField("panel");
                    f1.setAccessible(true);

                    Object colorPanel = f1.get(panel);
                    Field f2 = colorPanel.getClass().getDeclaredField("spinners");
                    f2.setAccessible(true);
                    Object spinners = f2.get(colorPanel);

                    Object transpSlispinner = Array.get(spinners, 3);
                    Field f3 = transpSlispinner.getClass().getDeclaredField("label");
                    f3.setAccessible(true);
                    JLabel label = (JLabel) f3.get(transpSlispinner);
                    label.setText("Percentage");
                    label.setVisible(false);
                } catch (Exception e) {
                }
            }
        }
        setPreviewPanel(new JPanel());
    }

    public double getPerc(){
        double perc = 0;
        for (AbstractColorChooserPanel panel: getChooserPanels()) {
            if (panel.getDisplayName().equals("HSV")) {
                try {
                    Field f1 = panel.getClass().getDeclaredField("panel");
                    f1.setAccessible(true);

                    Object colorPanel = f1.get(panel);
                    Field f2 = colorPanel.getClass().getDeclaredField("spinners");
                    f2.setAccessible(true);
                    Object spinners = f2.get(colorPanel);

                    Object transpSlispinner = Array.get(spinners, 3);
                    Field f3 = transpSlispinner.getClass().getDeclaredField("slider");
                    f3.setAccessible(true);
                    JSlider slider = (JSlider) f3.get(transpSlispinner);

                    perc = Double.valueOf((double)slider.getValue());
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return perc;
    }


}
