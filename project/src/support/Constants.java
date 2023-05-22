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
    Color COLOR_START = new Color(255, 186, 127);
    Color COLOR_END =  new Color(122, 107, 156);
    Color COLOR_PATH = new Color(222, 142, 192);

    // Fonts.
    Font FONT_BIG = new Font(Font.MONOSPACED, Font.BOLD, 52);
    Font FONT_TEXT = new Font(Font.MONOSPACED, Font.PLAIN, 28);
    Font FONT_SMALL_TEXT = new Font(Font.MONOSPACED, Font.PLAIN, 18);
    Font FONT_BUTTON = new Font(Font.MONOSPACED, Font.BOLD, 32);

    // Values.
    int MAX_PANEL_WIDTH = 700;  // Måste vara 700, annars tar du bort väggar i bilderna
    int MAX_PANEL_HEIGHT = 700;

    // Error messages.
    String ERR_NO_FILE_NAME = "Please enter a file name.";
    String ERR_NO_VALID_FILE = "Please enter a valid file name or make sure that the file exists.";
    String ERR_COORD = "Please enter start- and finish-coordinates within maze on open path.";
    String ERR_NO_PATH = "No valid paths found. Please re-pick start- and finish-coordinates within maze on open path.";

    // Algorithms
    String DIJK_HEAP = "dijkstraHeap";
    String DIJK_DEQ = "dijkstraDequeue";
    String ASTAR = "aStar";

    // GUI text.
    String TITLE = "Maze Solver";
    String ENTER_NAME = "Enter the file name of the maze you want to solve:";
    String SELECT_BUTTON = "Select Maze";
    String SELECT_START = "Select the start";
    String SELECT_FINISH = "Select the finish";
    String SOLVE_BUTTON = "Solve Maze";
    String TEXT_D_HEAP = "Dijkstra with heap";
    String TEXT_D_DEQ = "Dijkstra with dequeue";
    String TEXT_ASTAR = "A*";
    String RESTART_BUTTON = "Run new maze";
}
