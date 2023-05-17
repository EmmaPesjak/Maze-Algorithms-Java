package controllers;

import models.MainModel;
import support.Constants;
import views.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The program's main controller. Responsible for the communication between views and models,
 * responding to user input.
 */
public class MainController {

    private final MainModel mainModel = new MainModel();
    private final MainView mainView = new MainView(new SelectButtonListener(), new SolveButtonListener(), new RestartButtonListener());

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

                    JPanel img = mainModel.getMaze(file);
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
        @Override
        public void actionPerformed(ActionEvent e) {

            // Run the algorithms and calculate the paths
            mainModel.showPoints(); // Make sure that the start and finish points are showing.
            Point start = mainView.getStartCoords();
            Point end = mainView.getFinishCoords();

            // Kolla läget så den faktiskt är fina grejer.
            if (!mainModel.checkIfValid(start, end)){
                mainView.displayErrorMsg(Constants.ERR_COORD);

                // #TODO Gör så att användaren kan välja nya koordinater!
                mainModel.clearPoints();
                return;
            }

            mainView.displayLoadingPanel();
            long startTime, endTime, totStart, totEnd;
            JPanel path1, path2, path3;

            totStart = System.currentTimeMillis();

            startTime = System.currentTimeMillis();
            path1 = mainModel.displayPath(start, end, Constants.DIJK_HEAP);
            endTime = System.currentTimeMillis();
            long algorithm1Time = endTime - startTime;
            System.out.println("1: " + algorithm1Time);

            // Measure the execution time of the second algorithm
            startTime = System.currentTimeMillis();
            path2 = mainModel.displayPath(start, end, Constants.DIJK_DEQ);
            endTime = System.currentTimeMillis();
            long algorithm2Time = endTime - startTime;
            System.out.println("2: " + algorithm2Time);

            // Measure the execution time of the third algorithm
            startTime = System.currentTimeMillis();
            path3 = mainModel.displayPath(start, end, Constants.ASTAR);
            endTime = System.currentTimeMillis();
            long algorithm3Time = endTime - startTime;
            System.out.println("3: " + algorithm3Time);

            totEnd = System.currentTimeMillis();
            long total = totEnd - totStart;

            // Display the execution times
            System.out.println("Total: " + total);

            // Display the results
            SwingUtilities.invokeLater(() -> {
                mainView.displayResults(path1, path2, path3);
                mainView.closeLoadingPanel();
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
