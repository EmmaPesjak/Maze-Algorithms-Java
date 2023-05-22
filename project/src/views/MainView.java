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
    //private final ActionListener goBackListener;
    private final JTextField textField = new JTextField(30);
    private int clickCount = 0;
    private Point startCoords;
    private Point finishCoords;
    private final JPanel explanationPanel = new JPanel();
    private final JLabel explanationLabel = new JLabel();
    private final JLabel coordLabel = new JLabel();
    private JPanel maze;

    /**
     * Constructor which initiates the GUI and sets action listeners.
     * @param selectListener
     * @param solveListener
     * @param restartListener
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
        this.setSize(1000, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(Constants.COLOR_BACKGROUND);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createRigidArea(new Dimension(0, 100)));

        JLabel title = createLabel(Constants.TITLE, Constants.FONT_BIG);

        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel textLabel = createLabel(Constants.ENTER_NAME, Constants.FONT_TEXT);

        panel.add(textLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        textField.setFont(Constants.FONT_TEXT);
        textField.setMaximumSize(textField.getPreferredSize());
        panel.add(textField);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        final CustomButton selectButton = new CustomButton(Constants.SELECT_BUTTON);
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
     * @param mazeArg is the maze.
     */
    public void showMaze(JPanel mazeArg) {
        this.maze = mazeArg;
        panel.removeAll(); // Clear the panel.

        coordLabel.setFont(Constants.FONT_SMALL_TEXT);
        coordLabel.setForeground(Constants.COLOR_TEXT);
        coordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanationLabel.setFont(Constants.FONT_MEDIUM);
        explanationLabel.setForeground(Constants.COLOR_TEXT);
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clickCount = 0;

        maze.setLayout(null); // Set layout manager to null for absolute positioning when adding dots.

        maze.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                clickCount++;
                if (clickCount == 1) {
                    explanationLabel.setText(Constants.SELECT_FINISH);
                    startCoords = e.getPoint();
                    coordLabel.setText("Start Coordinates: x=" + startCoords.x + ", y=" + startCoords.y);
                    panel.add(coordLabel);
                    panel.revalidate();
                    addDot(maze, startCoords, true);
                } else if (clickCount == 2) {
                    explanationPanel.removeAll(); // Remove old text.
                    finishCoords = e.getPoint();
                    coordLabel.setText("Finish Coordinates: x=" + finishCoords.x + ", y=" + finishCoords.y);
                    panel.add(coordLabel);
                    final CustomButton solveButton = new CustomButton(Constants.SOLVE_BUTTON);
                    solveButton.addActionListener(solveListener);
                    explanationPanel.add(solveButton);
                    panel.revalidate();
                    addDot(maze, finishCoords, false);
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
        explanationLabel.setText(Constants.SELECT_START);

        explanationPanel.setBackground(Constants.COLOR_BACKGROUND);
        explanationPanel.add(explanationLabel);

        panel.add(explanationPanel);
        panel.add(maze);
        panel.add(coordLabel);

        //TODO: fungerar ej
//        JButton goBackButton = new CustomButton("Go back");
//        goBackButton.addActionListener(restartListener);
//        panel.add(goBackButton);

        this.pack(); // To get the right size for the frame.
        this.revalidate();
        this.repaint();
    }

    /**
     * Method for adding a dot where the user presses.
     * @param maze is the maze image.
     * @param point is the coordinates of the dot.
     * @param isStart determines the color of the dot.
     */
    private void addDot(JPanel maze, Point point, boolean isStart) {
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (isStart) {
                    g2d.setColor(Constants.COLOR_START);
                } else {
                    g2d.setColor(Constants.COLOR_END);
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };

        dot.setSize(10, 10);
        dot.setOpaque(false);
        dot.setBorder(BorderFactory.createEmptyBorder());
        dot.setLayout(null);

        int dotX = point.x - dot.getWidth() / 2;
        int dotY = point.y - dot.getHeight() / 2;
        dot.setLocation(dotX, dotY);

        maze.add(dot);
        maze.revalidate();
        maze.repaint();
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

    /**
     * Displays the result of the solves mazes.
     */
    public void displayResults(JPanel mazeDijkstraOne, JPanel mazeDijkstraTwo, JPanel mazeAStar,
                                long time1, long time2, long time3) {

        String s1 = Constants.HTML_RESULT_START + Constants.TEXT_D_HEAP + Constants.HTML_RESULT_MID + time1 + Constants.HTML_RESULT_END;
        String s2 = Constants.HTML_RESULT_START + Constants.TEXT_D_DEQ + Constants.HTML_RESULT_MID + time2 + Constants.HTML_RESULT_END;
        String s3 = Constants.HTML_RESULT_START + Constants.TEXT_ASTAR + Constants.HTML_RESULT_MID + time3 + Constants.HTML_RESULT_END;
        JLabel heapLbl = createLabel(s1, Constants.FONT_TEXT);
        JLabel dequeLbl = createLabel(s2, Constants.FONT_TEXT);
        JLabel aStarLbl = createLabel(s3, Constants.FONT_TEXT);

        JPanel centerPanel = createCustomPanel(new BorderLayout(),
                Box.createRigidArea(new Dimension(10, 0)), mazeDijkstraTwo);

        JPanel westPanel = createCustomPanel(new BorderLayout(),
                Box.createRigidArea(new Dimension(10, 0)), mazeDijkstraOne);

        JPanel eastPanel = createCustomPanel(new BorderLayout(),
                Box.createRigidArea(new Dimension(10, 0)), mazeAStar);

        clickCount = 0; // reset the click count for potential next solved maze
        panel.removeAll(); // Clear the panel.
        explanationPanel.removeAll(); // Clear the panel.

        panel.setBackground(Constants.COLOR_BACKGROUND);
        mazeDijkstraOne.setBackground(Constants.COLOR_BACKGROUND);
        mazeDijkstraTwo.setBackground(Constants.COLOR_BACKGROUND);
        mazeAStar.setBackground(Constants.COLOR_BACKGROUND);

        panel.setLayout(new BorderLayout());

        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);

        //JPanel centerPanel = new JPanel();
        //centerPanel.setLayout(new BorderLayout());
        //centerPanel.setBackground(Constants.COLOR_BACKGROUND);

        //JPanel westPanel = new JPanel();
        //westPanel.setLayout(new BorderLayout());
        //westPanel.setBackground(Constants.COLOR_BACKGROUND);
        //westPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        //westPanel.add(mazeDijkstraOne);
        //panel.add(westPanel, BorderLayout.WEST);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1, 10, 10));
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(1, 3, 10, 5));

        //centerPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        //centerPanel.add(mazeDijkstraTwo);
        //centerPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);

        //panel.add(centerPanel, BorderLayout.CENTER);

        //JPanel eastPanel = new JPanel();
        //eastPanel.setLayout(new BorderLayout());
        //eastPanel.setBackground(Constants.COLOR_BACKGROUND);
        //eastPanel.add(mazeAStar, BorderLayout.WEST);
        //eastPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
        //panel.add(eastPanel, BorderLayout.EAST);

        southPanel.setBackground(Constants.COLOR_BACKGROUND);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Constants.COLOR_BACKGROUND);

        final CustomButton restartButton = new CustomButton(Constants.RESTART_BUTTON);
        buttonPanel.add(restartButton);
        restartButton.addActionListener(restartListener);

        labelsPanel.add(heapLbl);
        labelsPanel.add(dequeLbl);
        labelsPanel.add(aStarLbl);
        labelsPanel.setBackground(Constants.COLOR_BACKGROUND);

        southPanel.add(labelsPanel);
        southPanel.add(buttonPanel);

        panel.add(southPanel, BorderLayout.SOUTH);
        panel.add(westPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST);

        this.pack(); // To get the right size for the frame.
        this.revalidate();
        this.repaint();

    }

    /**
     * Method for displaying error messages.
     * @param errorMsg is the error message string.
     */
    public void displayErrorMsg (String errorMsg) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, errorMsg);
        });
    }

    /**
     * Creates a label with specified properties.
     * @param text The label text.
     * @param font The font of the label.
     * @return The created label.
     */
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Constants.COLOR_TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createCustomPanel(LayoutManager layout, Component... components) {
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBackground(Constants.COLOR_BACKGROUND);

        for (Component component : components) {
            panel.add(component);
        }

        return panel;
    }
}
