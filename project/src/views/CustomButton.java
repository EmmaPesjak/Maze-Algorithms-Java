package views;

import support.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Button component class, used for generic buttons in the GUI.
 */
public class CustomButton extends JButton {

    /**
     * Constructor setting the design of the button.
     * @param text is the text displayed in the button.
     */
    public CustomButton(String text) {
        this.setText(text);
        this.setBackground(Constants.COLOR_BUTTON);
        this.setFont(Constants.FONT_BUTTON);
        this.setForeground(Constants.COLOR_BUTTON_TEXT);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
