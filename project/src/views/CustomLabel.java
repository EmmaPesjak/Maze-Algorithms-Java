package views;

import support.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Label component class, used for generic labels in the GUI.
 */
public class CustomLabel extends JLabel {

    /**
     * Constructor setting the design of the label.
     */
    public CustomLabel() {
        this.setForeground(Constants.COLOR_TEXT);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
