package views;

import support.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main View class of the program, used for the GUI of the application.
 */
public class MainView extends JFrame {

    private final JPanel panel = new JPanel();
    private final JButton solveButton = new JButton("Solve Maze");
    private final ActionListener listener;
    private final JTextField textField = new JTextField(30);

    /**
     * Constructor which initiates the GUI and sets an action listener.
     * @param listener is the action listener.
     */
    public MainView(ActionListener listener) {
        this.listener = listener;
        init();
    }

    /**
     * Initiator for setting up the GUI.
     */
    private void init() {
        this.setResizable(false);
        this.setSize(1000, 650); // Annan storlek?
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(Constants.COLOR_BACKGROUND);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createRigidArea(new Dimension(0, 100)));

        JLabel title = new JLabel("Maze Solver");
        title.setFont(Constants.FONT_BIG);
        title.setForeground(Constants.COLOR_TEXT);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel textLabel = new JLabel("Enter the file name of the maze you want to solve:");
        textLabel.setForeground(Constants.COLOR_TEXT);
        textLabel.setFont(Constants.FONT_TEXT);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(textLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        textField.setFont(Constants.FONT_TEXT);
        textField.setMaximumSize(textField.getPreferredSize());
        panel.add(textField);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        solveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        solveButton.setBackground(Constants.COLOR_BUTTON);
        solveButton.setFont(Constants.FONT_BUTTON);
        solveButton.setForeground(Constants.COLOR_BUTTON_TEXT);
        solveButton.addActionListener(listener);
        panel.add(solveButton);

        this.add(panel);
        this.revalidate();
        this.repaint();
    }

    /**
     * Getter for the file name user input-
     * @return the file name.
     */
    public String getFileName() {
        return textField.getText();
    }

    /**
     * Displays the result of the solves mazes.
     */
    public void displayResults() {
        panel.removeAll();

        // Här printar vi ut de solvade mazarna sen. Skicka dem från model på någon vänster?
        // Köra någon sorts BorderLayout och lägga mazarna i west, center, east?

        this.revalidate();
        this.repaint();
    }

    /**
     * Method for displaying error messages.
     * @param errorMsg is the error message string.
     */
    public void displayErrorMsg (String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg);
    }
}
