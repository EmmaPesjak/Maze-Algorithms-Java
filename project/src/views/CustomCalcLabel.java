package views;

import support.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Label component class, used for labels showing the calculations in the GUI.
 */
public class CustomCalcLabel extends JLabel {

    /**
     * Constructor setting the design of the label.
     * @param text is the text displayed in the label.
     */
    public CustomCalcLabel(String text) {
        this.setText(text);
        this.setForeground(Constants.COLOR_TEXT);
        this.setFont(Constants.FONT_TEXT);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
