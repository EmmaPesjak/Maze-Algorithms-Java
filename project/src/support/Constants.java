package support;

import java.awt.*;

/**
 * Interface that define constant values.
 */
public interface Constants {

    // Colors.
    Color COLOR_BACKGROUND = new Color(0, 94, 98);
    Color COLOR_TEXT = new Color(176, 222, 115);
    Color COLOR_BUTTON = new Color(59, 161, 122);
    Color COLOR_BUTTON_TEXT = new Color(192, 225, 148);

    // Fonts.
    Font FONT_BIG = new Font(Font.MONOSPACED, Font.BOLD, 52);
    Font FONT_TEXT = new Font(Font.MONOSPACED, Font.PLAIN, 28);
    Font FONT_SMALL_TEXT = new Font(Font.MONOSPACED, Font.PLAIN, 18);
    Font FONT_BUTTON = new Font(Font.MONOSPACED, Font.BOLD, 32);

    // Values.
    int INFINITY = Integer.MAX_VALUE;

    // Strings.

    String NO_PATH = "No shortest path was found!";
    String ENTER_FILE = "Please enter a valid file name.";
}
