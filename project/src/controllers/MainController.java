package controllers;

import models.MainModel;
import support.Constants;
import views.CustomPanel;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 */
public class MainController {

    private final MainModel mainModel = new MainModel(this::rePick);
    private final MainView mainView = new MainView(new SelectButtonListener(), new SolveButtonListener(), new RestartButtonListener());

    /**
     * Responsible for redirecting the user to change coordinates if no valid path was found.
     * @throws IOException exception.
     */
    private void rePick() throws IOException {
        mainView.init();
        String file = mainView.getFileName();
        CustomPanel img = mainModel.getMaze(file);
        mainView.showMaze(img);
        mainView.displayErrorMsg(Constants.ERR_NO_PATH);
    }

    /**
     *  Inner class responsible for listening to the select maze button.
     */
    class SelectButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Objects.equals(mainView.getFileName(), "")) { // Ensure that the user has entered a file name.
                try {
                    // Get values before calling show-maze.
                    String file = mainView.getFileName();

                    CustomPanel img = mainModel.getMaze(file);
                    mainView.showMaze(img);

                } catch (IOException ex) {
                    mainView.displayErrorMsg(Constants.ERR_NO_VALID_FILE);
                }
            } else {
                mainView.displayErrorMsg(Constants.ERR_NO_FILE_NAME);
            }
        }
    }

    /**
     * Inner class responsible for listening to the solve button.
     */
    class SolveButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            Point start = mainView.getStartCoords();
            Point end = mainView.getFinishCoords();
            mainModel.showPoints(); // Make sure that the start and finish points are showing.

            // Verify that the points are within maze or on an open path.
            if (!mainModel.checkIfValid(start, end)){
                try {
                    rePick();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }

            long startTime, endTime;
            CustomPanel heapPath, dequePath, aStarPath;

            // Run the algorithms and calculate the paths
            startTime = System.nanoTime();
            heapPath = mainModel.displayPath(start, end, Constants.DIJK_HEAP);
            endTime = System.nanoTime();
            long heapTime = (endTime - startTime) / 1000; // To micro seconds.

            // Measure the execution time of the second algorithm
            startTime = System.nanoTime();
            dequePath = mainModel.displayPath(start, end, Constants.DIJK_DEQ);
            endTime = System.nanoTime();
            long dequeTime = (endTime - startTime) / 1000; // To micro seconds.

            // Measure the execution time of the third algorithm
            startTime = System.nanoTime();
            aStarPath = mainModel.displayPath(start, end, Constants.ASTAR);
            endTime = System.nanoTime();
            long aStarTime = (endTime - startTime) / 1000; // To micro seconds.

            // Display the results
            SwingUtilities.invokeLater(() -> {
                mainView.displayResults(heapPath, dequePath, aStarPath, heapTime, dequeTime, aStarTime);
            });
        }

    }

    /**
     * Inner class responsible for listening to the restart button.
     */
    class RestartButtonListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            mainModel.clearPoints();
            mainView.init();
        }
    }
}
