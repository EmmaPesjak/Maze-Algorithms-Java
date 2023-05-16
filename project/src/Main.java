import controllers.MainController;

import javax.swing.*;

/**
 * The main starting point for the project.
 */
public class Main {

    /**
     * Create the Controller to start the program.
     * @param args are command arguments. Not relevant here.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainController::new);
    }
}