package views;

import support.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main View class of the program, used for the GUI of the application.
 */
public class MainView extends JFrame {

    private final JPanel panel = new JPanel();
    private final ActionListener selectListener;
    private final ActionListener solveListener;
    private final ActionListener restartListener;
    private final JTextField textField = new JTextField(30);
    private int clickCount = 0;
    private Point startCoords;
    private Point finishCoords;
    private final JPanel explanationPanel = new JPanel();
    private final JLabel explanationLabel = new JLabel();
    private final JLabel coordLabel = new JLabel();


    private final JPanel loadingPanel = new JPanel();


    /**
     * Constructor which initiates the GUI and sets an action listener.
     * @param solveListener is the action listener.
     */
    public MainView(ActionListener selectListener, ActionListener solveListener, ActionListener restartListener) {
        this.selectListener = selectListener;
        this.solveListener = solveListener;
        this.restartListener = restartListener;
        init();
    }

    /**
     * Initiator for setting up the GUI.
     */
    public void init() {
        panel.removeAll(); // Clear the panel.
        this.setResizable(false);
        this.setSize(1000, 600); // Annan storlek?
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

        final CustomButton selectButton = new CustomButton("Select Maze");
        selectButton.addActionListener(selectListener);
        panel.add(selectButton);

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
     * Shows the selected maze and lets the user pick start and finish coordinates.
     * @param maze is the maze.
     */
    public void showMaze(JPanel maze) {
        panel.removeAll(); // Clear the panel.

        coordLabel.setFont(Constants.FONT_SMALL_TEXT);
        coordLabel.setForeground(Constants.COLOR_TEXT);
        coordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanationLabel.setFont(Constants.FONT_SMALL_TEXT);
        explanationLabel.setForeground(Constants.COLOR_TEXT);
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        maze.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                clickCount++;
                if (clickCount == 1) {
                    explanationLabel.setText("Select the finish");
                    startCoords = e.getPoint();
                    coordLabel.setText("Start Coordinates: x=" + startCoords.x + ", y=" + startCoords.y);
                    panel.add(coordLabel);
                    panel.revalidate();
                } else if (clickCount == 2) {
                    explanationPanel.removeAll(); // Remove old text.
                    finishCoords = e.getPoint();
                    coordLabel.setText("Finish Coordinates: x=" + finishCoords.x + ", y=" + finishCoords.y);
                    panel.add(coordLabel);
                    final CustomButton solveButton = new CustomButton("Solve Maze");
                    solveButton.addActionListener(solveListener);
                    explanationPanel.add(solveButton);
                    panel.revalidate();
                }
            }

            // Not needed by the application but have to be overridden.
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        coordLabel.setText(" ");
        explanationLabel.setText("Select the start");

        explanationPanel.setBackground(Constants.COLOR_BACKGROUND);
        explanationPanel.add(explanationLabel);

        panel.add(explanationPanel);
        panel.add(maze);
        panel.add(coordLabel);

        this.pack(); // To get the right size for the frame.
        this.revalidate();
        this.repaint();
    }

    /**
     * Getter for the start coordinates.
     * @return the coordinates.
     */
    public Point getStartCoords() {
        return startCoords;
    }

    /**
     * Getter for the finish coordinates.
     * @return the coordinates.
     */
    public Point getFinishCoords() {
        return finishCoords;
    }

    public void displayLoadingPanel() {

        // #TODO Vartfan kommer den svarta bakgrunden från?????? VARFÖR BLIR ALLT SÅ FULT ??? och, varför funkar inte
        //  animeringen på min gif :(((( funkade fan innan.

        // Clear the current content
        panel.removeAll(); // Clear the panel.
        explanationPanel.removeAll(); // Clear the panel.

        panel.setBackground(Constants.COLOR_BACKGROUND);

        panel.setLayout(new BorderLayout());
        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);

        // Create a label to display the loading GIF
        JLabel loadingLabel = new JLabel(new ImageIcon("project/src/support/spinner.gif"));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLoadingLabel = new JLabel("Finding the shortest path...");
        textLoadingLabel.setFont(Constants.FONT_BIG);
        textLoadingLabel.setForeground(Constants.COLOR_TEXT);

        // Add the label to the panel
        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(textLoadingLabel, BorderLayout.SOUTH);

        // Add the loading panel to the view
        this.pack(); // To get the right size for the frame.
        this.revalidate();
        this.repaint();
    }

    public void closeLoadingPanel() {
        // Remove the loading panel from the view
        remove(loadingPanel);
        revalidate();
        repaint();
    }

    /**
     * Displays the result of the solves mazes.
     */
    public void displayResults(JPanel mazeDijkstraOne, JPanel mazeDijkstraTwo, JPanel mazeAStar) {

        // TODO: se till så att alla 3 paneler får plats/inget knäppt mellanrum

        clickCount = 0; // reset the click count for potential next solved maze
        panel.removeAll(); // Clear the panel.
        explanationPanel.removeAll(); // Clear the panel.

        panel.setBackground(Constants.COLOR_BACKGROUND);
        mazeDijkstraOne.setBackground(Constants.COLOR_BACKGROUND);
        mazeDijkstraTwo.setBackground(Constants.COLOR_BACKGROUND);
        mazeAStar.setBackground(Constants.COLOR_BACKGROUND);

        panel.setLayout(new BorderLayout());

        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Constants.COLOR_BACKGROUND);

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        westPanel.setBackground(Constants.COLOR_BACKGROUND);
        westPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        westPanel.add(mazeDijkstraOne);
        panel.add(westPanel, BorderLayout.WEST);

        JLabel label1 = new JLabel("Dijkstra with Prio Queue");
        label1.setForeground(Constants.COLOR_TEXT);
        label1.setFont(Constants.FONT_TEXT);
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1, 10, 10));
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(1, 3, 10, 5));

        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        centerPanel.add(mazeDijkstraTwo);
        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);

        panel.add(centerPanel, BorderLayout.CENTER);

        JLabel label2 = new JLabel("Dijkstra with deque");
        label2.setForeground(Constants.COLOR_TEXT);
        label2.setFont(Constants.FONT_TEXT);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.setBackground(Constants.COLOR_BACKGROUND);
        eastPanel.add(mazeAStar, BorderLayout.WEST);
        eastPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
        panel.add(eastPanel, BorderLayout.EAST);

        JLabel label3 = new JLabel("A*");
        label3.setForeground(Constants.COLOR_TEXT);
        label3.setFont(Constants.FONT_TEXT);
        label3.setHorizontalAlignment(JLabel.CENTER);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);

        southPanel.setBackground(Constants.COLOR_BACKGROUND);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Constants.COLOR_BACKGROUND);

        final CustomButton restartButton = new CustomButton("Run new maze");
        buttonPanel.add(restartButton);
        restartButton.addActionListener(restartListener);

        labelsPanel.add(label1);
        labelsPanel.add(label2);
        labelsPanel.add(label3);
        labelsPanel.setBackground(Constants.COLOR_BACKGROUND);

        southPanel.add(labelsPanel);
        southPanel.add(buttonPanel);

        panel.add(southPanel, BorderLayout.SOUTH);

        this.pack(); // To get the right size for the frame.
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
